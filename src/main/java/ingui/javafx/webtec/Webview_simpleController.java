/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingui.javafx.webtec;

import innui.modelos.comunicaciones.sockets.sslcontext_sin_verificar_hostnames;
import innui.modelos.configuraciones.ResourceBundles;
import innui.modelos.errores.oks;
import innui.modelos.internacionalizacion.tr;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * FXML Controller class
 *
 * @author emilio
 */
public class Webview_simpleController implements Initializable {
    public static String k_in_ruta = "in/ingui/javafx/webtec/in";  //NOI18N
    public ResourceBundle in = null;
    public static interface I_Webview_simpleController_extensiones {
        /**
         * Procesa una URL (reemplazable en Webview_simpleController_hijo)
         * @param url URL que procesar
         * @param ok
         * @param extras_array
         * @return null si hay error o no se debe procesar la petición; o el texto resultante de procesar la petición
         * @throws java.lang.Exception
         */
        public Boolean procesar_evento_llamada_a_url(URL url, oks ok, Object ... extras_array) throws Exception;
        /**
         * Escribe una copia del texto del Webview
         * @param texto
         * @param ok
         * @param extras_array
         * @return true si tiene éxito, o false si hay error
         * @throws java.lang.Exception
         */
        public boolean escribir_texto(String texto, oks ok, Object ... extras_array) throws Exception;
        /**
         * Lee una copia del texto del Webview
         * @param ok
         * @param extras_array
         * @return true si tiene éxito, o false si hay error
         * @throws java.lang.Exception
         */
        public String leer_texto(oks ok, Object ... extras_array) throws Exception;
    }
    public static interface I_Webview_simpleController_capturas {
        public boolean poner_error(String mensaje, oks ok, Object ... extras_array);
        public default boolean procesar_estado(String estado, String url_texto, oks ok, Object ... extras_array) throws Exception {
            return true;
        }
        public default boolean procesar_avance(Number avance, oks ok, Object ... extras_array) throws Exception {
            return true;
        }
    }
    
    public Webview_simpleController() throws Exception {
        in = ResourceBundles.getBundle(k_in_ruta);
    }
    @FXML
    private WebView webview;

    public String webview_texto = "";
    public I_Webview_simpleController_extensiones i_Webview_simpleController_extension;
    public I_Webview_simpleController_capturas i_Webview_simpleController_captura;
    public WebEngine webEngine;
    public Number avance_en_peticion;
    public String estado_en_procesamiento;
    public boolean es_intentar_sin_certificado_valido = false;
    public int presentar_contenido_seguro_intentos_num = 0;
    public boolean es_cancelar_peticion_en_curso = false;
    public sslcontext_sin_verificar_hostnames sslcontext_sin_verificar_hostname = new sslcontext_sin_verificar_hostnames();
    public URI uri = null;
    /**
     * Método que pone el escuchador de cambios de URL
     * Llama a poner_error, si hay error.
     * @param url (No se utiliza)
     * @param rb (No se utiliza)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oks ok = new oks();
        try {
            webEngine = webview.getEngine();
            sslcontext_sin_verificar_hostname.inicial_SSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            while (true) {
                poner_escuchador_cambios_url(ok);
                if (ok.es == false) { break; }
                poner_escuchador_de_errores(ok);
                if (ok.es == false) { break; }
                poner_escuchador_de_progreso(ok);
                if (ok.es == false) { break; }
                poner_escuchador_de_cambio(ok);
                break;
            }
            if (ok.es == false) {
                poner_error(ok.txt, ok);
            }
        } catch (Exception e) {
            ok.setTxt(e);
            poner_error(ok.txt, ok);
        }
    } 
    public boolean poner_escuchador_de_errores(oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        webEngine.setOnError(new EventHandler<WebErrorEvent>() {
            @Override
            public void handle(WebErrorEvent webErrorEvent) {
                try {
                    oks ok = new oks();
                    ok.setTxt(webErrorEvent.getMessage());
                    ok.setTxt(tr.in(in, "ERROR EN EL MOTOR DE PRESENTACIÓN DE PÁGINAS WEB. ")
                            , ok.txt);
                    poner_error(ok.txt, ok);
                } catch (Exception e) {
                    try {
                        ok.setTxt(tr.in(in, "ERROR EN EL MOTOR DE PRESENTACIÓN DE PÁGINAS WEB. ")
                            , e);
                    } catch (Exception ex) {}
                    poner_error(ok.txt, ok);
                }
            }
        });
        return ok.es;
    }   

    public boolean poner_escuchador_de_progreso(oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        webEngine.getLoadWorker().progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number arg1, Number arg2) {
                oks ok = new oks();
                try {
                    avance_en_peticion = observableValue.getValue();
                    procesar_avance(avance_en_peticion, ok);
                    if (ok.es == false) {
                        poner_error(ok.txt, ok);
                    }
                } catch (Exception e) {
                    ok.setTxt(e);
                    poner_error(ok.txt, ok);
                }
            }
        });    
        return ok.es;
    }
    
    public boolean poner_escuchador_de_cambio(oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue observableValue, State oldState, State newState) {
                oks ok = new oks();
                try {
                    String url_texto = webEngine.getLocation();
                    estado_en_procesamiento = newState.toString();
                    if (newState == Worker.State.SUCCEEDED) {
                        procesar_estado(estado_en_procesamiento, url_texto, ok);
                    } else if (newState == Worker.State.READY) {
                        procesar_estado(estado_en_procesamiento, url_texto, ok);
                    } else if (newState == Worker.State.RUNNING) {
                        procesar_estado(estado_en_procesamiento, url_texto, ok);
                    } else if (newState == Worker.State.SCHEDULED) {
                        procesar_estado(estado_en_procesamiento, url_texto, ok);
                    } else if (newState == Worker.State.FAILED) {
                        procesar_estado(estado_en_procesamiento, url_texto, ok);
                    } else if (newState == Worker.State.CANCELLED) {
                        procesar_estado(estado_en_procesamiento, url_texto, ok);
                    }
                    if (uri != null) {
                        URI uri_procesada = new URI(url_texto);
                        boolean es_uri_parecida = true;
                        if (es_uri_parecida) {
                            if (uri_procesada.getHost() != null
                                    && uri.getHost() != null) {
                                es_uri_parecida = uri_procesada.getHost().contains(uri.getHost());
                            } else {
                                es_uri_parecida = false;
                            }
                        }
                        if (es_uri_parecida) {
                            if (uri_procesada.getHost() != null
                                    && uri.getHost() != null) {
                                es_uri_parecida = uri_procesada.getPath().contains(uri.getPath());
                            } else {
                                es_uri_parecida = false;
                            }
                        }
                        if (es_uri_parecida) {
                            if (uri_procesada.getQuery() != null
                                    && uri.getQuery() != null) {
                                    es_uri_parecida = uri_procesada.getQuery().equals(uri.getQuery());
                            } else if (uri_procesada.getQuery() == null
                                    && uri.getQuery() == null) {
                                es_uri_parecida = true;
                            } else {
                                es_uri_parecida = false;
                            }
                        }
                        if (es_uri_parecida) {
                            if (newState == Worker.State.FAILED) {
                                if (presentar_contenido_seguro_intentos_num == 0) {
                                    if (sslcontext_sin_verificar_hostname.actual_SSLSocketFactory != null) {
                                        presentar_contenido_seguro_intentos_num = 1;
                                        es_cancelar_peticion_en_curso = true;
                                        presentar_contenido_con_factoria_de_socket(uri, sslcontext_sin_verificar_hostname.actual_SSLSocketFactory, ok);
                                    }
                                } else if (presentar_contenido_seguro_intentos_num == 1) {
                                    sslcontext_sin_verificar_hostname.restaurar_SSLSocketFactory(ok);
                                    if (es_intentar_sin_certificado_valido) {
                                        presentar_contenido_seguro_intentos_num = 2;
                                        es_cancelar_peticion_en_curso = true;
                                        es_intentar_sin_certificado_valido = true;
                                        if (ok.es) {
                                            presentar_contenido_tls_sin_validar_certificado(uri, ok);
                                        }
                                    }
                                } else if (presentar_contenido_seguro_intentos_num == 2) {
                                    presentar_contenido_seguro_intentos_num = 0;
                                    es_intentar_sin_certificado_valido = false;
                                    sslcontext_sin_verificar_hostname.restaurar_HostnameVerifier(ok);
                                }
                            } else if (newState == Worker.State.CANCELLED) {
                                if (es_cancelar_peticion_en_curso) {
                                    es_cancelar_peticion_en_curso = false;
                                } else {
                                    if (presentar_contenido_seguro_intentos_num == 1) {
                                        presentar_contenido_seguro_intentos_num = 0;
                                        es_cancelar_peticion_en_curso = false;
                                        sslcontext_sin_verificar_hostname.restaurar_SSLSocketFactory(ok);
                                    } else if (presentar_contenido_seguro_intentos_num == 2) {
                                        es_intentar_sin_certificado_valido = false;
                                        presentar_contenido_seguro_intentos_num = 0;
                                        es_cancelar_peticion_en_curso = false;
                                        sslcontext_sin_verificar_hostname.restaurar_HostnameVerifier(ok);
                                    }
                                }
                            } else if (newState == Worker.State.SUCCEEDED) { 
                                if (presentar_contenido_seguro_intentos_num == 1) {
                                    presentar_contenido_seguro_intentos_num = 0;
                                    es_cancelar_peticion_en_curso = false;
                                    sslcontext_sin_verificar_hostname.restaurar_SSLSocketFactory(ok);
                                } else if (presentar_contenido_seguro_intentos_num == 2) {
                                    es_intentar_sin_certificado_valido = false;
                                    presentar_contenido_seguro_intentos_num = 0;
                                    es_cancelar_peticion_en_curso = false;
                                    sslcontext_sin_verificar_hostname.restaurar_HostnameVerifier(ok);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    try {
                        ok.setTxt(tr.in(in, "ERROR DE EXCEPCIÓN EN EL ESCUCHADOR DE CAMBIOS. "), e);
                    } catch (Exception ex) {}
                }   
                if (ok.es == false) {
                    poner_error(ok.txt, ok);
                }
            }
        });
        return ok.es;
    }
    /**
     * Método que añade un escuchador de cambios de url al objeto webView
     * @param ok
     * @param extras_array
     * @return true si tiene éxito, o false si hay error
     * @throws java.lang.Exception
     */
    public boolean poner_escuchador_cambios_url(oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        ReadOnlyStringProperty readOnlyStringProperty_location = webEngine.locationProperty();
        readOnlyStringProperty_location.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                oks ok = new oks();
                URL url;
                try {
                    if (newValue.isEmpty() == false) {
                        url = new URL(newValue.trim());
                        procesar_evento_llamada_a_url(url, ok);
                    }
                } catch (Exception e) {
                    try {
                        ok.setTxt(tr.in(in, "ERROR EN PONER_ESCUCHADOR_CAMBIOS_URL->CHANGED. ")
                        , e);
                    } catch (Exception ex) {}
                }   
                if (ok.es  == false) {
                    poner_error(ok.txt, ok);
                }
            }           
        });
        return ok.es;
    }
    /**
     * Procesa una URL (reemplazable en Webview_simpleController_hijo)
     * @param url URL que procesar
     * @param ok
     * @param extras_array
     * @return true si todo es correcto, false si hay error.
     * @throws java.lang.Exception
     */
    public boolean procesar_evento_llamada_a_url(URL url, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        Boolean ret_Boolean = true;
        if (i_Webview_simpleController_extension != null) {
            ok.iniciar();
            ret_Boolean = i_Webview_simpleController_extension.procesar_evento_llamada_a_url(url, ok);
            if (ret_Boolean == null) {
                ok.es = true;
            } else {
                ok.es = ret_Boolean;
            }
        }
        return ok.es;
    }
    /**
     * Pone un mensaje de error en el objeto webView
     * @param mensaje mensaje de error, si lo hay
     * @param ok
     * @param extras_array
     * @return true si tiene éxito, o false si hay error
     */
    public boolean poner_error(String mensaje, oks ok, Object ... extras_array) {
        if (i_Webview_simpleController_captura != null) {
            i_Webview_simpleController_captura.poner_error(mensaje, ok);
        } else {
            try {
                escribir_texto(mensaje, ok);
                if (ok.es) {
                    presentar_contenido(ok); //NOI18N
                }
            } catch (Exception e) {
                ok.setTxt(e);
            }
        }
        return ok.es;
    }
    /**
     * Carga el contenido en un hilo
     * @param ok
     * @param extras_array
     * @return true si tiene éxito, o false si hay error
     * @throws java.lang.Exception
     */
    public boolean presentar_contenido(oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        String contenido;
        final String contenido_final;
        uri = null;
        contenido = leer_texto(ok);
        if (contenido == null || ok.es == false) {
            contenido = ok.txt;
        }
        contenido_final = contenido;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webEngine.loadContent(contenido_final, "text/html");
            }
        });
        return ok.es;
    }
    /**
     * Carga el contenido en un hilo
     * @param uri URI de donde obtener el contenido
     * @param ok
     * @param extras_array
     * @return true si todo es correcto, false si hay error.
     * @throws java.lang.Exception
     */
    public boolean presentar_contenido(URI uri, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        String texto = uri.toString();
        this.uri = uri;
        webEngine.load(texto);
        return ok.es;
    }
   /**
    * Presenta el contenido empleando sockets seguros
    * @param uri URI de donde obtener el contenido
    * @param sSLSocketFactory Factoría de sockets seguros; null si no se debe utilizar ninguna.
    * @param es_intentar_sin_certificado_valido Si es verdad, y la primera llamada falla, se hace un segundo intento; ignorando la validación de certificado.
     * @param ok
     * @param extras_array
    * @return true si todo es correcto, false si hay error.
     * @throws java.lang.Exception
    */
    public boolean presentar_contenido(URI uri, SSLSocketFactory sSLSocketFactory, boolean es_intentar_sin_certificado_valido
            , oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        this.es_intentar_sin_certificado_valido = es_intentar_sin_certificado_valido;
        sslcontext_sin_verificar_hostname.actual_SSLSocketFactory = sSLSocketFactory;
        presentar_contenido(uri, ok);
        return ok.es;
    }

    public boolean presentar_contenido_con_factoria_de_socket(URI uri, SSLSocketFactory sSLSocketFactory
            , oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        String texto = uri.toString();
        this.uri = uri;
        if (sSLSocketFactory != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sSLSocketFactory);
        }
        webEngine.load(texto);
        return ok.es;
    }
    
    public boolean presentar_contenido_tls_sin_validar_certificado(URI uri, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        String texto = uri.toString();
        this.uri = uri;
        // Crear un trust manager que lo valida todo
        TrustManager[] trustAllCerts = new TrustManager[] { 
            new X509TrustManager() {     
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
                    return null;
                } 
                @Override
                public void checkClientTrusted( 
                    java.security.cert.X509Certificate[] certs, String authType) {
                    } 
                @Override
                public void checkServerTrusted( 
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            } 
        }; 
        try {
            SSLContext sSLContext = SSLContext.getInstance("TLS"); 
            sSLContext.init(null, trustAllCerts, new java.security.SecureRandom()); 
            HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext.getSocketFactory());            
            sslcontext_sin_verificar_hostname.inicial_hostnameVerifier 
                    = HttpsURLConnection.getDefaultHostnameVerifier();
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            webEngine.load(texto);
        } catch (Exception e) {
            ok.setTxt(tr.in(in, "ERROR DE EXCEPCIÓN AL PRESENTAR CONTENIDO TLS SIN VALIDAR CERTIFICADO. ")
                , e);
        }   
        // webEngine.load(texto);
        return ok.es;
    }
    
    public boolean poner_texto(String mensaje, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        escribir_texto(mensaje, ok);
        if (ok.es) {
            presentar_contenido(ok);
        }
        return ok.es;
    }
    
    public boolean procesar_avance(Number avance, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        if (i_Webview_simpleController_captura != null) {
            i_Webview_simpleController_captura.procesar_avance(avance, ok);
        }
        return ok.es;
    }
    
    public synchronized Number leer_avance(oks ok, Object ... extras_array) throws Exception {
        return avance_en_peticion;
    }
    
    public boolean procesar_estado(String estado, String url_texto, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        if (i_Webview_simpleController_captura != null) {
            i_Webview_simpleController_captura.procesar_estado(estado, url_texto, ok);
        }
        return ok.es;
    }

    public synchronized String leer_estado(oks ok, Object ... extras_array) throws Exception {
        return estado_en_procesamiento;
    }
    
    public boolean agregar_objeto_de_extension(I_Webview_simpleController_extensiones i_webview_simpleController_extension
            , oks ok, Object ... extras_array) {
        if (ok.es == false) { return ok.es; }
        this.i_Webview_simpleController_extension = i_webview_simpleController_extension;
        return ok.es;
    }

    public boolean agregar_objeto_de_captura(I_Webview_simpleController_capturas i_webview_simpleController_captura
            , oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        this.i_Webview_simpleController_captura = i_webview_simpleController_captura;
        return ok.es;
    }
    /**
     * Escribe una copia del texto del Webview
     * @param texto
     * @param ok
     * @param extras_array
     * @return true si tiene éxito, o false si hay error
     * @throws java.lang.Exception
     */
    public boolean escribir_texto(String texto, oks ok, Object ... extras_array) throws Exception {
        if (i_Webview_simpleController_extension != null) {
            i_Webview_simpleController_extension.escribir_texto(texto, ok);
        } else {
            webview_texto = texto;
        }
        return ok.es;
    }
    /**
     * Lee una copia del texto del Webview
     * @param ok
     * @param extras_array
     * @return true si tiene éxito, o false si hay error
     * @throws java.lang.Exception
     */
    public String leer_texto(oks ok, Object ... extras_array) throws Exception {
        String retorno = null;
        if (i_Webview_simpleController_extension != null) {
            retorno = i_Webview_simpleController_extension.leer_texto(ok);
        } else {
            retorno = webview_texto;
        }
        return retorno;
    }
    /**
     * Equivalente a pulsar el botón atrás en el navegador
     * @param ok
     * @param extras_array
     * @return 
     * @throws Exception 
     */
    public boolean ir_adelante_en_historial_urls(oks ok, Object ... extras_array) throws Exception {
        Platform.runLater(() -> {
            webEngine.executeScript("history.back()");
        });
        return ok.es;
    }
    /**
     * Equivalente a pulsar el botón atrás en el navegador
     * @param ok
     * @param extras_array
     * @return 
     * @throws Exception 
     */
    public boolean ir_atras_en_historial_urls(oks ok, Object ... extras_array) throws Exception {
        Platform.runLater(() -> {
            webEngine.executeScript("history.forward()");
        });
        return ok.es;
    }    
}
    

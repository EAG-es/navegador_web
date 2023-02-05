/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingui.javafx.navegador_web;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import innui.modelos.comunicaciones.url.Urls;
import ingui.javafx.webtec.Webview_simpleController.I_Webview_simpleController_extensiones;
import innui.modelos.configuraciones.ResourceBundles;
import innui.modelos.errores.oks;
import innui.modelos.internacionalizacion.tr;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpHeaders;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author emilio
 */
public abstract class Webview_simpleController_implementaciones implements I_Webview_simpleController_extensiones {
    public static String k_in_ruta = "in/ingui/javafx/navegador_web/in";
    public static String k_protocolo_https = "https://";
    public static String k_protocolo_http = "http://";
    public static String k_url_formulario = k_protocolo_http + "navegador_web/formulario";
    public static String k_boton_enviar = "";
    public static String k_dir_servidor_web = "dir_servidor_web";
    public static int k_puerto_ssl = 443;
    public static String k_mensaje_aviso = "";
    public ResourceBundle in = null; 
    AtomicReference<String> contenido_web_atref = new AtomicReference<>();
    
    public Webview_simpleController_implementaciones() throws Exception {
        in = ResourceBundles.getBundle(k_in_ruta);
        k_boton_enviar = tr.in(in, "ENVIAR");
        k_mensaje_aviso = tr.in(in, "Solicitud de página empleando HTTP. No se recomienda este protocolo, debería emplear HTTPS. ");
    }
    /**
     * Procesa las peticiones URL que recibe el Webview_simpleController
     * @param url
     * @return true si todo es correcto, false si hay error.
     */
    @Override
    public Boolean procesar_url(URL url, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        // No hace nada, en esta aplicación. Un ejemplo sería si llamase a: _ejemplo_procesamiento_de_clic_en_url_del_navegador_web
        // _ejemplo_procesamiento_de_clic_en_url_del_navegador_web(url, ok);
        return ok.es;
    }
    
    public Boolean _ejemplo_procesamiento_de_clic_en_url_del_navegador_web(URL url, oks ok, Object ... extras_array) throws Exception {
        Map<String, String> datos_mapa = new LinkedHashMap<>();
        String url_texto;
        String texto;
        url_texto = url.toExternalForm();
        if (url_texto.startsWith(k_url_formulario)) {
            Urls.extraer_parametros_query(url, datos_mapa, ok);
            if (ok.es) {
                _procesar_formulario(datos_mapa, ok);
            }
        } else {
            URI uri = Urls.generar_uri(url_texto, ok);
            if (ok.es) {
                ok.es = (uri != null);
            }
            if (ok.es) {
                if (uri.getScheme().equals("http")) {
//                    texto = _procesar_con_httpclient(uri, error); // Opción 1
                    texto = _procesar_con_url(uri, ok); // Opción 2
                    if (ok.es) {
                        ok.es = (texto != null);
                    }
                    if (ok.es) {
                        texto = _corregir_html(uri, texto, ok);
                        if (ok.es) {
                            ok.es = (texto != null);
                        }
                    }
                    if (ok.es) {
                        texto = _modificar_html(texto, ok);
                        if (ok.es) {
                            ok.es = (texto != null);
                        }
                    }
                    if (ok.es) {
                        poner_texto(texto, ok);
                    }
                } else if (uri.getScheme().equals("https")) {
                    presentar_contenido(uri, ok);
                } else {
                    ok.setTxt(java.text.MessageFormat.format(ResourceBundles.getBundle("in/clientes_web_filtrador/ingui/javafx/in").getString("PROTOCOLO NO SOPORTADO: {0}"), new Object[] {uri.getScheme()}));
                    ok.es = false;
                }
            }
        }
        if (ok.es == false) {
            _poner_error_en_formulario(ok.txt, ok);
        }
        return ok.es;
    }
    /**
     * Prepara el formulario de conexión
     * @param ok
     * @param extras_array
     * @return true si tiene éxito, false si hay error.
     * @throws java.lang.Exception
     */
    public boolean _cargar_formulario (oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        String texto = "";
        texto = 
        "<html><body>" +
        "<form method='GET' style='background-color: aliceblue' action='" + k_url_formulario + "'><label style='font-weight: bold'>"
        + in.getString("INTRODUZCA LA DIRECCIÓN WEB DEL SERVIDOR")
        + " </label><br><input style='width:100%' name='" + k_dir_servidor_web + "' type='text' /> <br>" 
        + "<button type='submit' name='" + k_boton_enviar + "'>"
        + in.getString("ENVIAR")
        + "</button><br>" 
        + "</form>"
        + "</body></html>";
        poner_texto(texto, ok);
        return ok.es;
    }
    /**
     * Presenta un texto
     * @param texto
     * @param ok
     * @param extras_array
     * @return true si todo es correcto, false si hay error.
     * @throws java.lang.Exception
     */
    public boolean poner_texto(String texto, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        contenido_web_atref.set(texto);
        if (ok.es) {
            presentar_contenido(ok);
        }
        return ok.es;
    }
    /**
     * Presenta un texto
     * @param texto
     * @param ok
     * @param extras_array
     * @return true si todo es correcto, false si hay error.
     * @throws java.lang.Exception
     */
    public boolean _poner_error_en_formulario(String texto, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        _cargar_formulario(ok);
        if (ok.es) {
            poner_error(texto, ok);
        }
        return ok.es;
    }
    /**
     * Presenta un texto
     * @param texto
     * @param ok
     * @param extras_array
     * @return true si todo es correcto, false si hay error.
     * @throws java.lang.Exception
     */
    public boolean poner_error(String texto, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        String mensaje = contenido_web_atref.get();
        String nuevo_mensaje = "";
        ok.es = (mensaje != null);
        if (ok.es == false) {
            mensaje = texto;
        }
        if (mensaje.contains("<body>")) {
            nuevo_mensaje = mensaje.replace("<body>", "<body><h3 style='color:red'>" + texto + "</h3>");
            contenido_web_atref.set(nuevo_mensaje);
            if (ok.es) {
                presentar_contenido(ok);
            }
            if (ok.es) {
                contenido_web_atref.set(mensaje);
            }
        } else {
            contenido_web_atref.set(texto);
            if (ok.es) {
                presentar_contenido(ok);
            }
        }
        return ok.es;
    }
    /**
     * Escribe un texto
     * @param ok
     * @param extras_array
     * @return true si todo es correcto, false si hay error.
     * @throws java.lang.Exception
     */
    @Override
    public boolean escribir_texto(String texto, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        contenido_web_atref.set(texto);
        return ok.es;
    }
    /**
     * Lee un texto
     * @param ok
     * @param extras_array
     * @return true si todo es correcto, false si hay error.
     * @throws java.lang.Exception
     */
    @Override
    public String leer_texto(oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return null; }
        return contenido_web_atref.get();
    }
    /**
     * Carga el contenido en un hilo
     * @param ok
     * @param extras_array
     * @return true si tiene éxito, o false si hay error
     * @throws java.lang.Exception
     */
    public abstract boolean presentar_contenido(oks ok, Object ... extras_array) throws Exception;
    /**
     * Carga el contenido en un hilo
     * @param uri
     * @param ok
     * @param extras_array
     * @return true si tiene éxito, o false si hay error
     * @throws java.lang.Exception
     */
    public abstract boolean presentar_contenido(URI uri, oks ok, Object ... extras_array) throws Exception;
    /**
     * 
     * @param datos_mapa
     * @param ok
     * @param extras_array
     * @return true si todo es correcto, false si hay error.
     * @throws java.lang.Exception
     */
    public boolean _procesar_formulario(Map<String, String> datos_mapa, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        String texto;
        URI uri = null;
        texto = datos_mapa.get(k_dir_servidor_web);
        if (texto == null) {
            ok.setTxt(tr.in(in, "NO SE HA RECIBIDO EL PARÁMETRO {0}"), new Object[] {k_dir_servidor_web});
        }
        if (ok.es) {
            uri = Urls.generar_uri(texto, ok);
            if (ok.es) {
                ok.es = (uri != null);
            }
            if (ok.es) {
                if (uri.getScheme().equals("http")
                        || uri.getScheme().equals("https")
                        || uri.getScheme().equals("file")) {
                    // Inserte aquí el código necesario para filtrar las peticiones realizadas en la URL                    
                    texto = _procesar_con_url(uri, ok); // Opción 1
//                    texto = _procesar_con_httpclient(uri, error); // Opción 2
                    if (ok.es) {
                        ok.es = (texto != null);
                    }
                    if (ok.es) {
                        texto = _corregir_html(uri, texto, ok);
                        if (ok.es) {
                            ok.es = (texto != null);
                        }
                    }
                    if (ok.es) {
                        if (uri.getScheme().equals("http")) {
                            texto = _modificar_html(texto, ok);
                            if (ok.es) {
                                ok.es = (texto != null);
                            }
                        }
                    }
                    if (ok.es) {
                        poner_texto(texto, ok);
                    }
                } else if (uri.getScheme().equals("https")) {
                    // Inserte aquí el código necesario para filtrar las peticiones realizadas en la URL
                    presentar_contenido(uri, ok);
                } else {
                    ok.setTxt(java.text.MessageFormat.format(tr.in(in, "PROTOCOLO NO SOPORTADO: {0}"), new Object[] {uri.getScheme()}));
                }
            }
        }
        return ok.es;
    }
    /**
     * 
     * @param uri
     * @param ok
     * @param extras_array
     * @return el texto leido, null si hay error
     * @throws java.lang.Exception
     */
    public String _procesar_con_url(URI uri, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return null; }
        String retorno = null;
        String content_type;
        String charset;
        URL url = null;
        InputStream inputStream;
        URLConnection uRLConnection = null;
        HttpURLConnection httpURLConnection;
        int response_code;
        int tam;
        int pos;
        byte [] byte_array;
        String [] texto_array;
        try {
            url = uri.toURL();
            uRLConnection = url.openConnection();
            if (uRLConnection instanceof HttpURLConnection) {
                httpURLConnection = (HttpURLConnection) uRLConnection;
                response_code = httpURLConnection.getResponseCode();
                if (response_code >= 300 && response_code < 400) {
                    String location = httpURLConnection.getHeaderField("Location");
                    url = new URL(location);
                    retorno = _procesar_con_url(url.toURI(), ok);
                } else {
                    if (response_code != 200) {
                        ok.setTxt("Error en la respuesta. " 
                                + response_code
                                + " " + httpURLConnection.getResponseMessage());
                    }
                    if (ok.es) {
                        content_type = httpURLConnection.getContentType();
                        if (content_type == null) {
                            content_type = "text/html";
                        }
                        if (content_type.toLowerCase().contains("text/html")) {
                            charset = httpURLConnection.getContentEncoding();
                            if (charset == null) {
                                charset = "UTF-8";
                                texto_array = content_type.split(";");
                                for (String texto: texto_array) {
                                    pos = texto.toLowerCase().indexOf("charset=");
                                    if (pos >= 0) {
                                        charset = texto.substring(pos + "charset=".length());
                                        break;
                                    }
                                }
                            }
                            inputStream = httpURLConnection.getInputStream();
                            byte_array = new byte[1024];
                            retorno = "";
                            while (true) {
                                tam = inputStream.read(byte_array);
                                if (tam == -1) {
                                    break;
                                }
                                retorno = retorno + new String(byte_array, 0, tam, charset);
                            }
                        }
                    }
                }
            } else {
                inputStream = uRLConnection.getInputStream();
                byte_array = new byte[1024];
                retorno = "";
                while (true) {
                    tam = inputStream.read(byte_array);
                    if (tam == -1) {
                        break;
                    }
                    retorno = retorno + new String(byte_array, 0, tam, "UTF-8");
                }
            }
        } catch (Exception e) {
            ok.setTxt("Error al procesar la petición HTTP. ", e);
            retorno = null;
        }
        return retorno;
    }
    
    /**
     * 
     * @param uri
     * @param ok
     * @param extras_array
     * @return el texto leido, null si hay error
     * @throws java.lang.Exception
     */
    public String _procesar_con_httpclient(URI uri, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return null; }
        String retorno = null;
        String content_type;
        String charset;
        URI uri_destino;
        InputStream inputStream;
        int response_code;
        int tam;
        int pos;
        byte [] byte_array;
        String [] texto_array;
        HttpClient.Builder httpclient_builder;
        HttpClient httpClient;
        HttpResponse<InputStream> httpresponse;
        HttpRequest.Builder httpRequest_builder;
        HttpRequest httprequest;
        HttpHeaders httpHeader;
        try {
            httpclient_builder = HttpClient.newBuilder();
            httpClient = httpclient_builder.build();
            httpRequest_builder = HttpRequest.newBuilder(uri);
            httprequest = httpRequest_builder.build();
            httpresponse = httpClient.send(httprequest, BodyHandlers.ofInputStream());
            response_code = httpresponse.statusCode();
            httpHeader = httpresponse.headers();
            if (response_code >= 300 && response_code < 400) {
                String location = httpHeader.firstValue("Location").get();
                uri_destino = new URI(location);
                retorno = _procesar_con_httpclient(uri_destino, ok);
            } else {
                if (response_code != 200) {
                    ok.setTxt("Error en la respuesta. " 
                            + response_code
                            + " " + httpHeader.firstValue(null).get());
                }
                if (ok.es) {
                    content_type = httpHeader.firstValue("Content-Type").get();
                    if (content_type == null) {
                        content_type = "text/html";
                    }
                    if (content_type.toLowerCase().contains("text/html")) {
                        charset = "UTF-8";
                        texto_array = content_type.split(";");
                        for (String texto: texto_array) {
                            pos = texto.toLowerCase().indexOf("charset=");
                            if (pos >= 0) {
                                charset = texto.substring(pos + "charset=".length());
                                break;
                            }
                        }
                        inputStream = httpresponse.body();
                        byte_array = new byte[1024];
                        retorno = "";
                        while (true) {
                            tam = inputStream.read(byte_array);
                            if (tam == -1) {
                                break;
                            }
                            retorno = retorno + new String(byte_array, 0, tam, charset);
                        }
                    }
                } else {
                    inputStream = httpresponse.body();
                    byte_array = new byte[1024];
                    retorno = "";
                    while (true) {
                        tam = inputStream.read(byte_array);
                        if (tam == -1) {
                            break;
                        }
                        retorno = retorno + new String(byte_array, 0, tam, "UTF-8");
                    }
                }
                }
        } catch (Exception e) {
            ok.setTxt("Error al procesar la petición HTTP. "
                    + " " + Arrays.asList(e.getStackTrace()).toString(), e);
            retorno = null;
        }
        return retorno;
    }

    public String _modificar_html(String contenido_html, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return null; }
        String retorno;
        int tam = contenido_html.length();
        retorno = contenido_html.replaceFirst("(?i)<body.*>", "<body\\1><h1 style='color:red;'>"
        + k_mensaje_aviso
        + "</h1><div style='position: absolute;top: 0; left: 0; z-index:10;background:#ffc; padding:5px; border:1px solid #CCCCCC'><h1 style='color:red;'>"
        + k_mensaje_aviso
        + "</h1></div>");
        if (tam == retorno.length()) {
            // No es texto HTML
            retorno = "<html><body><pre>"
                    + retorno
                    + "<pre></body></html>";
        }
        return retorno;
    }

    public String _corregir_html(URI uri, String contenido_html, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return null; }
        String retorno = contenido_html;
        String auth_host;
        auth_host = uri.getRawAuthority();
        retorno = retorno.replaceAll("(?i)=\\s*\"//", "=\"https://");
        retorno = retorno.replaceAll("(?i)=\\s*'//", "='https://");
        retorno = retorno.replaceAll("(?i)=\\s*\"/", "=\"https://" + auth_host + "/");
        retorno = retorno.replaceAll("(?i)=\\s*'/", "='https://" + auth_host + "/");
        return retorno;
    }
}

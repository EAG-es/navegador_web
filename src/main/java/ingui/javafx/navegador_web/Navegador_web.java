/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingui.javafx.navegador_web;

import static java.lang.System.err;
import java.net.URI;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ingui.javafx.webtec.Webview_simpleController;
import ingui.javafx.webtec.Webview_simpleController.I_Webview_simpleController_capturas;
import innui.modelos.configuraciones.ResourceBundles;
import innui.modelos.configuraciones.iniciales;
import innui.modelos.errores.oks;
import innui.modelos.internacionalizacion.tr;
import innui.modelos.modelos;
import innui.modelos.modelos_comunicaciones.modelos_comunicaciones;
import java.io.InputStream;
import static java.lang.System.exit;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 *
 * @author profesor
 */
public class Navegador_web extends Application {
    public static String k_in_ruta = "in/ingui/javafx/navegador_web/in";  //NOI18N
    public static String k_icono_ruta = "/re/ingui.javafx.navegador_web.icono.jpg";
    public static String k_fxml_contenedor_principal = "/ingui/javafx/navegador_web/contenedor_principal.fxml";
    public static String k_fxml_webview_simple = "/ingui/javafx/webtec/webview_simple.fxml";
    public ResourceBundle in = null;
    public Contenedor_principalController contenedor_principalController;
    public Webview_simpleController_implementaciones webview_simpleController_implementacion;
    public I_Webview_simpleController_capturas i_webview_simpleController_captura;
    public Webview_simpleController webview_simpleController;
    public iniciales navegador_web_inicial = new iniciales () {
        @Override
        public boolean run(oks ok, Object... extra_array) throws Exception {
            try {
                if (ok.es == false) { return ok.es; }
                iniciar(ok);
                if (ok.es) { 
                    launch((String []) extra_array[0]);
                    terminar(ok);
                }
                return ok.es;
            } catch (Exception e) {
                throw e;
            }
        }

        @Override
        public boolean iniciar(oks ok, Object... extra_array) throws Exception {
            if (ok.es == false) { return ok.es; }
            _iniciar_desde_clase(modelos.class, ok);
            if (ok.es == false) { return ok.es; }
            _iniciar_desde_clase(modelos_comunicaciones.class, ok);
            if (ok.es == false) { return ok.es; }
            _iniciar_desde_clase(this.getClass(), ok);
            if (ok.es == false) { return ok.es; }
            return ok.es;
        }

        @Override
        public boolean terminar(oks ok, Object... extra_array) throws Exception {
            if (ok.es == false) { return ok.es; }
            _terminar_desde_clase(modelos.class, ok);
            if (ok.es == false) { return ok.es; }
            _terminar_desde_clase(modelos_comunicaciones.class, ok);
            if (ok.es == false) { return ok.es; }
            _terminar_desde_clase(this.getClass(), ok);
            if (ok.es == false) { return ok.es; }
            return ok.es;
        }       
    };
    
    public Navegador_web() throws Exception {
        oks ok = new oks();
        in = ResourceBundles.getBundle("in/clientes_web_filtrador/ingui/javafx/in");
        iniciar_atributos(ok);
    }
    /**
     * 
     * @param ok
     * @param extras_array
     * @return true si tiene éxito, false si hay error.
     */
    public boolean iniciar_atributos(oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        i_webview_simpleController_captura = new I_Webview_simpleController_capturas() {
            @Override
            public boolean poner_error(String mensaje, oks ok, Object ... extras_array) {
                return Navegador_web.this.poner_error(mensaje, ok);
            }
            @Override
            public boolean procesar_estado(String estado, String url_texto, oks ok, Object ... extras_array) throws Exception {
                if (ok.es == false) { return ok.es; }
                String mensaje = "";
                if (webview_simpleController.presentar_contenido_seguro_intentos_num == 2) {
                    mensaje = tr.in(in, "¡ORIGEN NO CERTIFICADO! ");
                }
                if (estado.equals("SUCCEEDED")) {
                    poner_error(
                            mensaje
                            + tr.in(in, "TERMINADO. ")
                            + url_texto, ok);
                } else if (estado.equals("FAILED")) {
                    poner_error(
                            mensaje
                            + tr.in(in, "FALLIDO. ")
                            + url_texto, ok);
                } else if (estado.equals("CANCELLED")) {
                    poner_error(
                            mensaje
                            + tr.in(in, "CANCELADO. ")
                            + url_texto, ok);
                } else {
                    poner_error(
                            mensaje
                            + tr.in(in, "PROCESANDO. ")
                            + url_texto, ok);
                }
                return ok.es;
            }            
        };
        webview_simpleController_implementacion = new Webview_simpleController_implementaciones() {
            @Override
            public boolean presentar_contenido(oks ok, Object ... extras_array) throws Exception {
                return webview_simpleController.presentar_contenido(ok);
            }
            @Override
            public boolean presentar_contenido(URI uri, oks ok, Object ... extras_array) throws Exception {
                return webview_simpleController.presentar_contenido(uri, ok);
            }
        };
        return ok.es;
    }   
    @Override
    public void start(Stage stage) throws Exception {
        oks ok = new oks();
        while (true) {
            Locale locale = Locale.getDefault();
            ResourceBundle resourceBundle = ResourceBundles.getBundle(k_in_ruta, locale);
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(k_fxml_contenedor_principal), resourceBundle); //NOI18N
            Parent root = fxmlloader.load();
            // Acceder al controller:
            contenedor_principalController = fxmlloader.<Contenedor_principalController>getController();
            contenedor_principalController.webview_simpleController_implementacion = webview_simpleController_implementacion;
            contenedor_principalController.poner_panel(1, k_fxml_webview_simple, resourceBundle, ok); //NOI18N
            if (ok.es == false) { break; }
            poner_icono(stage, ok);
            if (ok.es == false) { break; }
            stage.setTitle(tr.in(in, "TITULO"));
            stage.setScene(new Scene(root));
            stage.show();
            FXMLLoader fxmlloader_1 = contenedor_principalController.fxmlLoader_1; 
            webview_simpleController = fxmlloader_1.<Webview_simpleController>getController();
            webview_simpleController.agregar_objeto_de_captura(i_webview_simpleController_captura, ok);
            if (ok.es == false) { break; }
            webview_simpleController.agregar_objeto_de_extension(webview_simpleController_implementacion, ok);
            if (ok.es == false) { break; }
            webview_simpleController_implementacion._cargar_formulario(ok);
            break;
        }
        if (ok.es == false) {
            err.println(ok.txt);
        }
    }
    /**
     * Pone el icono de la aplicación
     * @param stage Escenario donde poner el icono
     * @param ok
     * @param extra_array
     * @return true si tiene éxito, false si hay error.
     */
    public boolean poner_icono(Stage stage, oks ok, Object... extra_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        ObservableList<Image> observableList = stage.getIcons();
        InputStream inputStream = this.getClass().getResourceAsStream(
            k_icono_ruta); 
        Image image = new Image(inputStream);
        observableList.add(image);
        return ok.es;
    }

    public static void main(String[] args) {
        oks ok = new oks();
        Navegador_web navegador_web = null;
        try {
            navegador_web = new Navegador_web();
            Object [] objects_array = { args };
            navegador_web.navegador_web_inicial.run(ok, objects_array);
        } catch (Exception e) {
            ok.setTxt(e);
        }
        if (ok.es == false) {
            System.err.println(ok.txt);
            exit(1);
        } else {
            exit(0);
        }
    }
    
    public boolean poner_error(String mensaje, oks ok, Object ... extras_array)  {
        boolean ret = true;
        if (contenedor_principalController != null) {
            ret = contenedor_principalController.poner_error(mensaje, ok);
        }
        return ret;
    }
}

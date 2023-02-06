/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingui.javafx.navegador_web;

import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import ingui.javafx.webtec.Webview_simpleController.I_Webview_simpleController_extensiones;
import innui.bases;
import innui.modelos.configuraciones.ResourceBundles;
import innui.modelos.errores.oks;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author emilio
 */
public abstract class Webview_simpleController_implementaciones 
        extends bases 
        implements I_Webview_simpleController_extensiones {
    public static String k_in_ruta = "in/ingui/javafx/navegador_web/in";
    public ResourceBundle in = null; 
    AtomicReference<String> contenido_web_atref = new AtomicReference<>();
    
    public Webview_simpleController_implementaciones() throws Exception {
        in = ResourceBundles.getBundle(k_in_ruta);
    }
    /**
     * Procesa las peticiones URL que recibe el Webview_simpleController
     * @param url
     * @return true si todo es correcto, false si hay error.
     */
    @Override
    public Boolean procesar_evento_llamada_a_url(URL url, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        if (o != null) {
            if (o instanceof Webview_simpleController_implementaciones) {
                return ((Webview_simpleController_implementaciones) o).procesar_evento_llamada_a_url(url, ok, extras_array);
            }
        }
        // No hace nada, en esta aplicación. Un ejemplo sería si llamase a: _ejemplo_procesamiento_de_clic_en_url_del_navegador_web
        // _ejemplo_procesamiento_de_clic_en_url_del_navegador_web(url, ok);
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
    public boolean presentar_contenido(String texto, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        if (o != null) {
            if (o instanceof Webview_simpleController_implementaciones) {
                return ((Webview_simpleController_implementaciones) o).presentar_contenido(texto, ok, extras_array);
            }
        }
        contenido_web_atref.set(texto);
        if (ok.es) {
            Webview_simpleController_implementaciones.this.presentar_contenido(ok);
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
        if (o != null) {
            if (o instanceof Webview_simpleController_implementaciones) {
                return ((Webview_simpleController_implementaciones) o).escribir_texto(texto, ok, extras_array);
            }
        }
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
        if (o != null) {
            if (o instanceof Webview_simpleController_implementaciones) {
                return ((Webview_simpleController_implementaciones) o).leer_texto(ok, extras_array);
            }
        }
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
}

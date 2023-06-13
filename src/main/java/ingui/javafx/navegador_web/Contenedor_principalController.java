/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingui.javafx.navegador_web;

import ingui.javafx.webtec.Webview_simpleController;
import innui.bases;
import innui.modelos.configuraciones.ResourceBundles;
import innui.modelos.errores.oks;
import innui.modelos.internacionalizacion.tr;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author emilio
 */
public class Contenedor_principalController 
        extends bases
        implements Initializable {
    public static String k_in_ruta = "in/ingui/javafx/navegador_web/in";  //NOI18N

    public ResourceBundle in = null;
    @FXML
    public Button atras_boton;
    @FXML
    public Button adelante_boton;
    
    public Contenedor_principalController() throws Exception {
        in = ResourceBundles.getBundle(k_in_ruta);
    }

    @FXML
    public ScrollPane scrollpane_1;

    public FXMLLoader fxmlLoader_1;
    @FXML
    public TextField errores_textfield;
    public Webview_simpleController webview_simpleController;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errores_textfield.setMinWidth(Region.USE_PREF_SIZE);
        errores_textfield.setMaxWidth(Region.USE_PREF_SIZE);
        errores_textfield.textProperty().addListener((ov, prevText, currText) -> {
            // Do this in a Platform.runLater because of Textfield has no padding at first time and so on
            Platform.runLater(() -> {
                Text text = new Text(currText);
                text.setFont(errores_textfield.getFont()); // Set the same font, so the size is the same
                double width = text.getLayoutBounds().getWidth() // This big is the Text in the TextField
                        + errores_textfield.getPadding().getLeft() + errores_textfield.getPadding().getRight() // Add the padding of the TextField
                        + 2d; // Add some spacing
                errores_textfield.setPrefWidth(width); // Set the width
                errores_textfield.positionCaret(errores_textfield.getCaretPosition()); // If you remove this line, it flashes a little bit
            });
        });
    }    
    
    public boolean poner_panel(int numero, String archivo_fxml, ResourceBundle resourceBundle, oks ok, Object ... extras_array) throws Exception {
        if (ok.es == false) { return ok.es; }
        try {
            if (numero == 1) {
                fxmlLoader_1 = new FXMLLoader(getClass().getResource(archivo_fxml), resourceBundle);
                Node node = fxmlLoader_1.load();
                scrollpane_1.setContent(node);
            } else {
                ok.setTxt(java.text.MessageFormat.format(tr.in(in, "EL NÚMERO DEL PANEL NO EXISTE: {0}"), new Object[] {numero}));
            }
        } catch (Exception e) {
            ok.setTxt(java.text.MessageFormat.format(tr.in(in, "ERROR AL INICIALIZAR COM_PROCESOSCONTROLLER. {0}"), new Object[] {""}), e);
        }
        return ok.es;
    }

    public boolean poner_error(String mensaje, oks ok, Object ... extras_array) {
        errores_textfield.setText(mensaje);
        return ok.es;
    }

    @FXML
    private void procesar_accion_en_atras_boton(ActionEvent event) {
        oks ok = new oks();
        try {
            webview_simpleController.ir_atras_en_historial_urls(ok);
            if (ok.es) {
                poner_error(ok.txt, ok);
            }
        } catch (Exception e) {
            ok.setTxt(e);
            poner_error(ok.txt, ok);
        }
    }

    @FXML
    private void procesar_accion_en_adelante_boton(ActionEvent event) {
        oks ok = new oks();
        try {
            webview_simpleController.ir_adelante_en_historial_urls(ok);
            if (ok.es) {
                poner_error(ok.txt, ok);
            }
        } catch (Exception e) {
            ok.setTxt(e);
            poner_error(ok.txt, ok);
        }
    }
}

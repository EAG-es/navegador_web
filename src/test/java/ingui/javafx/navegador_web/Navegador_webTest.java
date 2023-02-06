/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package ingui.javafx.navegador_web;

import innui.modelos.errores.oks;
import java.util.List;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author emilio
 */
public class Navegador_webTest {
    
    public Navegador_webTest() {
    }

    /**
     * Test of iniciar_atributos method, of class Navegador_web.
     */
    @Ignore
    public void testIniciar_atributos() throws Exception {
        System.out.println("iniciar_atributos");
        oks ok = null;
        Object[] extras_array = null;
        Navegador_web instance = new Navegador_web();
        boolean expResult = false;
        boolean result = instance.iniciar_atributos(ok, extras_array);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class Navegador_web.
     */
    @Ignore
    public void testStart() throws Exception {
        System.out.println("start");
        Stage stage = null;
        Navegador_web instance = new Navegador_web();
        instance.start(stage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of navegar method, of class Navegador_web.
     */
    @Ignore
    public void testNavegar() throws Exception {
        System.out.println("navegar");
        List<String> parametros_lista = null;
        oks ok = null;
        Object[] extra_array = null;
        Navegador_web instance = new Navegador_web();
        boolean expResult = false;
        boolean result = instance.navegar(parametros_lista, ok, extra_array);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of poner_icono method, of class Navegador_web.
     */
    @Ignore
    public void testPoner_icono() throws Exception {
        System.out.println("poner_icono");
        Stage stage = null;
        oks ok = null;
        Object[] extra_array = null;
        Navegador_web instance = new Navegador_web();
        boolean expResult = false;
        boolean result = instance.poner_icono(stage, ok, extra_array);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class Navegador_web.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = {
            "-url",
            "https://coinmarketcap.com/"
        };
        Navegador_web.main(args);
    }

    /**
     * Test of poner_error method, of class Navegador_web.
     */
    @Ignore
    public void testPoner_error() throws Exception {
        System.out.println("poner_error");
        String mensaje = "";
        oks ok = null;
        Object[] extras_array = null;
        Navegador_web instance = new Navegador_web();
        boolean expResult = false;
        boolean result = instance.poner_error(mensaje, ok, extras_array);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

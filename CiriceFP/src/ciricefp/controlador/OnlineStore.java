package ciricefp.controlador;

import ciricefp.modelo.Datos;
import ciricefp.vista.MenuPrincipal;

/**
 * Esta clase implementa el main de la tienda.
 *
 * @author Cirice
 */
public class OnlineStore {
    private static Datos datos;
    private static MenuPrincipal ventana;
    private static Controlador controlador;
    public static void main(String[] args) {
        System.out.println("OnlineStore Test");
        controlador = new Controlador();
        ventana = new MenuPrincipal(controlador);
        datos = new Datos(controlador);
        controlador.setDatos(datos);
        controlador.setMenu(ventana);

        ventana.showMenu();
    }

    public static void init() {
        // TODO
    }
}

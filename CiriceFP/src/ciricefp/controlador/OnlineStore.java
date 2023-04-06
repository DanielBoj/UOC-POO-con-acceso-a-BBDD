package ciricefp.controlador;

import ciricefp.modelo.*;
import ciricefp.vista.MenuPrincipal;

/**
 * Esta clase implementa el main de la tienda.
 * OnlineStore es el punto de entrada de la aplicación. Desde aquí se inicia la aplicación.
 * Representa la plataforma interna para gestionar las ventas de una tienda online, y permite la gestión de los clientes, artículos y pedidos directamente en el
 * backup de la tienda.
 *
 * @author Cirice
 * @version 1.0
 * @since 03-2023
 */
public class OnlineStore {

    public Datos datos;
    public MenuPrincipal ventana;
    public Controlador controlador;

    public static void main(String[] args) {
        System.out.println("OnlineStore Test!");
        OnlineStore prg = new OnlineStore();

        // Instanciamos los controladores.
        prg.datos = new Datos();
        prg.ventana = new MenuPrincipal();
        prg.controlador = new Controlador(prg.datos, prg.ventana);
        prg.ventana.setControlador(prg.controlador);
        prg.datos.setControlador(prg.controlador);

        int exitValue = init(prg);

        System.exit(exitValue);
    }

    public static int init(OnlineStore prg) {

        try {

        /* TEST */
        // Realizamos una carga de datos inicial para probar la aplicación antes de implementar la capa de acceso a datos en BBDD
        // Creamos una dirección de prueba.
        Direccion dir = new Direccion("Calle Media, 5",
                "Barcelona",
                "Barcelona",
                "08001",
                "España");

        // Creamos clientes de prueba.
        prg.controlador.createCliente(new ClienteEstandard("Cirice Hélada", dir, "12345678A", "cirice@algo.com"));
        prg.controlador.createCliente(new ClienteEstandard("Sócrates Hélada", dir, "12345678B", "socrates@algo.com"));
        prg.controlador.createCliente(new ClientePremium("Platón Hélada", dir, "12345678C", "platon@algo.com"));

        // Creamos artículos de prueba.
        prg.controlador.createArticulo(new Articulo("Corbatero", 10.50, 10.0, 1));
        prg.controlador.createArticulo(new Articulo("Camiseta", 3.50, 20.0, 2));
        prg.controlador.createArticulo(new Articulo("Pantalones", 9.85, 30.0, 3));

        // Creamos pedidos de prueba.
        prg.controlador.createPedido(prg.controlador.getDatos().getClientes().get(0),
                prg.controlador.getDatos().getArticulos().get(0),
                5);
        prg.controlador.createPedido(prg.controlador.getDatos().getClientes().get(1),
                prg.controlador.getDatos().getArticulos().get(1),
                6);
        prg.controlador.createPedido(prg.controlador.getDatos().getClientes().get(2),
                prg.controlador.getDatos().getArticulos().get(2),
                3);
        // Iniciamos el menú principal.
        prg.controlador.showMenu();

        // Mensaje de despedida.
        System.out.println("Gracias por usar el software de gestión de la tienda online.");

        // Si no ha habido ningún error, devolvemos un valor de éxito.
        return 0;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            // Si ha habido algún error, devolvemos un valor de error.
            return 1;
        }
    }
}

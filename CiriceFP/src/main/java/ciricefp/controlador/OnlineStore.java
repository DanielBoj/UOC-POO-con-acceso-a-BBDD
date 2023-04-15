package ciricefp.controlador;

import ciricefp.modelo.*;
import ciricefp.modelo.interfaces.factory.IClienteFactory;
import ciricefp.modelo.utils.Conexion;
import ciricefp.vista.MenuPrincipal;

import java.sql.Connection;

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

        // Instanciamos una nueva conexión a la BD.
        OnlineStore prg = new OnlineStore();

        // Instanciamos una nueva conexión a la BD siguiendo el patrón Singleton.
        Connection db = Conexion.getInstance("dev");

        // Instanciamos los controladores.
        prg.datos = new Datos();
        prg.ventana = new MenuPrincipal();
        prg.controlador = new Controlador(prg.datos, prg.ventana);
        prg.ventana.setControlador(prg.controlador);
        prg.datos.setControlador(prg.controlador);

        int exitValue = init(prg);

        System.exit(exitValue);
    }

    // Los valores de retorno nos informarán de los errores que se han producido durante la ejecución.
    public static int init(OnlineStore prg) {

        // Creamos un valor de retorno por defecto.
        int exitValue = 0;

        // Actualizamos los datos que necesitan las funciones automatizadas de la app
        // desde la BD.
        try {
            if (prg.controlador.actualizarContadores() < 0 &&
            prg.controlador.actualizarCodigosUnicos() < 0) {
                exitValue = 4;
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar las listas de datos.");
            e.printStackTrace();
            exitValue = 4;
        }

        try {
            /* TEST CREATE*/
            System.out.println("==== TEST CREATE ====");
            // Realizamos una carga de datos inicial en la BD para probar la aplicación.
            // Creamos una dirección de prueba.
            Direccion dir = new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España");

            // Creamos clientes de prueba.
            System.out.println("==== CREACIÓN DE CLIENTES ====");
            System.out.println(prg.controlador.createCliente(IClienteFactory.createCliente("Cirice Hélada", dir, "12345678A", "cirice@algo.com", "estandard")));
            System.out.println(prg.controlador.createCliente(IClienteFactory.createCliente("Sócrates Hélada", dir, "12345678B", "socrates@algo.com", "estandard")));
            System.out.println(prg.controlador.createCliente(IClienteFactory.createCliente("Platón Hélada", dir, "12345678C", "platon@algo.com", "premium")));


            // Creamos artículos de prueba.
            System.out.println("==== CREACIÓN DE ARTÍCULOS ====");
            System.out.println(prg.controlador.createArticulo(new Articulo("Corbatero", 10.50, 10.0, 1)));
            System.out.println(prg.controlador.createArticulo(new Articulo("Camiseta", 3.50, 20.0, 2)));
            System.out.println(prg.controlador.createArticulo(new Articulo("Pantalones", 9.85, 30.0, 3)));

            // Creamos los pedidos de prueba.
            System.out.println("==== CREACIÓN DE PEDIDOS ====");
            System.out.println(prg.controlador.createPedido(prg.controlador.getClienteById(1L),
                    prg.controlador.getArticuloById(1L),
                    5));


            /* TEST READ */
            System.out.println("==== TEST READ ====");
            System.out.println("==== LISTA DE CLIENTES ====");
            System.out.println(prg.controlador.listClientes());
            System.out.println("==== CLIENTE POR ID ====");
            System.out.println(prg.controlador.getClienteById(1L));
            System.out.println("==== LISTA DE ARTÍCULOS ====");
            System.out.println(prg.controlador.listArticulos());
            System.out.println("==== ARTÍCULO POR ID ====");
            System.out.println(prg.controlador.getArticuloById(1L));
            System.out.println("==== LISTA DE PEDIDOS ====");
            System.out.println(prg.controlador.listPedidos());
            System.out.println("==== PEDIDO POR ID ====");
            System.out.println(prg.controlador.getPedidoById(1L));

            System.out.println("==== INICIO DEL PROGRAMA ====");

            // Iniciamos el menú principal.
            // Los errores de ejecución de menus se reconocerán por el valor de retorno 2.
            try {
                prg.controlador.showMenu();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                exitValue += 2;
            }

            // Mensaje de despedida.
            System.out.println("Gracias por usar el software de gestión de la tienda online.");

            /* IMPORTANTE -> Cerramos la conexión a la BD */
            // Si no ha habido ningún error, devolvemos un valor de éxito.
            exitValue = Conexion.closeConnection() != 0 ? 0 : 3;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            // Si ha habido algún error, devolvemos un valor de error.
            exitValue += 3;
        }

        return exitValue;
    }
}

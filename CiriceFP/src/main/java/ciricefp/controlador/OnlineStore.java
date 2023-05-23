package ciricefp.controlador;

import ciricefp.modelo.*;
import ciricefp.modelo.utils.ConexionJpa;
import ciricefp.vista.MenuPrincipalController;

// Librería para trabajar con archivos .env
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;

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
    public MenuPrincipalController ventana;
    public Controlador controlador;

    // Instanciamos el archivo .env
    private static final Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) {
        System.out.println("OnlineStore Test!");

        // Instanciamos una nueva conexión a la BD.
        OnlineStore prg = new OnlineStore();

        // Instanciamos una nueva conexión a la BD siguiendo el patrón Singleton.
        // Connection db = Conexion.getInstance(dotenv.get("ENV"));

        // Producto 4 -> Instanciamos el Entity Manager para poder conectarnos a la BD.
        EntityManager em = ConexionJpa.getEntityManagerFactory();

        // Instanciamos los controladores.
        prg.datos = new Datos();
        prg.ventana = new MenuPrincipalController();
        prg.controlador = new Controlador(prg.datos, prg.ventana);
        prg.ventana.setControlador(prg.controlador);
        prg.datos.setControlador(prg.controlador);
        prg.datos.setEm(em);

        int exitValue = init(prg, args);

        /* IMPORTANTE -> Cerramos la conexión a la BD */
        prg.datos.getEm().close();

        System.exit(exitValue);
    }

    // Los valores de retorno nos informarán de los errores que se han producido durante la ejecución.
    public static int init(OnlineStore prg, String[] args) {

        // Creamos un valor de retorno por defecto.
        int exitValue = 0;

        // Actualizamos los datos que necesitan las funciones automatizadas de la app
        // desde la BD.
        try {
            prg.controlador.actualizarCodigosUnicos();
            prg.controlador.actualizarContadores();
        } catch (Exception e) {
            System.out.println("Error al actualizar las listas de datos.");
            e.printStackTrace();
            exitValue = 2;
        }

        try {

            System.out.println("==== INICIO DEL PROGRAMA ====");

            // Iniciamos el menú principal.
            // Los errores de ejecución de menus se reconocerán por el valor de retorno 2.
            try {
                //prg.controlador.showMenu(args);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                exitValue = exitValue * 10 + 3;
            }

            // Mensaje de despedida.
            System.out.println("Gracias por usar el software de gestión de la tienda online.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            // Si ha habido algún error, devolvemos un valor de error.
            exitValue = exitValue * 10 + 4;
        }

        return exitValue;
    }
}

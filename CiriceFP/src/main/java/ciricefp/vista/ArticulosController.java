package ciricefp.vista;

import ciricefp.modelo.Articulo;
import ciricefp.vista.interfaces.IVista;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Esta clase implementa la lógica de negocio de la vista de artículos.
 * Delega en el controlador interno MenuPrincipalController la gestión de las opciones del menú.
 *
 * @author Cirice
 */
public class ArticulosController {

    // Atributos de la clase.
    private MenuPrincipalController menu;
    public ArticulosController() {
        this.menu = new MenuPrincipalController();
    }

    public ArticulosController(MenuPrincipalController menu) {
        this.menu = menu;
    }

    /* Getters & Setters */

    public MenuPrincipalController getMenu() {
        return menu;
    }

    public void setMenu(MenuPrincipalController menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "ArticulosController{" +
                "menu=" + menu +
                '}';
    }

    /* Métodos de la clase */

    // Iniciador de la vista: Genera el submenú de Artículos.
   /* public void inicio() {

        boolean salir = false;
        char opt;

        do {
            // Mostramos el submenú para las funcionalidades de artículo
            System.out.println("Gestión de artículos");
            System.out.println("====================");
            System.out.println("1. Añadir artículo");
            System.out.println("2. Mostrar artículos");
            System.out.println("3. Borrar todos los artículos");
            System.out.println("0. Volver al menú principal");

            // Pedimos la opción al usuario.
            opt = menu.pedirOpcion(4);
            switch (opt) {
                *//*case '1' -> addArticulo();
                case '2' -> listArticulos();
                case '3' -> clearArticulos();*//*
                case '0' -> salir = true;
                // Manejamos el caso de opción incorrecta.
                default -> System.out.println("Opción incorrecta");
            }
        } while (!salir);

        // Mensaje de salida.
        System.out.println("Saliendo de la gestión de artículos...");
    }*/

    // Implementamos los métodos necesarios para la gestión de artículos llamando al controlador.
    // Añadir un nuevo artículo
    public boolean addArticulo(String descripcion, double precio, double gastosEnvio, int tiempoPreparacion) {
           /* Producto 5 --> Implementación de la interfaz gráfica */
        // Creamos el artículo
        if (descripcion != null && precio > 0 && gastosEnvio > 0 && tiempoPreparacion > 0) {
            // Llamamos al controlador para añadir el artículo
            try{
                return menu.createArticulo(descripcion, precio, gastosEnvio, tiempoPreparacion);
            } catch (Exception e) {
                System.out.println("Error al crear el artículo: " + e.getMessage());
            }
        }
        // Si no es posible crear el artículo devolvemos false
        return false;
    }

    // Mostrar todos los artículos
    public Optional<ArrayList<Articulo>> listArticulos() {
        // Producto 5 -->
        try {
            return menu.listArticulos();
        } catch (Exception e) {
            System.out.println("Error al listar los artículos: " + e.getMessage());
        }
        // Si no es posible listar los artículos devolvemos un Optional vacío
        return Optional.empty();
    }

    // Borrar todos los artículos
    public boolean clearArticulos() {
        // Producto 5 --> Adaptamos el método para trabajar con un GUI
        try {
            menu.clearArticulos();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Si no es posible borrar los artículos devolvemos false
        return false;
    }

}

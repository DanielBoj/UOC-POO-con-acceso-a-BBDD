package ciricefp.vista;

import ciricefp.vista.interfaces.IVista;

/**
 * Esta clase implementa la lógica de negocio de la vista de artículos.
 * Delega en el controlador interno MenuPrincipal la gestión de las opciones del menú.
 *
 * @author Cirice
 */
public class VistaArticulos implements IVista {

    // Atributos de la clase.
    private MenuPrincipal menu;

    public VistaArticulos(MenuPrincipal menu) {
        this.menu = menu;
    }

    /* Getters & Setters */

    public MenuPrincipal getMenu() {
        return menu;
    }

    public void setMenu(MenuPrincipal menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "VistaArticulos{" +
                "menu=" + menu +
                '}';
    }

    /* Métodos de la clase */

    // Iniciador de la vista: Genera el submenú de Artículos.
    public void inicio() {

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
            opt = menu.pedirOpcion(3);
            switch (opt) {
                case '1' -> addArticulo();
                case '2' -> listArticulos();
                case '3' -> clearArticulos();
                case '0' -> salir = true;
                // Manejamos el caso de opción incorrecta.
                default -> System.out.println("Opción incorrecta");
            }
        } while (!salir);

        // Mensaje de salida.
        System.out.println("Saliendo de la gestión de artículos...");
    }

    // Implementamos los métodos necesarios para la gestión de artículos llamando al controlador.
    // Añadir un nuevo artículo
    public void addArticulo() {
        System.out.println("Añadir artículo nuevo: ");

        // Capturamos los datos necesarios para crear el artículo y los parsemos.
        String descripcion = menu.pedirDatos("Introduce la descripción del artículo: ");
        double precio = Double.parseDouble(menu.pedirDatos("Introduce el precio del artículo: "));
        double gastosEnvio = Double.parseDouble(menu.pedirDatos("Introduce los gastos de envío del artículo: "));
        int tiempoPreparacion = Integer.parseInt(menu.pedirDatos("Introduce el tiempo de preparación del artículo: "));

        // Creamos el artículo
        if (descripcion != null && precio > 0 && gastosEnvio > 0 && tiempoPreparacion > 0) {
            // Llamamos al controlador para añadir el artículo
            try{
                menu.createArticulo(descripcion, precio, gastosEnvio, tiempoPreparacion);
            } catch (Exception e) {
                System.out.println("Error al crear el artículo: " + e.getMessage());
            }
        }
    }

    // Mostrar todos los artículos
    public void listArticulos() {
        menu.listArticulos();
    }

    // Borrar todos los artículos
    public void clearArticulos() {
        menu.clearArticulos();
    }

}

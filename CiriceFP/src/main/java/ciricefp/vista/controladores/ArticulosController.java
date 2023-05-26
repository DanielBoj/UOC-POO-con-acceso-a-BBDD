package ciricefp.vista.controladores;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.interfaces.IArticulo;
import ciricefp.vista.interfaces.IArticuloController;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Esta clase implementa la lógica de negocio de la vista de artículos.
 * Delega en el controlador interno MenuPrincipalController la gestión de las opciones del menú.
 *
 * @author Cirice
 */
public class ArticulosController implements IArticuloController {

    // Atributos de la clase.
    private MenuPrincipalController menu;
    // Producto 5 --> Constructor para GUI
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
    // Producto 5 --> Implementamos el controlador para trabajar con JavaFX
    // Implementamos los métodos necesarios para la gestión de artículos llamando al controlador.
    // Añadir un nuevo artículo
    @Override
    public boolean addArticulo(String descripcion, double precio, double gastosEnvio, int tiempoPreparacion) {
           /* Producto 5 --> Implementación de la interfaz gráfica */
        // Creamos el artículo
        if (descripcion != null && precio > 0 && gastosEnvio > 0 && tiempoPreparacion > 0) {
            // Llamamos al controlador para añadir el artículo
            try{
                return menu.createArticulo(descripcion, precio, gastosEnvio, tiempoPreparacion);
            } catch (Exception e) {
                System.out.println("Error al crear el artículo: ");
                e.printStackTrace();
            }
        }
        // Si no es posible crear el artículo devolvemos false
        return false;
    }

    // Mostrar todos los artículos
    @Override
    public Optional<ArrayList<Articulo>> listArticulos() {
        // Producto 5 -->
        try {
            return menu.listArticulos();
        } catch (Exception e) {
            System.out.println("Error al listar los artículos: ");
            e.printStackTrace();
        }
        // Si no es posible listar los artículos devolvemos un Optional vacío
        return Optional.empty();
    }

    // Borrar todos los artículos
    @Override
    public Optional<Integer> clearArticulos() {
        // Producto 5 --> Adaptamos el método para trabajar con un GUI
        try {
            return Optional.of(menu.clearArticulos());
        } catch (Exception e) {
            System.out.println("Error al borrar los artículos: ");
            e.printStackTrace();
        }
        // Si no es posible borrar los artículos devolvemos empty
        return Optional.empty();
    }

}

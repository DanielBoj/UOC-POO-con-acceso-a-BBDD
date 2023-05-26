package ciricefp.vista.controladores;

import ciricefp.modelo.Cliente;
import ciricefp.vista.interfaces.IClienteController;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

/**
 * Esta clase implementa la lógica de negocio de la vista de clientes.
 * Delega en la clase MenuPrincipalController la gestión de las opciones del menú.
 *
 * @author Cirice
 */
public class ClientesController implements IClienteController {

    // Atributos de la clase.
    private MenuPrincipalController menu;

    // Producto 5 --> Constructor para GUI
    public ClientesController() {
        this.menu = new MenuPrincipalController();
    }

    // Constructor por defecto.
    public ClientesController(MenuPrincipalController menu) {
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
        return "ClientesController{" +
                ", menu=" + menu +
                '}';
    }

    /* Métodos de la clase */
    // Producto 5 --> Implementamos el controlador para trabajar con JavaFX

    // Implementamos los métodos necesarios para la gestión de artículos llamando al controlador.
    // Creamos un nuevo cliente
    @Override
    public Optional<Cliente> addCliente(String nombre, String nif, String email, String domicilio, String poblacion,
                           String provincia, String cp, String pais, String tipo) {
        // Producto 5 --> Creamos el cliente mediante GUI
        try {
            return menu.createCliente(nombre, domicilio, poblacion, provincia, cp, pais, nif, email, tipo);
        } catch (Exception e) {
            System.out.println("Error al crear el cliente");
            e.printStackTrace();
        }
        // Si ha habido un error, devolvemos false
        return Optional.empty();
    }

    // Mostrar por pantalla una lista de los clientes
    @Override
    public Optional<ArrayList<Cliente>> listClientes() {
        // Producto 5 --> Mostramos los clientes mediante GUI
        try {
            return menu.listClientes();
        } catch (Exception e) {
            System.out.println("Error al listar los clientes");
            e.printStackTrace();
        }
        // Si no es posible listar los artículos devolvemos un Optional vacío
        return Optional.empty();
    }

    // Buscamos un cliente por su nif y lo presentramos por pantalla
    @Override
    public Optional<Cliente> buscarCliente(String nif) {
        // Producto 5 --> Adaptamos el método al GUI
        try {
            return menu.searchCliente(nif);
        } catch (Exception e) {
            System.out.println("Error al buscar el cliente");
            e.printStackTrace();
        }
        // Si no es posible buscar el cliente devolvemos un Optional vacío
        return Optional.empty();
    }

    // obtenemos una lista de los nifs de los clientes
    @Override
    public Optional<ArrayList<String>> listClientesNif() {
        // Producto 5 --> Mostramos los clientes mediante GUI
        try {
            return menu.listClientesNif();
        } catch (Exception e) {
            System.out.println("Error al listar los clientes");
            e.printStackTrace();
        }
        // Si no es posible listar los artículos devolvemos un Optional vacío
        return Optional.empty();
    }

    // Filtramos los clientes por tipo
    @Override
    public Optional<ArrayList<Cliente>> filtrarClientes(String tipo) {
        // Producto 5 --> Mostramos los clientes mediante GUI
        try {
            return menu.filterClientesByType(tipo);
        } catch (Exception e) {
            System.out.println("Error al listar los clientes");
            e.printStackTrace();
        }
        // Si no es posible listar los artículos devolvemos un Optional vacío
        return Optional.empty();
    }

    // Método para solicitar el tipo de cliente, solo accesible desde la clase.
    // Producto 5 --> Eliminamos el método, ya que no es necesario
}

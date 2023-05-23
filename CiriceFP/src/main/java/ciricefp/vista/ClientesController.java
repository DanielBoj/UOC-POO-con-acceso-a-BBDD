package ciricefp.vista;

import ciricefp.modelo.Cliente;
import ciricefp.vista.interfaces.IVista;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

/**
 * Esta clase implementa la lógica de negocio de la vista de clientes.
 * Delega en la clase MenuPrincipalController la gestión de las opciones del menú.
 *
 * @author Cirice
 */
public class ClientesController {

    // Atributos de la clase.

    // Buffer de entrada por teclado.
    Scanner teclado = new Scanner(System.in);

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

    // Implementamos los métodos necesarios para la gestión de artículos llamando al controlador.
    // Creamos un nuevo cliente
    public boolean addCliente(String nombre, String nif, String email, String domicilio, String poblacion,
                           String provincia, String cp, String pais, String tipo) {
        // Producto 5 --> Creamos el cliente mediante GUI
        try {
            return menu.createCliente(nombre, domicilio, poblacion, provincia, cp, pais, nif, email, tipo);
        } catch (Exception e) {
            System.out.println("Error al crear el cliente");
        }
        // Si ha habido un error, devolvemos false
        return false;
    }

    // Mostrar por pantalla una lista de los clientes
    public Optional<ArrayList<Cliente>> listClientes() {
        /*System.out.println("Listado de clientes: ");
        System.out.println("====================");*/
        // Producto 5 --> Mostramos los clientes mediante GUI
        return menu.listClientes();
    }

    // Buscamos un cliente por su nif y lo presentramos por pantalla
    public void buscarCliente() {
        System.out.println("Buscar cliente por NIF: ");
        System.out.println("========================");
        String nif = menu.pedirDatos("NIF cliente: ");
        menu.searchCliente(nif);
    }

    public void filtrarClientes() {
        System.out.println("Filtrar clientes por tipo: ");
        System.out.println("==========================");
        String tipo = pedirTipoCliente();

        ArrayList<Cliente> clientesTemp = menu.filterClientesByType(tipo);

        if (clientesTemp.isEmpty()) {
            System.out.println("No hay clientes de ese tipo");
        } else {
            System.out.println("Listado de clientes: ");
            System.out.println("====================");
            for (Cliente cliente : clientesTemp) {
                System.out.println(cliente);
                System.out.println("==================================");
                System.out.println("Pulsa una tecla para mostrar el siguiente cliente.");
                System.out.println("Pulsa ENTER para continuar");
                // Esperamos una pulsación de tecla para mostrar el siguiente artículo.
                teclado.nextLine();
            }
        }
    }

    // Método para solicitar el tipo de cliente, solo accesible desde la clase.
    public String pedirTipoCliente() {

        String tipo = "";

        do {
            System.out.println("Tipo de cliente: ");
            System.out.println("0. Estandard");
            System.out.println("1. Premium");
            char opt = menu.pedirOpcion(2);
            switch (opt) {
                case '0' -> tipo = "Estandard";
                case '1' -> tipo = "Premium";
                // Manejamos el caso de opción incorrecta.
                default -> System.out.println("Opción incorrecta");
            }
        } while (tipo.equals(""));

        return tipo;
    }
}

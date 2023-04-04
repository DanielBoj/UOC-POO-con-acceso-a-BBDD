package ciricefp.vista;

import ciricefp.modelo.Cliente;
import ciricefp.vista.interfaces.IVista;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Esta clase implementa la lógica de negocio de la vista de clientes.
 * Delega en la clase MenuPrincipal la gestión de las opciones del menú.
 *
 * @author Cirice
 */
public class VistaClientes implements IVista {

    // Atributos de la clase.

    // Buffer de entrada por teclado.
    Scanner teclado = new Scanner(System.in);

    private MenuPrincipal menu;

    // Constructor por defecto.
    public VistaClientes(MenuPrincipal menu) {
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
        return "VistaClientes{" +
                ", menu=" + menu +
                '}';
    }

    /* Métodos de la clase */

    // Iniciador de la vista: Genera el submenú de Clientes.
    public void inicio() {

        boolean salir = false;
        char opt;

        do {
            // Mostramos el submenú para las funcionalidades de artículo
            System.out.println("Gestión de clientes");
            System.out.println("====================");
            System.out.println("1. Añadir cliente");
            System.out.println("2. Mostrar clientes");
            System.out.println("3. Buscar cliente por NIF");
            System.out.println("4. Filtrar clientes por tipo");
            System.out.println("0. Volver al menú principal");

            // Pedimos la opción al usuario.
            opt = menu.pedirOpcion(5);
            switch (opt) {
                case '1' -> addCliente();
                case '2' -> listClientes();
                case '3' -> buscarCliente();
                case '4' -> filtrarClientes();
                case '0' -> salir = true;
                // Manejamos el caso de opción incorrecta.
                default -> System.out.println("Opción incorrecta");
            }
        } while (!salir);

        // Mensaje de salida.
        System.out.println("Saliendo de la gestión de clientes...");
    }

    // Implementamos los métodos necesarios para la gestión de artículos llamando al controlador.
    // Creamos un nuevo cliente
    public void addCliente() {

        System.out.println("Añadir cliente nuevo: ");
        System.out.println("=====================");

        // Pedimos los datos del cliente
        System.out.println("Por favor, indica el tipo de cliente");
        String tipo = "";
        tipo = pedirTipoCliente();

        // Solicitamos los datos del cliente
        String nombre = menu.pedirDatos("Nombre: ");
        String nif = menu.pedirDatos("NIF: ");
        String email = menu.pedirDatos("Email: ");
        System.out.println("Dirección: ");
        String domicilio = menu.pedirDatos("Domicilio: ");
        String poblacion = menu.pedirDatos("Población: ");
        String provincia = menu.pedirDatos("Provincia: ");
        String cp = menu.pedirDatos("Código postal: ");
        String pais = menu.pedirDatos("País: ");

        // Creamos el cliente
        try {
            menu.createCliente(nombre, domicilio, poblacion, provincia, cp, pais, nif, email, tipo);
        } catch (Exception e) {
            System.out.println("Error al crear el cliente");
        }
    }

    // Mostrar por pantalla una lista de los clientes
    public void listClientes() {
        System.out.println("Listado de clientes: ");
        System.out.println("====================");
        menu.listClientes();
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
                try {
                    char wait = teclado.nextLine().charAt(0);
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
    }

    // Método para solicitar el tipo de cliente, solo accesible desde la clase.
    public String pedirTipoCliente() {

        String tipo = "";

        System.out.println("Tipo de cliente: ");
        System.out.println("1. Estandard");
        System.out.println("2. Premium");
        char opt = menu.pedirOpcion(2);
        switch (opt) {
            case '1' -> tipo = "Estandard";
            case '2' -> tipo = "Premium";
            // Manejamos el caso de opción incorrecta.
            default -> System.out.println("Opción incorrecta");
        }

        return tipo;
    }
}

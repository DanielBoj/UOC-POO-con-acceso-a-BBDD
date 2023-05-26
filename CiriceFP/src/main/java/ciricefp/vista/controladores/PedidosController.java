package ciricefp.vista.controladores;

import ciricefp.modelo.Pedido;
import ciricefp.vista.controladores.MenuPrincipalController;
import ciricefp.vista.interfaces.IPedidoController;
import ciricefp.vista.interfaces.IVista;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Esta clase implementa la lógica de negocio de la vista de pedidos.
 * Delega en el controlador interno MenuPrincipalController la gestión de las opciones del menú.
 *
 * @author Cirice
 */
public class PedidosController implements IPedidoController {
    // Atributos de la clase.
    private MenuPrincipalController menu;

    // Constructor por defecto.
    public PedidosController(MenuPrincipalController menu) {
        this.menu = menu;
    }

    // Constructor vacío
    public PedidosController() {
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
        return "PedidosController{" +
                "menu=" + menu +
                '}';
    }

    /* Métodos de la clase */
    // Producto 5 --> Adapta el método de la vista para usar JavaFX

    // Añadimos un pedido nuevo.
    @Override
    public Optional<Pedido> addPedido(String nif, String codArticulo, int cantidad) {
        // Producto 5 -> Adaptar el método para usar JavaFX
        try {
            return menu.createPedido(nif, codArticulo, cantidad);
        } catch (Exception e) {
            System.out.println("Error al crear el pedido");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /* Métodos auxiliares para la creación de un pedido */
    // Producto 5 -> Manejamos mediante JavaFX, ya no son necesarios los métodos auxiliares.

    /*// Añadimos un pedido nuevo a un cliente existente.
    public Optional<Pedido> addPedidoExistente(String nif, String codArticulo, int cantidad) {
        // Producto 5 -> Adaptar el método para usar JavaFX
        try {
            return menu.createPedido(nif, codArticulo, cantidad);
        } catch (Exception e) {
            System.out.println("Error al crear el pedido");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Añadimos un pedido nuevo a un cliente nuevo.
    public void addPedidoNuevo(String nif, String codArticulo, int cantidad) {

        *//*System.out.println("Añadir pedido nuevo a un cliente nuevo: ");

        // Capturamos los datos del cliente
        // Pedimos los datos del cliente
        System.out.println("Por favor, indica el tipo de cliente");
        String tipo = "";
        tipo = menu.getVistaClientes().pedirTipoCliente();

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

        // Solicitamos los datos del artículo
        System.out.println("Introduce el código del artículo: ");
        String codArticulo;

        // Comprobamos que el artículo existe, repetimos la operación mientras no exista o no se escoja salir.
        do {
            codArticulo = menu.pedirDatos("Código del artículo: ");

            if (codArticulo.equals("0")) {
                return;
            }

            if (menu.searchArticulo(codArticulo) == null) {
                System.out.println("El artículo no existe. Introduce un código válido o pulsa 0 para salir.");
            }
        } while (menu.searchArticulo(codArticulo) == null);

        // Introducimos el número de unidades.
        System.out.println("Introduce la cantidad de unidades: ");
        int cantidad = Integer.parseInt(menu.pedirDatos("Cantidad de unidades[Entero]: "));

        // Llamamos al método del controlador
        try {
            menu.createPedido(nif, codArticulo, cantidad);
        } catch (Exception e) {
            System.out.println("Error al crear el pedido");
        }*//*
    }*/

    // Mostramos todos los pedidos.
    @Override
    public Optional<ArrayList<Pedido>> listPedidos() {
        // Producto 5 --> Adapta el método de la vista para usar JavaFX
        try {
            return menu.listPedidos();
        } catch (Exception e) {
            System.out.println("Error al listar los pedidos");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Actualizamos el estado de los pedidos enviados.
    @Override
    public Optional<Integer> updatePedidos() {
        try {
            return menu.actualizarEstadoPedidos();
        } catch (Exception e) {
            System.out.println("Error al actualizar los pedidos");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Mostramos los pedidos pendientes de envío.
    @Override
    public Optional<ArrayList<Pedido>> listPedidosPendientes() {
        try {
            return menu.filterPedidosByEstado("pendiente");
        } catch (Exception e) {
            System.out.println("Error al listar los pedidos pendientes");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Iniciamos el submenú para eliminar pedidos
    @Override
    public Optional<Boolean> deletePedido(Pedido actualPedido) {
        try {
            return menu.deletePedido(actualPedido);
        } catch (Exception e) {
            System.out.println("Error al eliminar el pedido");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Mostramos los pedidos por cliente.
    @Override
    public Optional<ArrayList<Pedido>> listPedidosCliente(String nif) {
        try {
            return menu.filterPedidosByCliente(nif);
        } catch (Exception e) {
            System.out.println("Error al listar los pedidos por cliente");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Mostramos una lista de pedidos según su estado: Pendiente o Enviado.
    public void listPedidosEstado() {
        /*// Preguntamos al usuario qué estado quiere usar como filtro.
        System.out.println("Introduce el estado del pedido:");
        System.out.println("1. Pendiente");
        System.out.println("2. Enviado");
        char opt = menu.pedirOpcion(2);

        switch (opt) {
            case '1' -> menu.filterPedidosByEstado("pendiente");
            case '2' -> menu.filterPedidosByEstado("enviado");
        }*/
    }
}

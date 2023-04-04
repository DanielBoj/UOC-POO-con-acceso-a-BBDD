package ciricefp.vista;

import ciricefp.vista.interfaces.IVista;

/**
 * Esta clase implementa la lógica de negocio de la vista de pedidos.
 * Delega en el controlador interno MenuPrincipal la gestión de las opciones del menú.
 *
 * @author Cirice
 */
public class VistaPedidos implements IVista {

    // Atributos de la clase.
    private MenuPrincipal menu;

    // Constructor por defecto.
    public VistaPedidos(MenuPrincipal menu) {
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
        return "VistaPedidos{" +
                "menu=" + menu +
                '}';
    }

    /* Métodos de la clase */

    // Iniciador de la vista: Genera el submenú de Pedidos.
    public void inicio() {

        boolean salir = false;
        char opt;

        do {
            // Mostramos el submenú para las funcionalidades de artículo
            System.out.println("Gestión de pedidos");
            System.out.println("====================");
            System.out.println("1. Añadir pedido");
            System.out.println("2. Mostrar pedidos");
            System.out.println("3. Actualizar estado de los pedidos enviados");
            System.out.println("4. Mostrar pedidos pendientes de envío");
            System.out.println("5. Eliminar pedidos");
            System.out.println("5. Mostrar pedidos por cliente");
            System.out.println("0. Volver al menú principal");

            // Pedimos la opción al usuario.
            opt = menu.pedirOpcion(6);
            switch (opt) {
                case '1' -> addPedido();
                case '2' -> listPedidos();
                case '3' -> updatePedidos();
                case '4' -> listPedidosPendientes();
                case '5' -> deletePedidos();
                case '6' -> listPedidosCliente();
                case '0' -> salir = true;
                // Manejamos el caso de opción incorrecta.
                default -> System.out.println("Opción incorrecta");
            }
        } while (!salir);

        // Mensaje de salida.
        System.out.println("Saliendo de la gestión de pedidos...");
    }

    // Añadimos un pedido nuevo.
    public void addPedido() {
        System.out.println("Añadir pedido nuevo: ");
        // Solicitamos los datos del pedido.
        String nif = menu.pedirDatos("NIF del cliente: ");
        String codArticulo = menu.pedirDatos("Código del artículo: ");
        int cantidad = Integer.parseInt(menu.pedirDatos("Cantidad de unidades[Entero]: "));

        // Llamamos al método del controlador
        menu.createPedido(nif, codArticulo, cantidad);
    }

    // Mostramos todos los pedidos.
    public void listPedidos() {
        menu.listPedidos();
    }

    // Actualizamos el estado de los pedidos enviados.
    public void updatePedidos() {
        menu.actualizarEstadoPedidos();
    }

    // Mostramos los pedidos pendientes de envío.
    public void listPedidosPendientes() {
        System.out.println("Mostrar pedidos pendientes de envío: ");
    }

    // Iniciamos el submenú para eliminar pedidos
    public void deletePedidos() {
        menu.deletePedido();
    }

    // Mostramos los pedidos por cliente.
    public void listPedidosCliente() {
        System.out.println("Mostrar pedidos por cliente: ");
        System.out.println("Introduce el NIF del cliente: ");
        String nif = menu.pedirDatos("NIF del cliente: ");

        menu.filterPedidosByCliente(nif);
    }

    // Mostramos una lista de pedidos según su estado: Pendiente o Enviado.
    public void listPedidosEstado() {
        // Preguntamos al usuario qué estado quiere usar como filtro.
        System.out.println("Introduce el estado del pedido:");
        System.out.println("1. Pendiente");
        System.out.println("2. Enviado");
        char opt = menu.pedirOpcion(2);

        switch (opt) {
            case '1' -> menu.filterPedidosByEstado("pendiente");
            case '2' -> menu.filterPedidosByEstado("enviado");
        }
    }
}

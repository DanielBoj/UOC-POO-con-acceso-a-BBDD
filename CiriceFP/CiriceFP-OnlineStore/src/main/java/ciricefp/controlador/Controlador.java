package ciricefp.controlador;

import ciricefp.modelo.*;
import ciricefp.vista.controladores.MenuPrincipalController;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Esta clase implementa el controlador principal del modelo MVC.
 * Se encarga de gestionar los datos y las acciones del usuario y actualizar la vista.
 * Ejerce de puente entre los módulos vita y modelo.
 * Para mantener la conexión, recibe como parámetros el modelo y la vista.
 *
 * @author Cirice
 */
public class Controlador {

    private Datos datos;
    private MenuPrincipalController menu;

    // Constructor por defecto sin parámetros.
    public Controlador() {
        this.datos = new Datos();
        this.menu = new MenuPrincipalController();
    }

    // Constructor con parámetros.
    public Controlador(Datos datos, MenuPrincipalController menu) {
        this.datos = datos;
        this.menu = menu;
    }

    /* Getters & Setters */
    public Datos getDatos() {
        return datos;
    }

    public void setDatos(Datos datos) {
        this.datos = datos;
    }

    public MenuPrincipalController getMenu() {
        return menu;
    }

    public void setMenu(MenuPrincipalController menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Controlador{" +
                "datos=" + datos +
                ", menu=" + menu +
                '}';
    }

    /*// TODO --> updateMenu()
    public void showMenu(String[] args) {
        OnlineStoreApplication.main(args);
    }*/

    // Implementamos los métodos para las acciones CRUD que hemos creado en la clase Datos para que sean accesibles desde el controlador.
    // Estos métodos se encargarán de llamar a los métodos de la clase Datos y actualizar la vista.
    // Es decir, todos los métodos del Controlador delegan en los métodos de la clase Datos, el controlador interno del módulo Modelo.

    /* Articulos */
    // Creamos un artículo nuevo y lo añadimos a la lista de artículos.
    public Articulo createArticulo(String descripcion,
                                   double pvp,
                                   double gastosEnvio,
                                   int tiempoPreparacion) {
        return datos.createArticulo(descripcion, pvp, gastosEnvio, tiempoPreparacion).orElse(null);
    }

    // Recibimos un artículo y lo añadimos a la lista de artículos.
    public Articulo createArticulo(Articulo articulo) {
        return datos.createArticulo(articulo).orElse(null);
    }

    // Como Vista no debe tener acceso a la lógica implementada en Modelo, casteamos el retorno a un ArrayList
    public ArrayList<Articulo> listArticulos() {
        return datos.listArticulos().getLista();
    }

    // Producto 3 -> Obtenemos un artículo por su id.
    public Articulo getArticuloById(@NotNull Long id) { return datos.getArticuloById(id).orElse(null); }

    // Buscamos un artículo por su código.
    public Articulo searchArticulo(@NotNull String codigo) {
        return datos.searchArticulo(codigo).orElse(null);
    }

    // TODO -> Como extra podemos crear métodos para eliminar y modificar artículos

    // Eliminamos un artículo de la lista de artículos.
    public ArrayList<Articulo> clearArticulos() {
        return datos.clearArticulos();
    }

    /* Clientes */

    // Creamos un cliente nuevo y lo añadimos a la lista de clientes.
    public Cliente createCliente(
         String nombre,
         String domicilio,
         String poblacion,
         String provincia,
         String cp,
         String pais,
         String nif,
         String email, String tipo) {

        // Generamos la dirección
        Direccion direccion = new Direccion(domicilio, poblacion, provincia, cp, pais);

        return datos.createCliente(nombre, direccion, nif, email, tipo).orElse(null);
    }

    // Recibimos un cliente y lo añadimos a la lista de clientes.
    public Cliente createCliente(@NotNull Cliente cliente) {
        return datos.createCliente(cliente).orElse(null);
    }

    // Obtenemos una copia de la lista de clientes
    public ArrayList<Cliente> listClientes() {
        return datos.listClientes().getLista();
    }

    // Producto 3 --> Obtenemos un cliente por su id.
    public Cliente getClienteById(@NotNull Long id) { return datos.getClienteById(id).orElse(null); }

    // Eliminamos un cliente
    public ArrayList<Cliente> filterClientesByType(String tipo) {
        return datos.filterClientesByType(tipo);
    }

    // Buscamos un cliente por su DNI
    public Cliente searchCliente(@NotNull String nif) { return datos.searchCliente(nif).orElse(null); }

    // Limpiamos la lista de clientes
    public ArrayList<Cliente> clearClientes() {
        return datos.clearClientes();
    }

    /* Pedidos */
    // Creamos un pedido nuevo y lo añadimos a la lista de pedidos.
    public Pedido createPedido(@NotNull Cliente cliente, @NotNull Articulo articulo, int cantidad) {
        return datos.createPedido(cliente, articulo, cantidad);
    }

    // Recibimos un pedido y lo añadimos a la lista de pedidos.
    public Pedido createPedido(@NotNull Pedido pedido) {
        return datos.createPedido(pedido);
    }

    // Obtenemos una copia de la lista de pedidos
    public ArrayList<Pedido> listPedidos() {
        return datos.listPedidos().getLista();
    }

    // Producto 3 --> Obtenemos un pedido por su id.
    public Pedido getPedidoById(@NotNull Long id) { return datos.getPedidoById(id).orElse(null); }

    // Eliminamos un pedido
    public Pedido deletePedido(@NotNull Pedido pedido) {
        return datos.deletePedido(pedido);
    }

    // Buscamos un pedido por su número de pedido
    public Pedido searchPedido(int numeroPedido) {
        return datos.searchPedido(numeroPedido).orElse(null);
    }

    // Buscamos un pedido por su cliente recibiendo el NIF del cliente
    public ArrayList<Pedido> filterPedidosByCliente(@NotNull String nif) {
        return datos.filterPedidosByCliente(nif);
    }

    // Buscamos un pedido por su cliente recibiendo la instancia del cliente
    public ArrayList<Pedido> filterPedidosByCliente(@NotNull Cliente cliente) {
        return datos.filterPedidosByCliente(cliente);
    }

    // Recibimos una lista de pedidos filtrados por su estado
    public ArrayList<Pedido> filterPedidosByEstado(String opt) {
        return datos.filterPedidosByEstado(opt);
    }

    // Recibimos una lista de pedidos enviados filtrados por su fecha
    public ArrayList<Pedido> filterPedidosEnviadosByFecha(@NotNull LocalDate fecha) {
        return datos.filterPedidosEnviadosByFecha(fecha);
    }

    // Limpiamos la lista de pedidos -> Cuidado, este método ahora mismo eliminaría pedidos enviados.
    public ArrayList<Pedido> clearPedidos() {
        /* En caso de implementar este método, manejaremos el siguiente aviso en el controlador de vistas:
        *  System.out.println("PRECAUCIÓN: Se eliminarán todos los pedidos de la lista, incluidos los enviados.");
        *  System.out.println("¿Estás seguro de que quieres continuar? (S/N)");
        *  String opt = teclado.nextLine();
        *  if (opt.equalsIgnoreCase("S")) ...
         */
        // Generamos un aviso por consola para prevenir que usemos la opción sin implementar el aviso.
        System.out.println("AVISO: Este método debería implementar un aviso para el usuario.");
        return datos.clearPedidos();
    }

    // Actualizamos el estado de los pedidos de la lista
    public int actualizarEstadoPedidos() {
        return datos.actualizarEstadoPedidos();
    }

    // Actualizamos el estado de un pedido --> automatización de cambio de estado a ENVIADO cuando sea necesario
    public int actualizarEstadoPedido(@NotNull Pedido pedido) {
        return datos.actualizarEstadoPedido(pedido);
    }


    // Imprimimos el prototipo de ticket del pedido
    public void printTicket(Pedido pedido) {
        datos.printTicket(pedido);
    }

    // Actualizamos las listas desde la BBDD al iniciar la aplicación.
    public int actualizarContadores() {
        return datos.actualizarContadores();
    }

    // Actualizamos las listas de códigos únicos para que funcione bien la implementación del
    // manejador de colisiones en la creación de códigos únicos.
    public int actualizarCodigosUnicos() { return datos.actualizarCodigos(); }

    // Método para ejecutar una carga de datos de test en la BD.
    public int loadTestData() { return datos.loadTestData(); }

    // Método para comprobar si existen datos en la BD.
    public boolean checkData() { return datos.checkData(); }
}

package ciricefp.vista;

import ciricefp.controlador.*;
import ciricefp.modelo.Articulo;
import ciricefp.modelo.Cliente;
import ciricefp.modelo.Pedido;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuPrincipal {
    private Controlador controlador;
    private VistaArticulos vistaArticulos;
    private VistaClientes vistaClientes;
    private VistaPedidos vistaPedidos;

    public MenuPrincipal(Controlador controlador) {
        this.controlador = controlador;
        this.vistaArticulos = new VistaArticulos();
        this.vistaClientes = new VistaClientes();
        this.vistaPedidos = new VistaPedidos();
    }

    public MenuPrincipal(Controlador controlador, VistaArticulos vistaArticulos, VistaClientes vistaClientes, VistaPedidos vistaPedidos) {
        this.controlador = controlador;
        this.vistaArticulos = vistaArticulos;
        this.vistaClientes = vistaClientes;
        this.vistaPedidos = vistaPedidos;
    }

    public Controlador getControlador() {
        return controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public VistaArticulos getVistaArticulos() {
        return vistaArticulos;
    }

    public void setVistaArticulos(VistaArticulos vistaArticulos) {
        this.vistaArticulos = vistaArticulos;
    }

    public VistaClientes getVistaClientes() {
        return vistaClientes;
    }

    public void setVistaClientes(VistaClientes vistaClientes) {
        this.vistaClientes = vistaClientes;
    }

    public VistaPedidos getVistaPedidos() {
        return vistaPedidos;
    }

    public void setVistaPedidos(VistaPedidos vistaPedidos) {
        this.vistaPedidos = vistaPedidos;
    }

    public void showMenu(){
        Scanner teclado = new Scanner(System.in);

        //Creamos e inicializamos un formulario para el usuario.
        boolean salir = false;
        String opcio;
        do {
            System.out.println("");
            System.out.println("Principales opciones.");
            System.out.println("* Gestión Artículos.");
            System.out.println("1. Añadir Articulo.");
            System.out.println("2. Mostrar Artículos.");
            System.out.println("* Gestión de Clientes.");
            System.out.println("3. Añadir Clientes.");
            System.out.println("4. Mostrar Clientes.");
            System.out.println("5. Mostrar Clientes Estándar.");
            System.out.println("6. Mostrar Clientes Premium.");
            System.out.println("* Gestión de Pedidos:");
            System.out.println("7. Añadir Pedido.");
            System.out.println("8. Eliminar Pedido.");
            System.out.println("9. Mostrar pedidos pendientes de envío.");
            System.out.println("10. Mostrar pedidos enviados.");
            System.out.println("0. Salir de la aplicación.");
            opcio = demanarOpcioMenu();
            switch (opcio) {
                case "1":
                    Articulo nuevoArticulo = this.vistaArticulos.addArticulo();
                    this.addArticulo(nuevoArticulo);
                    break;
                case "2":
                    ArrayList<Articulo> articulos = this.printArticulos();
                    this.vistaArticulos.printArticulos(articulos);
                    break;
                case "3":
                    Cliente nuevoCliente = this.vistaClientes.addCliente();
                    this.addCliente(nuevoCliente);
                    break;
                case "4":
                    ArrayList<Cliente> clientes = this.printClientes();
                    this.vistaClientes.printClientes(clientes);
                    break;
                case "5":
                    ArrayList<Cliente> clientesEstandar = this.printClientesEstandar();
                    System.out.println(clientesEstandar.size());
                    this.vistaClientes.printClientesEstandar(clientesEstandar);
                    break;
                case "6":
                    ArrayList<Cliente> clientesPremium = this.printClientesPremium();
                    this.vistaClientes.printClientesPremium(clientesPremium);
                    break;
                case "7":
                    Pedido nuevoPedido = this.vistaPedidos.addPedido(this);
                    this.addPedido(nuevoPedido);
                    break;
                case "8":
                    Pedido eliminarPedido = this.vistaPedidos.deletePedido(this);
                    this.controlador.eliminarPedido(eliminarPedido);
                    break;
                case "9":
                    ArrayList<Pedido> pedidosPendientes = this.printPedidosPendientes();
                    this.vistaPedidos.printPedidosPendientes(pedidosPendientes);
                    break;
                case "10":
                    ArrayList<Pedido> pedidosEnviados = this.printPedidosEnviados();
                    this.vistaPedidos.printPedidosEnviados(pedidosEnviados);
                    break;
                case "0":
                    salir = true;
            }
        } while (!salir);
    }

    public void addArticulo(Articulo articulo){
        this.controlador.createArticulo(articulo);
    }

    public ArrayList<Articulo> printArticulos(){
        return this.controlador.listArticulos();
    }
    public Articulo searchArticulo(String code){
        return this.controlador.searchArticulo(code);
    }

    public void addCliente(Cliente cliente){
        this.controlador.createCliente(cliente);
    }
    public ArrayList<Cliente> printClientes(){
        return this.controlador.listClientes();
    }
    public ArrayList<Cliente> printClientesPremium(){
        return this.controlador.filterClientesByType("Premium");
    }
    public ArrayList<Cliente> printClientesEstandar(){
        return this.controlador.filterClientesByType("Estandard");
    }
    public Cliente searchCliente(String dni){
        return this.controlador.searchCliente(dni);
    }
    public void addPedido (Pedido pedido){
        this.controlador.createPedido(pedido);
    }

    public void eliminarPedido (Pedido numeroPedido){
        this.controlador.eliminarPedido(numeroPedido);
    }

    public Pedido searchPedido(int code){
        return this.controlador.searchPedido(code);
    }

    public void actualizarEstadoPedido( Pedido pedido){
        this.controlador.actualizarEstadoPedido(pedido);
    }

    public ArrayList<Pedido> printPedidosPendientes(){
        return this.controlador.filterPedidoByEstado("No Enviado");
    }
    public ArrayList<Pedido> printPedidosEnviados(){
        return this.controlador.filterPedidoByEstado("Enviado");
    }

    @Override
    public String toString() {
        return "MenuPrincipal{" +
                "controlador=" + controlador +
                ", vistaArticulos=" + vistaArticulos +
                ", vistaClientes=" + vistaClientes +
                ", vistaPedidos=" + vistaPedidos +
                '}';
    }

    /**
     * Método para recoger opción del usuario.
     */
    String demanarOpcioMenu() {
        Scanner teclado = new Scanner(System.in);
        String resp;
        System.out.print("Elige una opción (1,2,3,4,5,6,7,8,9,10 o 0): ");
        resp = teclado.nextLine();
        if (resp.isEmpty()) {
            resp = " ";
        }
        return resp;
    }

}

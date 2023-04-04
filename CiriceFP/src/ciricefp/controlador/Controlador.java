package ciricefp.controlador;

import ciricefp.modelo.*;
import ciricefp.vista.*;

import java.util.ArrayList;

/**
 * Esta clase implementa el controlador de la tienda.
 *
 * @author Cirice
 */
public class Controlador {

    private Datos datos;
    private MenuPrincipal menu;

    public Controlador() {
    }
    public Controlador(Datos datos, MenuPrincipal menu) {
        this.datos = datos;
        this.menu = menu;
    }

    public Datos getDatos() {
        return datos;
    }

    public void setDatos(Datos datos) {
        this.datos = datos;
    }

    public MenuPrincipal getMenu() {
        return menu;
    }

    public void setMenu(MenuPrincipal menu) {
        this.menu = menu;
    }

    public void showMenu(){
        this.menu.showMenu();

    }

    public void createArticulo(Articulo articulo){
        this.datos.createArticulo(articulo);
    }

    public ArrayList<Articulo> listArticulos(){
        return this.datos.listArticulos();
    }

    public Articulo searchArticulo(String code){
        return this.datos.searchArticulo(code);
    }

    public void createCliente(Cliente cliente){
        this.datos.createCliente(cliente);
    }
    public ArrayList<Cliente> listClientes(){
        return this.datos.listClientes();
    }
    public ArrayList<Cliente> filterClientesByType(String tipo){
        return this.datos.filterClientesByType(tipo);
    }

    public Cliente searchCliente(String dni){
        return this.datos.searchCliente(dni);
    }

    public void createPedido(Pedido pedido){
        this.datos.createPedido(pedido);
    }

    public void eliminarPedido(Pedido pedido){
        this.datos.deletePedido(pedido);
    }

    public Pedido searchPedido(int code){
        return this.datos.searchPedido(code);
    }

    public void actualizarEstadoPedido( Pedido pedido){
        this.datos.actualizarEstadoPedido(pedido);
    }

    public ArrayList<Pedido> filterPedidoByEstado(String state){
        return this.datos.filterPedidoByEstado(state);
    }

    @Override
    public String toString() {
        return "Controlador{" +
                "datos=" + datos +
                ", menu=" + menu +
                '}';
    }
}

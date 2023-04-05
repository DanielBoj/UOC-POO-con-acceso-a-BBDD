package ciricefp.modelo;

import ciricefp.controlador.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Esta clase funciona como un controlador interno para el módulo Modelo siguiendo el patrón MVC.
 *
 * @author Cirice
 */
public class Datos {

    private Controlador controlador;
    private Listas<Cliente> clientes;
    private Listas<Articulo> articulos;
    private Listas<Pedido> pedido;
    private Conexion baseDatos;

    public Datos(Controlador controlador) {
        this.controlador = controlador;
        this.clientes = new Listas<Cliente>();
        this.articulos = new Listas<Articulo>();
        this.pedido = new Listas<Pedido>();
    }
    public Datos() {
        this.clientes = new Listas<Cliente>();
        this.articulos = new Listas<Articulo>();
        this.pedido = new Listas<Pedido>();
    }

    public Datos(Controlador controlador, Conexion baseDatos) {
        this.controlador = controlador;
        this.baseDatos = baseDatos;
        this.clientes = new Listas<Cliente>();
        this.articulos = new Listas<Articulo>();
        this.pedido = new Listas<Pedido>();
    }

    public Datos(Controlador controlador,
                 Listas<Cliente> clientes,
                 Listas<Articulo> articulos,
                 Conexion baseDatos) {
        this.controlador = controlador;
        this.baseDatos = baseDatos;
        this.clientes = clientes;
        this.articulos = articulos;
        this.pedido = new Listas<Pedido>();

    }

    public Controlador getControlador() {
        return controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public Conexion getBaseDatos() {
        return baseDatos;
    }

    public void setBaseDatos(Conexion baseDatos) {
        this.baseDatos = baseDatos;
    }

    public Listas<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Listas<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Listas<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(Listas<Articulo> articulos) {
        this.articulos = articulos;
    }

    public Listas<Pedido> getPedido() {
        return pedido;
    }

    public void setPedido(Listas<Pedido> pedido) {
        this.pedido = pedido;
    }

    //Cambiada a Void porque add no devuelve nada
    public void createArticulo(Articulo articulo) {
        this.articulos.add(articulo);
    }

    public ArrayList<Articulo> listArticulos(){
        return this.articulos.getLista();
    }

    public Articulo searchArticulo (String codigo){
        for(Articulo art : this.articulos.getLista()) {
           if( art.getCodArticulo().compareTo(codigo) == 0 ){
               return art;
           }
        }
        return null;
    }

    //Cambiada a Void porque clear no devuelve nada
    public void clearArticulos (){
        this.articulos.clear();
    }

    //Eliminar articulo función no consta Uml

    public void removeArticulo (Articulo articulo){
        this.articulos.borrar(articulo);
    }

    //Cambiada a Void porque add no devuelve nada
    public void createCliente(Cliente cliente) {
        this.clientes.add(cliente);
    }
    public ArrayList<Cliente> listClientes(){
        return this.clientes.getArrayList();
    }
    public ArrayList<Cliente> filterClientesByType(String tipo){
        ArrayList<Cliente> clienteFiltered = new ArrayList<Cliente>();
        for( Cliente cl: this.listClientes() ) {
            if( cl.tipoCliente().compareTo(tipo) == 0 ){
                clienteFiltered.add(cl);
            }
        }
        return clienteFiltered;
    }
    public Cliente searchCliente (String dni){
        for( Cliente ok : this.clientes.getLista()) {
            if( ok.getNif().compareTo(dni) == 0 ){
                return ok;
            }
        }
        return null;
    }
    //Cambiada a Void porque clear no devuelve nada
    public void clearClientes (){
        this.clientes.clear();
    }

    //Eliminar articulo función no consta Uml

    public void removeCliente (Cliente cliente){
        this.clientes.borrar(cliente);
    }
    //Cambiada a Void porque add no devuelve nada
    public void createPedido(Pedido pedido) {
        this.pedido.add(pedido);
    }
    public ArrayList<Pedido> listPedidos(){
        return this.pedido.getArrayList();
    }
    //Cambiada a Void porque devulve boolean
    public void deletePedido (Pedido pedido){
        this.pedido.borrar(pedido);
    }
    public Pedido searchPedido (int numeroPedido){
        for( Pedido order : this.pedido.getLista()) {
            if( numeroPedido == order.getNumeroPedido()){
                return order;
            }
        }
        return null;
    }
    public ArrayList<Pedido> filterPedidoByCliente(String dni){
        ArrayList<Pedido> pedidosFiltered = new ArrayList<Pedido>();
        for( Pedido order: this.pedido.getLista() ) {
            if( dni == order.getCliente().getNif()){
                pedidosFiltered.add(order);
            }
        }
        return pedidosFiltered;
    }
    public ArrayList<Pedido> filterPedidoEnviadoByFecha (LocalDate fecha){
        ArrayList<Pedido> pedidosFiltered = new ArrayList<Pedido>();
        for( Pedido order: this.pedido.getLista() ) {
            if( fecha.format( DateTimeFormatter.ofPattern("dd/MM/yyyy") ) == order.getFechaPedido().format( DateTimeFormatter.ofPattern("dd/MM/yyyy")) ){
                pedidosFiltered.add(order);
            }
        }
        return pedidosFiltered;
    }

    public ArrayList<Pedido> filterPedidoByEstado(String opt){
        ArrayList<Pedido> estadoFiltered = new ArrayList<Pedido>();
        this.actualizarEstadoPedidos();
        for( Pedido order: this.pedido.getLista() ) {
            switch (opt) {
                case "Enviado":
                    //Enviado
                    if( order.getEsEnviado() ){
                        estadoFiltered.add(order);
                    }
                    break;
                case "No Enviado":
                    //No enviado
                    if( !order.getEsEnviado() ){
                        estadoFiltered.add(order);
                    }
                    break;
            }

        }
        return estadoFiltered;
    }
    //Cambiada a Void porque clear no devuelve nada
    public void clearPedidos (){
        this.pedido.clear();
    }

    public int actualizarEstadoPedidos (){
        int i=0;
        for( Pedido order: this.pedido.getLista() ) {
            LocalDateTime finPreparacion = order.getFechaPedido().plusMinutes( order.getArticulo().getTiempoPreparacion() * order.getUnidades() );
            if (finPreparacion.isBefore(LocalDateTime.now()) ){
                order.setEsEnviado(true);
                i++;
            }
        }
        return i;
    }
    //RECIBE UN CÓDIGO EN LUGAR DE UN PEDIDO PARA ACTUALIZAR ESTE PEDIDO EN CONCRETO
    public void actualizarEstadoPedido (Pedido pedido){
        Pedido pedidoEncontrado = this.searchPedido(pedido.getNumeroPedido());
        LocalDateTime finPreparacion = pedidoEncontrado.getFechaPedido().plusMinutes( pedidoEncontrado.getArticulo().getTiempoPreparacion() * pedidoEncontrado.getUnidades() );
        if (finPreparacion.isBefore(LocalDateTime.now()) ){
            pedidoEncontrado.setEsEnviado(true);
        }

    }

    @Override
    public String toString() {
        return "Datos{" +
                "controlador=" + controlador +
                ", clientes=" + clientes +
                ", articulos=" + articulos +
                ", pedido=" + pedido +
                ", baseDatos=" + baseDatos +
                '}';
    }
    

}

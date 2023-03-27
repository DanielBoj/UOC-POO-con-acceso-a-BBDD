package ciricefp.modelo;

import ciricefp.controlador.*;

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

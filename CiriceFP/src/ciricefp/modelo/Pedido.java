package ciricefp.modelo;

import java.time.LocalDateTime;

/**
 * Esta clase implementa la lógica de negocio para almacenar los Pedidos que pueda hacer un cliente de la tienda online.
 *
 * @author Cirice
 */
public class Pedido implements Comparable<Pedido> {

    private final int numeroPedido;
    private Cliente cliente;
    private Articulo articulo;
    private int unidades;
    private LocalDateTime fechaPedido;
    private boolean esEnviado;
    private static int totalPedidos = 0;

    public Pedido(Cliente cliente,
                  Articulo articulo,
                  int unidades,
                  LocalDateTime fechaPedido,
                  boolean esEnviado) {
        this.numeroPedido = ++totalPedidos;
        this.cliente = cliente;
        this.articulo = articulo;
        this.unidades = unidades;
        this.fechaPedido = fechaPedido;
        this.esEnviado = esEnviado;
        // TODO --> this.costeEnvio = calculateCosteEnvio();
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public boolean isEsEnviado() {
        return esEnviado;
    }

    public void setEsEnviado(boolean esEnviado) {
        this.esEnviado = esEnviado;
    }

    public static int getTotalPedidos() {
        return totalPedidos;
    }

    public static void setTotalPedidos(int totalPedidos) {
        Pedido.totalPedidos = totalPedidos;
    }

    @Override
    public int compareTo(Pedido sourcePedido) {
        return sourcePedido.getNumeroPedido() == this.numeroPedido? 0 : -1;
    }
}

// TODO --> public double calculatePrecioTotal() {}

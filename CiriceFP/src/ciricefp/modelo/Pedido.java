package ciricefp.modelo;

import ciricefp.modelo.interfaces.IPedido;

import java.time.LocalDate;

/**
 * Esta clase implementa la lógica de negocio para almacenar los Pedidos que pueda hacer un cliente de la tienda online.
 *
 * @author Cirice
 */
public class Pedido implements Comparable<Pedido>, IPedido {

    // Atributos de la clase.
    private final int numeroPedido;
    private Cliente cliente;
    private Articulo articulo;
    private int unidades;
    private LocalDate fechaPedido;
    private boolean esEnviado;
    private static int totalPedidos = 0;

    // Constructor sin argumentos por defecto
    public Pedido() {
        this.fechaPedido = LocalDate.now();
        this.esEnviado = false;
        this.numeroPedido = ++totalPedidos;
    }

    // Constructor con los argumentos mínimos que necesita el pedido
    public Pedido(Cliente cliente,
                  Articulo articulo,
                  int unidades) {
        this.cliente = cliente;
        this.articulo = articulo;
        this.unidades = unidades;
        this.fechaPedido = LocalDate.now();
        this.esEnviado = false;
        this.numeroPedido = ++totalPedidos;
    }

    // Constructor con todos los argumentos
    public Pedido(Cliente cliente,
                  Articulo articulo,
                  int unidades,
                  LocalDate fechaPedido,
                  boolean esEnviado) {
        this.cliente = cliente;
        this.articulo = articulo;
        this.unidades = unidades;
        this.fechaPedido = fechaPedido;
        this.esEnviado = esEnviado;

        this.numeroPedido = ++totalPedidos;
    }

    /* Getters & Setters */
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

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public boolean getEsEnviado() {
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

    // StringBuilder nos permite implementar un patrón de diseño de string para el método toString() de una forma visual muy clara.
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("Pedido Número: " + this.numeroPedido + "\n");
        sb.append("Fecha: " + this.fechaPedido + "\n")
                .append("Nif: ").append(this.cliente.getNif()).append(" ")
                        .append("Nombre: ").append(this.cliente.getNombre()).append("\n")
                        .append("Dirección de envio: ").append(this.cliente.getDomicilio()).append("\n")
                        .append("Artículo: ").append(this.articulo.getDescripcion()).append("\t")
                        .append("Código: ").append(this.articulo.getCodArticulo()).append("\n")
                        .append("Unidades").append("\t").append("Precio").append("\t").append("Total").append("\n")
                        .append(this.unidades).append("\t").append(this.articulo.getPvp()).append("\t").append(this.unidades * this.articulo.getPvp()).append("\n")
                        .append("Gastos de envío: ").append(this.precioEnvio()).append("\n")
                        .append("Precio total: ").append(this.precioTotal()).append("\n");

        if (this.esEnviado) {
            sb.append("El pedido ha sido enviado");
        } else {
            sb.append("El pedido no ha sido enviado");
        }

        return sb.toString();
    }

    // Implementamos el método de la interfaz Comparable
    @Override
    public int compareTo(Pedido sourcePedido) {
        return sourcePedido.getNumeroPedido() == this.numeroPedido? 0 : -1;
    }

    /* Métodos de la clase -> Implementamos los métodos del Interface */

    // Añadimos un cliente al pedido
    @Override
    public boolean addCliente(Cliente cliente) {
        if (cliente == null) {
            System.out.println("El cliente no puede ser nulo");

            // Exception a manejar para la futura implementación de vistas.
            // throw new NullPointerException("El cliente no puede ser nulo");
        }
        // Intentamos setear el cliente del pedido. Si se produce una excepción, devolvemos false.
        try {
            this.setCliente(cliente);
            return true;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    // Editamos el cliente del pedido
    @Override
    public boolean editCliente(Cliente cliente) {
        if (cliente == null) {
            System.out.println("El cliente no puede ser nulo");

            // Exception a manejar para la futura implementación de vistas.
            // throw new NullPointerException("El cliente no puede ser nulo");
        }

        // Intentamos setear el cliente del pedido. Si se produce una excepción, devolvemos false.
        try {
            this.setCliente(cliente);
            return true;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Añadimos un artículo al pedido
    @Override
    public boolean addArticulo(Articulo articulo) {
        if (articulo == null) {
            System.out.println("El artículo no puede ser nulo");

            // Exception a manejar para la futura implementación de vistas.
            // throw new NullPointerException("El artículo no puede ser nulo");
        }

        // Intentamos setear el artículo del pedido. Si se produce una excepción, devolvemos false.
        try {
            this.setArticulo(articulo);
            return true;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    // Editamos el artículo del pedido
    @Override
    public boolean editArticulo(Articulo articulo) {
        if (articulo == null) {
            System.out.println("El artículo no puede ser nulo");

            // Exception a manejar para la futura implementación de vistas.
            // throw new NullPointerException("El artículo no puede ser nulo");
        }

        // Intentamos setear el artículo del pedido. Si se produce una excepción, devolvemos false.
        try {
            this.setArticulo(articulo);
            return true;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    // Añadimos las unidades del pedido
    @Override
    public boolean addUnidades(int unidades) {
        // Solo aceptamos pedidos de al menos 1 unidad.
        if (unidades < 1) {
            System.out.println("El número de unidades debe ser al menos 1.");
            // Exception a manejar para la futura implementación de vistas.
            //throw new IllegalArgumentException("El número de unidades debe ser al menos 1.");
        }
        // Intentamos setear las unidades del pedido. Si se produce una excepción, devolvemos false.
        try {
            this.setUnidades(unidades);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    // Editamos las unidades del pedido
    @Override
    public boolean editUnidades(int unidades) {
        // Solo aceptamos pedidos de al menos 1 unidad.
        if (unidades < 1) {
            System.out.println("El número de unidades debe ser al menos 1.");
            // Exception a manejar para la futura implementación de vistas.
            //throw new IllegalArgumentException("El número de unidades debe ser al menos 1.");
        }
        // Intentamos setear las unidades del pedido. Si se produce una excepción, devolvemos false.
        try {
            this.setUnidades(unidades);
            return true;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    // Calculamos la fecha de envío teniendo en cuenta el tiempo de preparación del artículo expresado en días.
    @Override
    public boolean pedidoEnviado() {

        // Calculamos la fecha de envío teniendo en cuenta el tiempo de preparación del artículo expresado en días.
        LocalDate fechaEnvio = this.calcularFechaEnvio();

        // Devolvemos true si la fecha de envío es igual o posterior a la fecha del pedido.
        return this.fechaPedido.equals(fechaEnvio) || this.fechaPedido.isAfter(fechaEnvio);
    }

    // Calculamos el precio del envío teniendo en cuenta el descuento aplicable a clientes Premium
    @Override
    public double precioEnvio() {

        return this.cliente.tipoCliente().equals("ClientePremium")? this.articulo.getGastosEnvio() - this.cliente.descuentoEnv(this.articulo.getGastosEnvio()) :
                this.articulo.getGastosEnvio();
    }

    // Calculamos el precio total de la línea de articulo
    @Override
    public double calcularSubtotal() {
        return this.unidades * this.articulo.getPvp();
    }

    // Calculamos la fecha de envío teniendo en cuenta el tiempo de preparación del artículo expresado en días.
    @Override
    public LocalDate calcularFechaEnvio() {
        return this.fechaPedido.plusDays(this.articulo.getTiempoPreparacion());
    }

    // Calculamos el precio total del pedido teniendo en cuenta el precio del envío
    @Override
    public double precioTotal() {
        return (this.unidades * this.articulo.getPvp()) + this.precioEnvio();
    }

    // Implementamos un método para la interfaz Comparable
    public static <U extends Comparable<? super U>, T> U getFechaEnvio(T t) {
        if (t instanceof Pedido pedido) {
            return (U) pedido.calcularFechaEnvio();
        }

        return null;
    }
}

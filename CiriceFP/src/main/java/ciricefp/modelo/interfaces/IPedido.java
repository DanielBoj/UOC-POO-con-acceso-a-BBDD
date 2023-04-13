package ciricefp.modelo.interfaces;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.Cliente;

import java.time.LocalDate;

/**
 * Lss interfaces nos permiten prototipar los métodos que por defecto usará una clase que implemente la interfaz.
 * Resultan especialmente útiles cuando se realiza la implementación de la capa de acceso a datos.
 *
 * @author Cirice
 */
public interface IPedido {

    /* Declaramos los métodos que deberán implementar la clase del tipo Pedido. */
    public boolean addCliente(Cliente cliente);
    public boolean editCliente(Cliente cliente);
    public boolean addArticulo(Articulo articulo);
    public boolean editArticulo(Articulo articulo);
    public boolean addUnidades(int unidades);
    public boolean editUnidades(int unidades);
    public boolean pedidoEnviado();
    public double precioEnvio();
    public double precioTotal();
    public double calcularSubtotal();
    public LocalDate calcularFechaEnvio();
}

package ciricefp.modelo.interfaces;

import ciricefp.modelo.Cliente;

/**
 * Las interfaces nos permiten prototipar los métodos que por defecto usará una clase que implemente la interfaz.
 * Resultan especialmente útiles cuando se realiza la implementación de la capa de acceso a datos.
 *
 * @author Cirice
 */
public interface ICliente {

    /* Declaramos los métodos que deberán implementar la clase del tipo Cliente. */
    public double calcAnual();
    public double descuentoEnv(double costeEnvio);
    public String tipoCliente();
    public int compareTo(Cliente srcCliente);
}

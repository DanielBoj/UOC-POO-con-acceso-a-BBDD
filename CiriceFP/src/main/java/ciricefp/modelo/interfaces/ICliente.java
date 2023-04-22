package ciricefp.modelo.interfaces;

import ciricefp.modelo.*;

/**
 * Las interfaces nos permiten prototipar los métodos que por defecto usará una clase que implemente la interfaz.
 * Resultan especialmente útiles cuando se realiza la implementación de la capa de acceso a datos.
 *
 * @author Cirice
 */
public interface ICliente extends Comparable<Cliente> {

    /* Declaramos los métodos que deberán implementar la clase del tipo Cliente. */
    // Hemos migrado la mayor parte de estos métodos a la Factory.

    /**
     *
     * @see ciricefp.modelo.interfaces.factory.IClienteFactory
     */
    // double calcAnual();
    //double descuentoEnv(double costeEnvio);
    // String tipoCliente();
    int compareTo(Cliente srcCliente);
}

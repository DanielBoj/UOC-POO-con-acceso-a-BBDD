package ciricefp.modelo;

/**
 * Esta clase implemente la lógica de negocio para el subtipo de Cliente Estandard.
 *
 * @author Cirice
 */
public class ClienteEstandard extends Cliente {
    public ClienteEstandard(String nombre, Direccion domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    // Usa los métodos del padre, al no haber añadido atributo, no hace fata añadir ningún método.
}

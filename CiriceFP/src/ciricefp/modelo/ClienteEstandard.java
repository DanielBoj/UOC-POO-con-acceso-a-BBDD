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

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String tipoCliente() {
        return "Estandard";
    }

    @Override
    public double calcAnual() {
        return 0;
    }

    @Override
    public double descuentoEnv() {
        return 0;
    }

}

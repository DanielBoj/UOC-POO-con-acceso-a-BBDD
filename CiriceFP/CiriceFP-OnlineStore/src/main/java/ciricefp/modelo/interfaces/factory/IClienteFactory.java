package ciricefp.modelo.interfaces.factory;

import ciricefp.modelo.Cliente;
import ciricefp.modelo.ClienteEstandard;
import ciricefp.modelo.ClientePremium;
import ciricefp.modelo.Direccion;

/**
 * Esta interfaz implementa el diseño Factory para la clase Cliente y sus subclases.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public interface IClienteFactory {
    // Producto 3 -> Diseño Factory: A partir de Java 8 podemos implementar métodos estáticos en las interfaces.
    // Estos métodos nos permiten crear objetos de tipo ClienteEstandard o ClientePremium.
    // El método createCliente() recibe un String con el tipo de cliente que queremos crear.
    // El método calcAnual() recibe un String con el tipo de cliente del que queremos calcular el coste anual.
    // El método descuentoEnv() recibe un String con el tipo de cliente del que queremos calcular el descuento en el envío.
    static Cliente createCliente(String nombre, Direccion domicilio, String nif, String email, String tipoCliente) {

        return switch (tipoCliente.toUpperCase()) {
            case "ESTANDARD" -> new ClienteEstandard(nombre, domicilio, nif, email);
            case "PREMIUM" -> new ClientePremium(nombre, domicilio, nif, email);
            default -> null;
        };
    }

    static Double calcAnual(Cliente srcCliente) {
        String tipoCliente = tipoCliente(srcCliente).toUpperCase();

        return switch (tipoCliente) {
            case "CLIENTEPREMIUM" -> ((ClientePremium) srcCliente).getCuota();
            default -> 0.0;
        };
    }

    static Double descuentoEnv(Cliente srcCliente, double costeEnvio) {
        String tipoCliente = tipoCliente(srcCliente).toUpperCase();

        return switch (tipoCliente) {
            case "CLIENTEESTANDARD" -> costeEnvio;
            case "CLIENTEPREMIUM" -> costeEnvio * ((ClientePremium) srcCliente).getDescuento();
            default -> null;
        };
    }

    static String tipoCliente(Cliente srcCliente) {
        return srcCliente.getClass().getSimpleName();
    }

    static double getCuota(Cliente srcCliente) {
        String tipoCliente = tipoCliente(srcCliente).toUpperCase();

        return switch (tipoCliente) {
            case "CLIENTEPREMIUM" -> ((ClientePremium) srcCliente).getCuota();
            default -> 0.0;
        };
    }

    static double getDescuento(Cliente srcCliente) {
        String tipoCliente = tipoCliente(srcCliente).toUpperCase();

        return switch (tipoCliente) {
            case "CLIENTEPREMIUM" -> ((ClientePremium) srcCliente).getDescuento();
            default -> 0.0;
        };
    }

    static String getCodSocio(Cliente srcCliente) {
        String tipoCliente = tipoCliente(srcCliente).toUpperCase();

        return switch (tipoCliente) {
            case "CLIENTEPREMIUM" -> ((ClientePremium) srcCliente).getCodSocio();
            default -> "";
        };
    }
}

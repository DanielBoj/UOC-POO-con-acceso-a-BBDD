package ciricefp.modelo;

import ciricefp.modelo.interfaces.HashCode;

/**
 * Esta clase implementa la lógica de negocio para el subtipo de Cliente Premium.
 * Esta clase es una subclase de la superclase Cliente e implementa los métodos abstractos.
 *
 * @author Cirice
 */
public class ClientePremium extends Cliente {

    // Atributos de la clase.
    private double cuota;
    private double descuento;
    private String codSocio;
    private static int totalSocios = 0;

    // Constructor por defecto, recibe todos los elementos necesarios por parámetro. Llama al constructor de la superclase.
    public ClientePremium (String nombre,
                           Direccion domicilio,
                           String nif,
                           String email) {
        super(nombre, domicilio, nif, email);

        // Establecemos los valores por defecto según vienen definidos en el caso práctico.
        this.cuota = 30;
        this.descuento = 0.2;

        // Generamos el código de socio.
        this.codSocio = this.generateCodCliente(nif);

        totalSocios++;
    }

    // Constructor que recibe además los parámetros de descuento y cuota.
    public ClientePremium(String nombre,
                          Direccion domicilio,
                          String nif,
                          String email,
                          double cuota,
                          double descuento) {
        super(nombre, domicilio, nif, email);
        this.cuota = cuota;
        this.descuento = descuento;

        // Generamos el código de socio.
        this.codSocio = this.generateCodCliente(nif);

        totalSocios++;
    }

    /* Getters & Setters */
    public double getCuota() {
        return cuota;
    }

    public void setCuota(double cuota) {
        this.cuota = cuota;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getCodSocio() {
        return codSocio;
    }

    public static int getTotalSocios() {
        return totalSocios;
    }

    // StringBuilder nos permite implementar un patrón de diseño de string para el método toString() de una forma visual muy clara.
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n")
                .append("Código de socio: ").append(this.codSocio).append("\n")
                .append("Cuota: ").append(this.cuota).append("€\n")
                .append("Descuento: ").append(this.descuento * 100).append("%");

        return sb.toString();
    }

    /* Métodos de clase */

    /**
     * El método tipoCliente() devuelve el tipo de cliente en formato String.
     *
     * @return String con el tipo de cliente.
     */
    public String tipoCliente() {
        return this.getClass().getSimpleName();
    }

    /* IMPORTANTE */
    // Un cliente Premium tiene un descuento del 20% en el envío y paga una cuota anual de 30€.
    // Según el caso práctico estos valores son fijos, por eso directamente devolvemos el valor del atributo.
    @Override
    public double calcAnual() {
        return this.getCuota();
    }

    @Override
    public double descuentoEnv(double costeEnvio) {
        return costeEnvio * this.descuento;
    }

    // Implementación del método para generar un código único usando el NIF del cliente y la interfaz HashCode.
    private String generateCodCliente(String key) {
        return "PREMIUM" + HashCode.generateHash(key);
    }
}

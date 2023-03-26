package ciricefp.modelo;

/**
 * Esta clase implementa la lógica de negocio para el subtipo de Cliente Premium.
 *
 * @author Cirice
 */
public class ClientePremium extends Cliente {

    private double cuota;
    private double descuento;
    private String codSocio;
    private static int totalSocios = 0;

    public ClientePremium(String nombre,
                          Direccion domicilio,
                          String nif,
                          String email,
                          double cuota,
                          double descuento) {
        super(nombre, domicilio, nif, email);
        this.cuota = cuota;
        this.descuento = descuento;
        // TODO --> this.codCliente = generateCodCliente(nif);
        this.totalSocios++;
    }

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

    @Override
    public String toString() {
        return super.toString() +
                " - ClientePremium{" +
                "cuota=" + cuota +
                ", descuento=" + descuento +
                ", codSocio='" + codSocio + '\'' +
                '}';
    }

    @Override
    public String tipoCliente() {
        return "Premium";
    }

    @Override
    public double calcAnual() {
        return this.getCuota();
    }

    @Override
    public double descuentoEnv() {
        return this.getDescuento();
    }


}

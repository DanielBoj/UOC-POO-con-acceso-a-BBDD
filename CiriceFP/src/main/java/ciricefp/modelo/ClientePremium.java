package ciricefp.modelo;

import ciricefp.modelo.interfaces.HashCode;

import java.util.ArrayList;

/**
 * Esta clase implementa la lógica de negocio para el subtipo de Cliente Premium.
 * Esta clase es una subclase de la superclase Cliente e implementa los métodos abstractos.
 *
 * @author Cirice
 */
public class ClientePremium extends Cliente implements HashCode {

    // Atributos de la clase.
    private double cuota;
    private double descuento;
    private String codSocio;

    private static ArrayList<String> codigos;

    // Constructor por defecto, recibe todos los elementos necesarios por parámetro. Llama al constructor de la superclase.
    public ClientePremium (String nombre,
                           Direccion domicilio,
                           String nif,
                           String email) {
        super(nombre, domicilio, nif, email);

        // Establecemos los valores por defecto según vienen definidos en el caso práctico.
        this.cuota = 30;
        this.descuento = 0.2;

        codigos =  new ArrayList<>();

        // Generamos el código de socio.
        this.codSocio = this.generateCodigo(nif);
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

        codigos =  new ArrayList<>();

        // Generamos el código de socio.
        this.codSocio = this.generateCodigo(nif);
    }

    // Producto 3 --> COnstructor vacío
    public ClientePremium() {
        codigos = new ArrayList<>();
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

    public void setCodSocio(String codSocio) {
        this.codSocio = codSocio;
    }

    public static ArrayList<String> getCodigos() {
        return codigos;
    }

    public static void setCodigos(ArrayList<String> codigos) {
        ClientePremium.codigos = codigos;
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
//    @Override
//    public double calcAnual() {
//        return this.getCuota();
//    }
//
//    @Override
//    public double descuentoEnv(double costeEnvio) {
//        return costeEnvio * this.descuento;
//    }

    @Override
    // Implementación del método para generar un código único usando el NIF del cliente y la interfaz HashCode.
    public String generateCodigo(String key) {
        // Obtenemos el código generado.
        String codigo = HashCode.generateHash(key);

        // Comprobamos que el código no exista
        codigo = manageCollisions(codigo, codigos);

        return "PREMIUM" + HashCode.generateHash(key);
    }


    // Método para gestionar las colisiones mediante una función recursiva.
    @Override
    public String manageCollisions(String key, ArrayList<String> set) {
        if (set.isEmpty()) {
            return key;
        }
        // Primero definimos el caso base.
        // Normalizamos el código
        String codigo = "PREMIUM" + key;
        // Recorremos la lista buscando coincidencias.
        if (set.stream().noneMatch(codigo::equals)) {

            // Si no hay coincidencias, devolvemos el código generado.
            return key;
        }

        key = Integer.toString(Integer.parseInt(key) + 1);
        // Si hay coincidencias, generamos un nuevo código.
        return manageCollisions(key + "1", set);
    }
}

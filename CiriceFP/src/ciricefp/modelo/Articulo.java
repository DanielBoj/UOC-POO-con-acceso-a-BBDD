package ciricefp.modelo;

import ciricefp.modelo.interfaces.HashCode;
import ciricefp.modelo.interfaces.IArticulo;

/**
 * Esta clase implementa la lógica de negocio de un artículo que se puede comprar en la tienda.
 *
 * @author Cirice
 */
public class Articulo implements Comparable<Articulo>, IArticulo, HashCode {

    // Atributos de la clase.
    private String codArticulo;
    private String descripcion;
    private double pvp;
    private double gastosEnvio;
    private int tiempoPreparacion; // En días
    private static int totalArticulos = 0;

    // Constructor implementando la creación automática del código único de artículo.
    public Articulo(String descripcion,
                    double pvp,
                    double gastosEnvio,
                    int tiempoPreparacion) {
        this.descripcion = descripcion;
        this.pvp = pvp;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
        this.codArticulo = generateCodigo(this.descripcion);

        totalArticulos++;
    }

    /* Getters & Setters */
    public String getCodArticulo() {
        return codArticulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPvp() {
        return pvp;
    }

    public void setPvp(double pvp) {
        this.pvp = pvp;
    }

    public double getGastosEnvio() {
        return gastosEnvio;
    }

    public void setGastosEnvio(double gastosEnvio) {
        this.gastosEnvio = gastosEnvio;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    // StringBuilder nos permite implementar un patrón de diseño de string para el método toString() de una forma visual muy clara.
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("Articulo: ");
        sb.append("codArticulo: ").append(codArticulo).append('\t')
                .append(descripcion).append('\n')
                .append("Precio: ").append(pvp).append('\n')
                .append("Gastos de envío: ").append(gastosEnvio).append('\n')
                .append("Tiempo de preparación: ").append(tiempoPreparacion).append(" días");

        return sb.toString();
    }

    // Método para comparar dos artículos, implementamos la clase de Java Comparable.
    @Override
    public int compareTo (Articulo sourceArticulo) {
        return sourceArticulo.getCodArticulo().equals(this.codArticulo)? 0 : -1;
    }

    // Generamos el código único del artículo mediante la implementación del interfaz HashCode.
    @Override
    public String generateCodigo(String key) {
        return "A" + HashCode.generateHash(key);
    }
}

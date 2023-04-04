package ciricefp.modelo;

/**
 * Esta clase implementa la lógica de negocio de un artículo que se puede comprar en la tienda.
 *
 * @author Cirice
 */
public class Articulo implements Comparable<Articulo> {

    private String codArticulo;
    private String descripcion;
    private double pvp;
    private double gastosEnvio;
    private int tiempoPreparacion;
    private static int totalArticulos = 0; //TODO --> Tener en cuenta que en el método add() hay que implementar la suma al contador de artículos.

    public Articulo(String codArticulo, String descripcion, double pvp, double gastosEnvio, int tiempoPreparacion) {
        this.codArticulo = codArticulo;
        this.descripcion = descripcion;
        this.pvp = pvp;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
    }
    public Articulo(String descripcion,
                    double pvp,
                    double gastosEnvio,
                    int tiempoPreparacion) {
        this.descripcion = descripcion;
        this.pvp = pvp;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
        // TODO --> this.codArticulo = generateCodArticulo();
        // TODO --> Tener en cuenta que en el método add() hay que implementar la suma al contador de artículos.
    }

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

    @Override
    public String toString() {
        return "Articulo{" +
                "codArticulo='" + codArticulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", pvp=" + pvp +
                ", gastosEnvio=" + gastosEnvio +
                ", tiempoPreparacion=" + tiempoPreparacion +
                '}';
    }

    @Override
    public int compareTo (Articulo sourceArticulo) {
        return sourceArticulo.getCodArticulo().equals(this.codArticulo)? 0 : -1;
    }
}

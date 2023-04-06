package ciricefp.modelo;

/**
 * Esta clase funciona como un prototipo para añadir elementos dirección en las clases que lo necesiten implementar.
 * En nuestro caso lo usará la clase Cliente.
 *
 * @author Cirice
 */
public class Direccion {

    // Atributos de clase
    String direccion;
    String ciudad;
    String provincia;
    String codigoPostal;
    String pais;

    // Constructor vacío por si se necesitase en la implementación de algún método
    public Direccion() {
        this.direccion = "";
        this.ciudad = "";
        this.provincia = "";
        this.codigoPostal = "";
        this.pais = "";
    }

    // Constructor por defecto que recibe todos los atributos por parámetro.
    public Direccion(String direccion, String ciudad, String provincia, String codigoPostal, String pais) {
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.pais = pais;
    }

    /* Getters & Setters */
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    // StringBuilder nos permite implementar un patrón de diseño de string para el método toString() de una forma visual muy clara.
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("Direccion: ");
        sb.append(direccion).append("\n")
                .append("Ciudad: ").append(ciudad).append("\n")
                .append("Provincia: ").append(provincia).append("\n")
                .append("Código Postal: ").append(codigoPostal).append("\n")
                .append("País: ").append(pais);
        return sb.toString();
    }
}

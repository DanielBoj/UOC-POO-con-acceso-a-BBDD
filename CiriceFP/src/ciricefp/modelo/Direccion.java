package ciricefp.modelo;

/**
 * Esta clase funciona como un prototipo para añadir elementoa dirección en las clases que lo necesiten implementar.
 * En nuestro caso lo usará la clase Cliente.
 *
 * @author Cirice
 */
public class Direccion {

    String direccion;
    String ciudad;
    String provincia;
    String codigoPostal;
    String pais;

    public Direccion(String direccion, String ciudad, String provincia, String codigoPostal, String pais) {
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.pais = pais;
    }

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

    @Override
    public String toString() {
        return "Direccion{" +
                "direccion='" + direccion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}

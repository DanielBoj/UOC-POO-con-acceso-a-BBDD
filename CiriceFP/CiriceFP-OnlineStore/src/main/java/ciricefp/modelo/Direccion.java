package ciricefp.modelo;

import jakarta.persistence.*;

/**
 * Esta clase funciona como un prototipo para añadir elementos dirección en las clases que lo necesiten implementar.
 * En nuestro caso lo usará la clase Cliente.
 *
 * @author Cirice
 */
@Entity
@Table(name = "direcciones")
public class Direccion {

    // Atributos de clase
    // Producto 3 -> Añadimos el id de nuestro modelo relacional
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private Long id;
    String direccion;
    String ciudad;
    String provincia;
    @Column(name = "codigo_postal")
    String codigoPostal;
    String pais;

    // Constructor vacío por si se necesitase en la implementación de algún método
    public Direccion() { }

    // Constructor por defecto que recibe todos los atributos por parámetro.
    public Direccion(String direccion, String ciudad, String provincia, String codigoPostal, String pais) {
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.pais = pais;
    }

    // Constructor que recibe además el id de nuestro modelo relacional
    public Direccion(Long id, String direccion, String ciudad, String provincia, String codigoPostal, String pais) {
        this.id = id;
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

    // Producto 3 -> Añadimos el id de nuestro modelo relacional
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // StringBuilder nos permite implementar un patrón de diseño de string para el método toString() de una forma visual muy clara.
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("Direccion: ");
        sb.append(direccion).append("\n")
                .append("Ciudad: ").append(ciudad).append("\n")
                .append("Provincia: ").append(provincia).append("\n")
                .append("Código Postal: ").append(codigoPostal).append("\n")
                .append("País: ").append(pais).append("\n")
                .append("Id: ").append(id).append("\n");

        return sb.toString();
    }
}

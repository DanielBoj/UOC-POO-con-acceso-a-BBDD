package ciricefp.modelo;

/**
 * Esta clase implementa la lógica de negocio de un cliente que puede comprar en la tienda.
 * Es una superclase que heredan las clases ClienteEstandard y ClientePremium.
 *
 * @author Cirice
 */
public abstract class Cliente implements Comparable<Cliente> {

    private String nombre;
    private Direccion domicilio;
    private String nif;
    private String email;
    private static Integer totalClientes = 0;

    public Cliente(String nombre, Direccion domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
        // TODO --> Tener en cuenta que en el método add() hay que implementar la suma al contador de clientes.
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Direccion getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Direccion domicilio) {
        this.domicilio = domicilio;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Integer getTotalClientes() {
        return totalClientes;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", domicilio=" + domicilio +
                ", nif='" + nif + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int compareTo (Cliente sourceCliente) {
        return sourceCliente.getNif().equals(this.nif)? 0 : -1;
    }

    abstract public String tipoCliente();
    abstract public double calcAnual();
    abstract public double descuentoEnv();


}

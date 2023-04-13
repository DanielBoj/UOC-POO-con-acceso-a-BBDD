package ciricefp.modelo;

import ciricefp.modelo.interfaces.ICliente;

/**
 * Esta clase implementa la lógica de negocio de un cliente que puede comprar en la tienda.
 * Es una superclase que heredan las clases ClienteEstandard y ClientePremium.
 *
 * @author Cirice
 */
public abstract class Cliente implements Comparable<Cliente>, ICliente {

    // Atributos de la clase.
    // Producto 3 -> Añadimos el id de nuestro modelo relacional
    private Long id;
    private String nombre;
    private Direccion domicilio;
    private String nif;
    private String email;
    private static Integer totalClientes = 0;

    // Constructor sin parámetros por si se necesitase en la implementación de algún método
    // que requiera la creación de un objeto de tipo Cliente.
    public Cliente() { this.domicilio = new Direccion(); }

    // Constructor por defecto, recibe todos los elementos necesarios por parámetro.
    public Cliente(String nombre, Direccion domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;

        totalClientes++;
    }

    // Constructor que recibe además el id de nuestro modelo relacional
    public Cliente(Long id, String nombre, Direccion domicilio, String nif, String email) {
        this.id = id;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;

        totalClientes++;
    }

    /* Getters & Setters */
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

        // Usamos un string builder
        StringBuilder sb = new StringBuilder("Cliente: ");
        sb.append(nombre).append("\n")
                .append("Tipo: ").append(tipoCliente()).append("\n")
                .append("Domicilio: \n").append(domicilio).append("\n")
                .append("NIF: ").append(nif).append("\n")
                .append("Email: ").append(email).append("\n")
                .append("Id: ").append(id).append("\n");

        return sb.toString();
    }

    // Método para comparar dos clientes por su NIF, implementa la interfaz Comparable.
    @Override
    public int compareTo (Cliente sourceCliente) {
        return sourceCliente.getNif().equals(this.nif)? 0 : -1;
    }

    /* Métodos abstractos --> Cada clase hija debe implementar su versión de estos métodos */
    // Obtenemos el tipo de cliente en formato string
    public abstract String tipoCliente();

    // Nos deveulve la anualiadad del cliente.
    /* IMPORTANTE */
    // En el caso práctico se especifica que la anualidad es fija para todos los clientes, por eso el método simplemente devolverá el valor del atributo.
    public abstract double calcAnual();

    // Método para calcular el descuento que se le aplica al cliente en función de su tipo.
    public abstract double descuentoEnv(double costeEnvio);

    // Este método puede usarse para modificar el contador de clientes.
    public static void advanceTotalClientes() {
        ++totalClientes;
    }

    // Este método puede usarse para modificar el contador de clientes.
    public static void decreaseTotalClientes() {
        --totalClientes;
    }

    // Este método setea el contador a 0.
    public static void resetTotalClientes() {
        totalClientes = 0;
    }
}

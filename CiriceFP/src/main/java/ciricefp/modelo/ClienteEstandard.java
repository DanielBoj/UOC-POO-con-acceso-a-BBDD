package ciricefp.modelo;

import jakarta.persistence.*;

/**
 * Esta clase implementa la lógica de negocio para el subtipo de Cliente Estandard.
 * Esta clase es una subclase de la superclase Cliente e implementa los métodos abstractos.
 *
 * @author Cirice
 */
@Entity
@Table(name = "clientes_estandard")
// Indicamos que se trata de una clase hija de Cliente.
@PrimaryKeyJoinColumn(name = "cliente_id")
public class ClienteEstandard extends Cliente {
    // Producto 4 -> Añadimos los atributos Entity necesarios
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "cliente_id")
    private Long clienteId;*/
    // Constructor por defecto, recibe todos los elementos necesarios por parámetro. Llama al constructor de la superclase.
    public ClienteEstandard(String nombre, Direccion domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    // Constructor vacío, llama al constructor de la superclase.
    public ClienteEstandard() {
        super();
    }

    /* Override métodos padre */
    @Override
    public String toString() {
        return super.toString();
    }

//    @Override
//    public String tipoCliente() {
//        return this.getClass().getSimpleName();
//    }
//
//    // un cliente Estándar no tiene descuento en el envío ni paga cuota anual
//    @Override
//    public double calcAnual() { return 0; }
//
//    @Override
//    public double descuentoEnv(double costeEnvio) {
//        return 0;
//    }
}

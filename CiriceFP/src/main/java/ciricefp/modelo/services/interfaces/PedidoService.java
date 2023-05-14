package ciricefp.modelo.services.interfaces;

import ciricefp.modelo.Pedido;
import ciricefp.modelo.listas.Listas;

import java.util.Optional;

/**
 * Implementamos un interfaz para poder trabajar con los servicios de los pedidos.
 * El servicio deberá implementar la lógica de negocio de la aplicación y el contrato con el interface.
 *
 * @author Cirice
 * @version 1.0
 * @since 05-2023
 */
public interface PedidoService {
    // Los métodos de lectura de todos los objetos de la entidad devolverán una Lista.
    Listas<Pedido> findAll();

    // Los métodos de lectura de un objeto de la entidad devolverán un objeto de la entidad.
    // Buscaremos el objeto por su ID, el identificador único de cada objeto en la BBDD.
    Optional<Pedido> findById(Long id);

    // Buscamos el objeto por un parámetro llave que sea único. Usamos Optional para que no lance excepciones.
    Optional<Pedido> findOne(String key);

    // Los métodos de escritura devolverán un booleano para indicar si la operación se ha realizado correctamente.
    // Manejamos la creación y la actualización de un objeto de la entidad como un método único.
    // Si el objeto no existe, se creará, si existe, se actualizará.
    // Los métodos por separado serían create() y update().
    boolean save(Pedido articulo);

    // Borrar un objeto de la entidad identificado por su id.
    boolean delete(Long id);

    // Contar el número de registros de la tabla.
    int count();

    // Recuperar el último elemento de la tabla.
    Optional<Pedido> getLast();

    // Comprobar si la tabla está vacía.
    boolean isEmpty();

    // Creamos un método estático para resetear los contadores de las tablas.
    boolean resetId();
}
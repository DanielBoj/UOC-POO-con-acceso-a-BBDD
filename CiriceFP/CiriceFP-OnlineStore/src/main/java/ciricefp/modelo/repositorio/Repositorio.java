package ciricefp.modelo.repositorio;

import ciricefp.modelo.listas.Listas;

/**
 * La interfaz Repositorio es la interfaz base de todos los repositorios.
 * Esta definirá el comportamiento de todas las entidades de la aplicación y
 * ayuda a conectar el modelo con la base de datos.
 * Como queremos utilizarla como interfaz para todas las entidades, usaremos el
 * tipo genérico T para que pueda ser reutilizada por todas.
 * Nos basaremos en la implementación de lso métodos CRUD para la gestión de los datos.
 * Usaremos los nombres usuales de la implementación de los métodos CRUD en inglés.
 *
 * @param <T> Tipo genérico para la entidad.
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public interface Repositorio<T> {

    // Los métodos de lectura de todos los objetos de la entidad devolverán una Lista.
    Listas<T> findAll();

    // Los métodos de lectura de un objeto de la entidad devolverán un objeto de la entidad.
    // Buscaremos el objeto por su ID, el identificador único de cada objeto en la BBDD.
    T findById(Long id);

    // Buscamos el objeto por un parámetro llave que sea único.
    T findOne(String key);

    // Los métodos de escritura devolverán un booleano para indicar si la operación se ha realizado correctamente.
    // Manejamos la creación y la actualización de un objeto de la entidad como un método único.
    // Si el objeto no existe, se creará, si existe, se actualizará.
    // Los métodos por separado serían create() y update().
    void save(T t);

    // Borrar un objeto de la entidad identificado por su id.
    void delete(Long id);

    // Contar el número de registros de la tabla.
    int count();

    // Recuperar el último elemento de la tabla.
    T getLast();

    // Creamos un método estático para resetear los contadores de las tablas.
    boolean resetId();
}

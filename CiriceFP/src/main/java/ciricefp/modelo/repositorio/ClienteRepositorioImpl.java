package ciricefp.modelo.repositorio;

import ciricefp.modelo.*;
import ciricefp.modelo.interfaces.factory.IClienteFactory;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.utils.Conexion;
import ciricefp.modelo.utils.ConexionJpa;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Esta clase implementa la interfaz Repositorio para nuestra entidad Cliente.
 * Esta clase será la encargada de gestionar los datos de la entidad Cliente.
 * Debe implementar todos los métodos de la interfaz Repositorio.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public class ClienteRepositorioImpl implements Repositorio<Cliente> {
    // Producto 4 -> Refactorizamos la clase para usar Entity Manager.
    // Creamos el atributo para nuestro Entity Manager.
    private final EntityManager em;

    // Seteamos nuestro Entity Manager a través del constructor.
    public ClienteRepositorioImpl(EntityManager em) {
        this.em = em;
    }

    /* Producto 4 ≥ Refactoriazamos la clase para trabajar con Hibernate */

    /* Simplificamos al máximo la implementación de nuestro contrato con la interfaz ya que trasladaremos
     * toda la lógica a un servicio, para cumplir con las buenas prácticas. */

    @Override
    public Listas<Cliente> findAll() {
        // Creamos la lista de Clientes que recibiremos de la BD.
        Listas<Cliente> clientes = new Listas<>();

        // Reseteamos el número de clientes.
        Cliente.resetTotalClientes();

        // Producto 4 -> Refactorizamos el método para usar Entity Manager.
        // Creamos la sentencia para la consulta.
        // Solucionamos el problema con los ids.
        // Añadimos el id al array.
        List<Long> ids = em.createQuery("select c.id from Cliente c", Long.class).getResultList();

        // Creamos un índice de posición que podamos usar dentro de la lambda.
        int[] index = {0};
        // Como usamos listas personalizadas, no podemos usar getResultList() y debemos usar getResultStream().
        em.createQuery("select c from Cliente c", Cliente.class).getResultStream().forEach(
                cliente -> {
                    // Seteamos el id correcto.
                    cliente.setId(ids.get(index[0]));
                    // Avanzamos el contador de id.
                    index[0]++;
                    // Añadimos el cliente a la lista.
                    clientes.add(cliente);

                    // Avanzamos el contador de clientes.
                    Cliente.advanceTotalClientes();
                }
        );

        // Devolvemos la lista de clientes.
        return clientes;
    }

    @Override
    public Cliente findById(Long id) {
        // Producto 4 -> Refactorizamos el método para usar Entity Manager.
        // Tenemos que solucioanr el problema de los ID
        // Creamos las consultas usando lenguaje HQL/JPQL.
        Long srcId = em.createQuery("select c.id from Cliente c where c.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        Cliente src = em.find(Cliente.class, id);
         // Solucionamos el problema con los ids.
        src.setId(srcId);

        return src;
    }

    @Override
    public Cliente findOne(String key) {
        // Producto 4 -> Refactorizamos el método para usar Entity Manager.
        // Como find solo busca por Id, debemos crear una consulta HQL/JPQL.
        // Solucionamos el problema con los ids.
        Long id = em.createQuery("select c.id from Cliente c where c.nif = :key", Long.class)
                .setParameter("key", key)
                .getSingleResult();
        Cliente src = em.find(Cliente.class, id);
        // Solucionamos el problema con los ids.
        src.setId(id);

        return src;
    }

    /* Al estar guardando un modelo de objeto complejo, realizaremos una inserción por pasos.
    * Primero, insertaremos los datos de la tabla padre de las entidades Cliente.
    * Después, insertaremos los datos de la tabla hija de las entidades Cliente.
    * A la vez, tenemos que tener en cuenta el manejo de la Dirección, que también es una entidad.
    */
    /* Producto 4 ≥ Ahora Hibernate debería manejar la inserción de los datos de la tabla padre.
     */
    @Override
    public void save(Cliente cliente) {
        // Producto 4 -> Refactorizamos el método para usar Entity Manager.
        // Comenzamos por determinar si es un CREATE o un UPDATE.
        // Para ello, comprobamos si el id es null o no.
        if (cliente.getId() != null && cliente.getId() > 0) {
            // Si el id es mayor que 0, significa que ya existe en la BD.
            // Por lo tanto, ejecutaremos un Update.
            em.merge(cliente);
        } else {
            // Si el id es null o 0, significa que no existe en la BD.
            // Por lo tanto, ejecutaremos un Create.
            em.persist(cliente);
        }
    }

    /* Producto 4 -> Al usar Hibernate, se realizará la eliminación de forma automática.
    * mediante los decoradores que hemos añádido en las clases implicadas.
    */
    @Override
    public void delete(Long id) {
        // Producto 4 ≥ Implementamos el método mediante Entity Manager.
        em.remove(em.find(Cliente.class, id));
    }

    @Override
    public int count() {
        // Producto 4 ≥ Implementamos el método mediante Entity Manager.
        // Creamos la consulta usando lenguaje HQL/JPQL. Si no hay ningún artículo nos devolverá 0.
        return em.createQuery("select count(c) from Cliente c", Long.class)
                // retornamos un único valor.
                .getSingleResult()
                // convertimos el resultado a int.
                .intValue();
    }

    @Override
    public Cliente getLast() {
        // Producto 4 ≥ Implementamos el método mediante Entity Manager.
        // Creamos la consulta usando lenguaje HQL/JPQL.
        // Obtenemos el último artículo de la BD recibiendo el primer resultado de la consulta
        // ordenada de forma descendente por el id.
        // Solucionamos el problema con los ids.
        Long id = em.createQuery("select c.id from Cliente c order by c.id desc", Long.class)
                // retornamos un único valor.
                .setMaxResults(1)
                // obtenemos el resultado.
                .getSingleResult();
        Cliente src = em.find(Cliente.class, id);
        // Solucionamos el problema con los ids.
        src.setId(id);

        return src;
    }

    @Override
    public boolean resetId() {
        // Producto 4 ≥ Implementamos el método mediante Entity Manager.
        // Creamos la consulta usando el método de Hibernate JPA para llamar a un procedimiento almacenado.
        // Si el procedimiento se ejecuta correctamente, nos devolverá true.
        return em.createStoredProcedureQuery("reset_id_clientes")
                .executeUpdate() > 0;
    }

    /* Producto 4 ≥ Ya no necesitamos métodos auxiliares para mapear los resultados de las consultas porque lo
    realiza automáticamente el framework. */
}

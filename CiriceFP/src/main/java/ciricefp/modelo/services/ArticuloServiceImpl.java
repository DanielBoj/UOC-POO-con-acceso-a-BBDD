package ciricefp.modelo.services;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.repositorio.ArticuloRepositorioImpl;
import ciricefp.modelo.repositorio.Repositorio;
import ciricefp.modelo.services.interfaces.ClienteService;
import jakarta.persistence.EntityManager;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Esta clase implementa toda la lógica del contrato con la interface de servicios.
 * Los servicios sirven para aislar la lógica de negocio de nuestras clases DAO.
 *
 * @author Cirice
 * @version 1.0
 * @since 05-2023
 */
public class ClienteServiceImpl implements ClienteService {
    // Empezamos por realizar la conexión con el repositorio para poder realizar las acciones sobre la capa de datos.
    // Necesitamos acceso al entity manejar para poder manejar las transacciones con la BBDD.
    private EntityManager em;
    // Creamos el repositorio para poder realizar el manejo de los datos y le pasamos el entity manager.
    private Repositorio<Articulo> repositorio;

    public ClienteServiceImpl(EntityManager em) {
        this.em = em;

        // Aquí tenemos que concretar la implementación de Repositorio que vamos a usar.
        this.repositorio = new ArticuloRepositorioImpl(em);
    }

    /* Implementamos los métodos de la interface de servicios. */
    @Override
    public Listas<Articulo> findAll() {
        // Listar no requiere transacción
        try {
            return repositorio.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Listas<Articulo>();
        }
    }

    @Override
    public Articulo findById(Long id) {
        // Como es un método GET, no requieren transacción.
        try {
            return repositorio.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Articulo> findOne(String key) {
        // Los métodos GET no necesitan transacción, pero en este caso, hay que manejar el retorno opcional.
        // Manejamos la excepción directamente con Optional.
        return Optional.ofNullable(repositorio.findOne(key));

    }

    @Override
    public boolean save(Articulo articulo) {
        // Los métodos de escritura requieren transacción.
        // Creamos la consulta. Retornamos un Stream de Articulos ya que nuestra clase List está personalizada
        // y no podemos usar directamente el método getResultList().
        try {
            // Iniciamos la transacción
            em.getTransaction().begin();

            // Guardamos el artículo
            repositorio.save(articulo);

            // Salvamos los cambios mediante el commit
            em.getTransaction().commit();

            return true;
        } catch (Exception e) {
            System.out.println(MessageFormat.format("Error al guardar el artículo {0}", articulo.getDescripcion()));
            // Realizamos el rollback
            em.getTransaction().rollback();

            e.printStackTrace();
        } finally {
            // Cerramos la conexión
            em.close();
        }

        // Si la operación ha producido un error, retornamos false.
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public Optional<Articulo> getLast() {
        return Optional.empty();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean resetId() {
        return false;
    }
}

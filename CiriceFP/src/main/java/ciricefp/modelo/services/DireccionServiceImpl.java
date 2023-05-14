package ciricefp.modelo.services;

import ciricefp.modelo.Direccion;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.repositorio.DireccionRepositorioImpl;
import ciricefp.modelo.repositorio.Repositorio;
import ciricefp.modelo.services.interfaces.DireccionService;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Esta clase implementa toda la lógica del contrato con la interface de servicios.
 * Los servicios sirven para aislar la lógica de negocio de nuestras clases DAO.
 * Realizaremos también el manejo de excepciones en esta clase.
 *
 * @author Cirice
 * @version 1.0
 * @since 05-2023
 */
public class DireccionServiceImpl implements DireccionService {

    // Empezamos por realizar la conexión con el repositorio para poder realizar las acciones sobre la capa de datos.
    // Necesitamos acceso al entity manejar para poder manejar las transacciones con la BBDD.
    private final EntityManager em;
    // Creamos el repositorio para poder realizar el manejo de los datos y le pasamos el entity manager.
    private final Repositorio<Direccion> repositorio;

    // Constructor
    public DireccionServiceImpl(EntityManager em) {
        this.em = em;

        // Aquí tenemos que concretar la implementación de Repositorio que vamos a usar.
        this.repositorio = new DireccionRepositorioImpl(em);
    }

    /* Implementamos los métodos de la interface de servicios. */
    @Override
    public Listas<Direccion> findAll() {
        // Listar no requiere transacción
        try {
            return repositorio.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Listas<>();
        }
    }

    @Override
    public Optional<Direccion> findById(Long id) {
        // Como es un método GET, no requieren transacción.
        // Manejamos la excepción con Optional.
        return Optional.ofNullable(repositorio.findById(id));
    }

    @Override
    public Optional<Direccion> findOne(@NotNull String key) {
        // Como dirección no tiene ningún valor único además del id, no implementamos este método.
        return Optional.empty();
    }

    @Override
    public boolean save(@NotNull Direccion direccion) {
        // Los métodos de escritura requieren transacción.
        // Creamos la consulta.
        try {
            // Iniciamos la transacción
            em.getTransaction().begin();

            // Guardamos el artículo
            repositorio.save(direccion);

            // Salvamos los cambios mediante el commit
            em.getTransaction().commit();

            return true;
        } catch (Exception e) {
            System.out.println(MessageFormat.format("Error al guardar la dirección {0}", direccion.toString()));
            // Realizamos el rollback
            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            e.printStackTrace();

            // Si la operación ha producido un error, retornamos false.
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        // Llamamos al método del DAO, en este caso queremos eliminar una dirección por su Id.
        // Para ello usaremos el método remove() de la clase EntityManager.
        try {
            // Como es una acción de escritura, iniciamos una transacción.
            em.getTransaction().begin();

            // Eliminamos el artículo.
            repositorio.delete(id);

            // Confirmamos la transacción.
            em.getTransaction().commit();

            // Si la operación ha ido bien, devolvemos true.
            return true;
        } catch (Exception e) {
            System.out.println(MessageFormat.format("No es posible eliminar el artículo con id {0}", id));

            // Realizamos un rollback en caso de error.
            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            e.printStackTrace();

            // Si la operación ha producido un error, retornamos false.
            return false;
        }
    }

    @Override
    public int count() {
        // Como es un método GET, no requieren transacción.
        // Creamos un valor de retorno por defecto por si sucede algún error.
        int defaultValue = 0;

        try {
            // Llamamos al método del repositorio, es un método de lectura así que no necesitamos transacción.
            return repositorio.count();
        } catch (Exception e) {
            System.out.println("No existen direcciones en la Base de Datos.");
            e.printStackTrace();

            // Si la operación ha producido un error, retornamos el valor por defecto.
            return defaultValue;
        }
    }

    @Override
    public Optional<Direccion> getLast() {
        // Como es un método GET, no requieren transacción.
        // Manejamos las excepciones mediante Optional.
        return Optional.ofNullable(repositorio.getLast());
    }

    // Podemos llamar directamente al método
    @Override
    public boolean isEmpty() { return count() == 0; }

    @Override
    public boolean resetId() {
        // LLamamos al método del repositorio, es un método de escritura así que
        // necesitamos una transacción.
        try {
            // Como es una acción de escritura, iniciamos una transacción.
            em.getTransaction().begin();

            // Ejecutamos el procedimiento y asignamos el resultado a un flag.
            boolean res = repositorio.resetId();

            // Confirmamos la transacción.
            em.getTransaction().commit();

            // Devolvemos true.
            return res;
        } catch (Exception e) {
            System.out.println("No es posible resetear el contador de la tabla.");

            // Realizamos un rollback en caso de error.
            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            e.printStackTrace();

            // Si la operación ha producido un error, retornamos false.
            return false;
        }
    }
}
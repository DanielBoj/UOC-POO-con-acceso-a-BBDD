package ciricefp.modelo.services;

import ciricefp.modelo.Pedido;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.repositorio.PedidoRepositorioImpl;
import ciricefp.modelo.repositorio.Repositorio;
import ciricefp.modelo.services.interfaces.PedidoService;
import jakarta.persistence.EntityManager;

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
public class PedidoServiceImpl implements PedidoService {
    // Empezamos por realizar la conexión con el repositorio para poder realizar las acciones sobre la capa de datos.
    // Necesitamos acceso al entity manejar para poder manejar las transacciones con la BBDD.
    private final EntityManager em;
    // Creamos el repositorio para poder realizar el manejo de los datos y le pasamos el entity manager.
    private final Repositorio<Pedido> repositorio;

    // Constructor
    public PedidoServiceImpl(EntityManager em) {
        this.em = em;

        // Aquí tenemos que concretar la implementación de Repositorio que vamos a usar.
        this.repositorio = new PedidoRepositorioImpl(em);
    }

    /* Implementamos los métodos de la interface de servicios. */
    @Override
    public Listas<Pedido> findAll() {
        // Listar no requiere transacción
        try {
            return repositorio.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Listas<>();
        }
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        // findById no requiere transacción porque es un método GET.
        // Manejamos la excepción con Optional.
        return Optional.ofNullable(repositorio.findById(id));
    }

    @Override
    public Optional<Pedido> findOne(String key) {
        // Los métodos GET no necesitan transacción, pero en este caso, hay que manejar el retorno opcional.
        // Manejamos la excepción directamente con Optional.
        return Optional.ofNullable(repositorio.findOne(key));
    }

    @Override
    public boolean save(Pedido articulo) {
        // Los métodos de escritura POST, PUT y DELETE requieren transacción.
        // Manejamos la excepción con Optional.
        try {
            // Iniciamos la transacción
            em.getTransaction().begin();

            // Guardamos el articulo
            repositorio.save(articulo);

            // Hacemos commit
            em.getTransaction().commit();

            // Devolvemos true
            return true;
        } catch (Exception e) {
            System.out.println(MessageFormat.format("Error al guardar el pedido {0}", articulo.getNumeroPedido()));

            // Hacemos rollback
            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            e.printStackTrace();

            // Devolvemos false
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        // Las acciones de escritura usan una transacción.
        try {
            // Iniciamos la transacción
            em.getTransaction().begin();

            // Eliminamos el pedido
            repositorio.delete(id);

            // Hacemos commit
            em.getTransaction().commit();

            // Como no ha habido errores, devolvemos true.
            return true;
        } catch (Exception e) {
            System.out.println(MessageFormat.format("Error al eliminar el pedido con id {0}", id));

            // Hacemos rollback
            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            e.printStackTrace();

            // Como ha habido un error, devolvemos false.
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
            System.out.println("No existen pedidos en la Base de Datos.");
            e.printStackTrace();

            // Si ha habido algún error, devolvemos el valor por defecto.
            return defaultValue;
        }
    }

    @Override
    public Optional<Pedido> getLast() {
        // Como es un método GET, no requieren transacción.
        // Manejamos las excepciones mediante Optional.
        return Optional.ofNullable(repositorio.getLast());
    }

    // Podemos llamar directamente al método.
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
            return false;
        }
    }
}
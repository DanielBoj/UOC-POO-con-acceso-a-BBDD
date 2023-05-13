package ciricefp.modelo.services;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.Pedido;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.repositorio.ArticuloRepositorioImpl;
import ciricefp.modelo.repositorio.PedidoRepositorioImpl;
import ciricefp.modelo.repositorio.Repositorio;
import ciricefp.modelo.services.interfaces.ArticuloService;
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
public class ArticuloServiceImpl implements ArticuloService {
    // Empezamos por realizar la conexión con el repositorio para poder realizar las acciones sobre la capa de datos.
    // Necesitamos acceso al entity manejar para poder manejar las transacciones con la BBDD.
    private final EntityManager em;
    // Creamos el repositorio para poder realizar el manejo de los datos y le pasamos el entity manager.
    private final Repositorio<Articulo> repositorio;

    // Constructor
    public ArticuloServiceImpl(EntityManager em) {
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
            return new Listas<>();
        }
    }

    @Override
    public Optional<Articulo> findById(Long id) {
        // Como es un método GET, no requieren transacción.
        // Manejamos la excepción con Optional.
        return Optional.ofNullable(repositorio.findById(id));
    }

    @Override
    public Optional<Articulo> findOne(@NotNull  String key) {
        // Los métodos GET no necesitan transacción, pero en este caso, hay que manejar el retorno opcional.
        // Manejamos la excepción directamente con Optional.
        return Optional.ofNullable(repositorio.findOne(key));
    }

    @Override
    public boolean save(@NotNull Articulo articulo) {
        // Los métodos de escritura requieren transacción.
        // Creamos la consulta.
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
            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            e.printStackTrace();

            // Si la operación ha producido un error, retornamos false.
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        // Si el artículo aparece en algún pedido, no podrá borrarse.
        // Creamos un repositorio para buscar los pedidos.
        Repositorio<Pedido> repoPedidos = new PedidoRepositorioImpl(em);

        // Comprobamos si el artículo aparece en algún pedido: La función podría escribirse en una
        // única línea, pero la expandimos para explicarla.
        // Obtenemos la lista de pedidos.
        if (repoPedidos.findAll().getLista()
                // La convertimos en un stream.
                .stream()
                // Comprobamos si el artículo aparece en algún pedido a través de su Id.
                .anyMatch(p -> p.getArticulo().getId().equals(id))) {
            System.out.println(MessageFormat.format("No es posible eliminar el artículo con id {0} porque aparece en algún pedido.", id));
            return false;
        }

        // Llamamos al método del DAO, en este caso queremos eliminar un artículo por su Id.
        // Para ello usaremos el método remove() de la clase EntityManager.
        try {
            // Como es una acción de escritura, iniciamos una transacción.
            em.getTransaction().begin();

            // Eliminamos el artículo.
            repositorio.delete(id);

            // Retrocedemos el contador de artículos.
            Articulo.retrocederContador();

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
            System.out.println("No existen artículos en la Base de Datos.");
            e.printStackTrace();

            // Si la operación ha producido un error, retornamos el valor por defecto.
            return defaultValue;
        }
    }

    @Override
    public Optional<Articulo> getLast() {
        // Como es un método GET, no requieren transacción.
        // Manejamos las excepciones mediante Optional.
        return Optional.ofNullable(repositorio.getLast());
    }

    // Podemos llamar directament al método.
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

package ciricefp.modelo.services;

import ciricefp.modelo.Cliente;
import ciricefp.modelo.Pedido;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.repositorio.ClienteRepositorioImpl;
import ciricefp.modelo.repositorio.PedidoRepositorioImpl;
import ciricefp.modelo.repositorio.Repositorio;
import ciricefp.modelo.services.interfaces.ClienteService;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {
    // Empezamos por realizar la conexión con el repositorio para poder realizar las acciones sobre la capa de datos.
    // Necesitamos acceso al entity manejar para poder manejar las transacciones con la BBDD.
    private final EntityManager em;
    // Creamos el repositorio para poder realizar el manejo de los datos y le pasamos el entity manager.
    private final Repositorio<Cliente> repositorio;

    // Constructor
    public ClienteServiceImpl(EntityManager em) {
        this.em = em;

        // Aquí tenemos que concretar la implementación de Repositorio que vamos a usar.
        this.repositorio = new ClienteRepositorioImpl(em);
    }

    /* Implementamos los métodos de la interface de servicios. */
    @Override
    public Listas<Cliente> findAll() {
        // Listar es un método GET, no requieren transacción.
        try {
            return repositorio.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Listas<>();
        }
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        // Como es un método GET, no requieren transacción.
        // Manejamos la excepción con Optional.
        return Optional.ofNullable(repositorio.findById(id));
    }

    @Override
    public Optional<Cliente> findOne(@NotNull String key) {
        // Como es un método GET, no requieren transacción.
        // Manejamos la excepción con Optional.
        return Optional.ofNullable(repositorio.findOne(key));
    }

    @Override
    public boolean save(@NotNull Cliente cliente) {
        // Como es un método de escritura POST, requiere transacción.
        // Creamos la consulta
        try {
            // Iniciamos la transacción.
            em.getTransaction().begin();

            // Guardamos el cliente
            repositorio.save(cliente);

            // Hacemos commit
            em.getTransaction().commit();

            // Si la operación ha ido bien, devolvemos true
            return true;
        } catch (Exception e) {
            System.out.println(MessageFormat.format("Error al guardar el cliente {0}", cliente.getNif()));

            // Realizamos el rollback
            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            e.printStackTrace();

            // Si ha habido un error, devolvemos false
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        // Si el cliente aparece en algún pedido, no se podrá eliminar.
        // Creamos el repositorio para consultar los pedidos
        Repositorio<Pedido> pedidoRepo = new PedidoRepositorioImpl(em);

        if (pedidoRepo.findAll().getLista().stream()
                .anyMatch(ped -> ped.getCliente().getId().equals(id)) ||
                findById(id).isEmpty()) {
            System.out.println("No se puede eliminar el cliente porque está asociado a un pedido o no existe.");
            return false;
        }

        // Como es un método de escritura DELETE, requiere transacción.
        // Creamos la consulta
        try {
            // Iniciamos la transacción.
            em.getTransaction().begin();

            // Eliminamos el cliente
            repositorio.delete(id);

            // Hacemos commit
            em.getTransaction().commit();

            // Si la operación ha ido bien, devolvemos true
            return true;
        } catch (Exception e) {
            System.out.println(MessageFormat.format("Error al eliminar el cliente con id {0}", id));

            // Realizamos el rollback
            if (em.getTransaction().isActive()) em.getTransaction().rollback();

            e.printStackTrace();

            // Si ha habido un error, devolvemos false
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
            System.out.println("No existen clientes en la Base de Datos.");
            e.printStackTrace();

            // Si la operación ha producido un error, retornamos el valor por defecto.
            return defaultValue;
        }
    }

    @Override
    public Optional<Cliente> getLast() {
        // Como es un método GET, no requieren transacción.
        // Manejamos las excepciones mediante Optional.
        return Optional.ofNullable(repositorio.getLast());
    }

    // Podemos llamar directamente al método.
    @Override
    public boolean isEmpty() {
        return count() == 0;
    }

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
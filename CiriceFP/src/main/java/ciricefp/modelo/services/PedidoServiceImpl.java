package ciricefp.modelo.services;

import ciricefp.modelo.Pedido;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.repositorio.PedidoRepositorioImpl;
import ciricefp.modelo.repositorio.Repositorio;
import ciricefp.modelo.services.interfaces.PedidoService;
import jakarta.persistence.EntityManager;

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
        return null;
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Pedido> findOne(String key) {
        return Optional.empty();
    }

    @Override
    public boolean save(Pedido articulo) {
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
    public Optional<Pedido> getLast() {
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

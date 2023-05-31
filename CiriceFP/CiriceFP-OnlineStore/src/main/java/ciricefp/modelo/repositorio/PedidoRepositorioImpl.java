package ciricefp.modelo.repositorio;

import ciricefp.modelo.Pedido;
import ciricefp.modelo.listas.Listas;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.util.List;

/**
 * Esta clase implementa la interfaz Repositorio para nuestra entidad Pedido.
 * Esta clase será la encargada de gestionar los datos de la entidad Pedido.
 * Debe implementar todos los métodos de la interfaz Repositorio.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public class PedidoRepositorioImpl implements Repositorio<Pedido> {
    // Producto 4 -> Refactorizamos la clase para usar Entity Manager.
    // Creamos el atributo para nuestro Entity Manager.
    private final EntityManager em;

    // Seteamos nuestro Entity Manager a través del constructor.
    public PedidoRepositorioImpl(EntityManager em) {
        this.em = em;
    }

    /* Producto 4 ≥ Refactoriazamos la clase para trabajar con Hibernate */

    /* Simplificamos al máximo la implementación de nuestro contrato con la interfaz ya que trasladaremos
     * toda la lógica a un servicio, para cumplir con las buenas prácticas. */

    /* MÉTODOS DE LECTURA
    * Como la entidad Pedido, por sus relaciones/asociaciones con otras entidades, presenta
    * una estructura compleja, debemos crear métodos auxiliares para la lectura de los datos que
    * aprovecharán los métodos de la interfaz Repositorio para las entidades asociadas en el modelo
    * POO.
    *
    * Esto significa que las implementaciones serán distintas de las vistas en clientes donde lo
    * manejábamos a través de la sentencia SQL mediante JOIN.
    *
    * El modelo de respuesta que recibiremos es: _id, numero_pedido, cliente_id, articulo_id, unidades,
    * fecha_pedido, es_enviado
    * Así que podemos usar los identificadores del cliente y del articulo para obtener directamente una entidad
    * de la base de datos y asociarla a nuestro objeto Pedido en el modelo POO.
     */
    @Override
    public Listas<Pedido> findAll() {

        // Creamos la lista que contendrá los pedidos.
        Listas<Pedido> pedidos = new Listas<>();

        // Producto 4 -> Realizamos la consulta a través de JPA.
        em.createQuery("select p from Pedido p", Pedido.class).getResultList().forEach(pedidos::add);

        // Devolvemos la lista de pedidos.
        return pedidos;
    }

    @Override
    public Pedido findById(Long id) {
        // Producto 4 -> Realizamos la consulta a través de un método de JPA.
        return em.find(Pedido.class, id);
    }

    @Override
    public Pedido findOne(String key) {
        System.out.println("Buscando pedido por número de pedido: " + key);
        // Producto 4 -> Realizamos la consulta a través de un query HQL/JPQL.
        // Llamamos a un procedimiento almacenado para obtener el pedido por su número de pedido que nos devuelve el pedido.
        StoredProcedureQuery query = em.createStoredProcedureQuery("get_pedido_by_numero_pedido", Pedido.class)
            .registerStoredProcedureParameter("_numero_pedido", Integer.class, ParameterMode.IN)
            .setParameter("_numero_pedido", Integer.parseInt(key));

        // Usamos una lista para manejar un posible retorno vacío.
        List<Pedido> pedidos = (List<Pedido>) query.getResultList();

        return pedidos.size() > 0 ? pedidos.get(0) : null;
    }

    @Override
    public void save(Pedido pedido) {
        // Producto 4 ≥ Manejamos la petición con Entity Manager.
        // Comenzaremos por determinar si tenemos que ejecutar una acción Create o un Update.
        // Para ello, comprobaremos si el artículo tiene un id asignado que funcionará como un flag.
        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        if (pedido.getId() != null && pedido.getId() > 0) {
            // Si es así, actualizamos el pedido.
            // Para este ejemplo usaremos el procedimiento almacenado para demostrar cómo se hace.
            // En la práctica, se recomienda usar un método HQL/JPQL a no ser que necesitemos restringir la seguridad
            // de forma explícita a usuarios que solo puedan ejecutar procedimientos.
            // Cómo el primer parámetro del procedimiento es de salida, tenemos que registrarlo.
            em.merge(pedido);
        } else {
            System.out.println(pedido);
            // Si no, creamos un nuevo pedido.
            em.persist(pedido);
        }
    }

    @Override
    public void delete(Long id) {
        // Producto 4 ≥ Manejamos la petición con Entity Manager.
        // Buscamos el pedido en la misma llamada a la función ya que hemos de pasar un objeto al método remove.
        em.createStoredProcedureQuery("delete_pedido")
                .registerStoredProcedureParameter("result", Long.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("id", Long.class, ParameterMode.IN)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public int count() {
        // Producto 4 ≥ Manejamos la petición con Entity Manager.
        // Creamos la consulta usando lenguaje HQL/JPQL. Si no hay ningún artículo nos devolverá 0.
        return em.createQuery("select count(p) from Pedido p", Long.class)
                // retornamos un único valor.
                .getSingleResult()
                // convertimos el resultado a int.
                .intValue();
    }

    @Override
    public Pedido getLast() {
        // Producto 4 ≥ Manejamos la petición con Entity Manager.
        // Creamos la consulta usando lenguaje HQL/JPQL.
        // Obtenemos el último artículo de la BD recibiendo el primer resultado de la consulta
        // ordenada de forma descendente por el id.
        return em.createQuery("select p from Pedido p order by p.id desc", Pedido.class)
                // retornamos un único valor.
                .setMaxResults(1)
                // obtenemos el resultado.
                .getSingleResult();
    }

    @Override
    public boolean resetId() {
        // Producto 4 ≥ Manejamos la petición con Entity Manager.
        // Creamos la consulta usando el método de Hibernate JPA para llamar a un procedimiento almacenado.
        // Si el procedimiento se ejecuta correctamente, nos devolverá true.
        return em.createStoredProcedureQuery("reset_id_pedidos")
                .executeUpdate() > 0;
    }

     /* Producto 4 ≥ Ya no necesitamos métodos auxiliares para mapear los resultados de las consultas porque lo
    realiza automáticamente el framework. */
}

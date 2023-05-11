package ciricefp.modelo.repositorio;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.listas.Listas;
import jakarta.persistence.EntityManager;



/**
 * Esta clase implementa la interfaz Repositorio para nuestra entidad Articulo.
 * Esta clase será la encargada de gestionar los datos de la entidad Articulo.
 * Debe implementar todos los métodos de la interfaz Repositorio.
 * El manejo de excepciones se realizará en la clase de servicios.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public class ArticuloRepositorioImpl implements Repositorio<Articulo> {

    // Producto 4 -> Refactorizamos la clase para usar Entity Manager.
    // Creamos el atributo para nuestro Entity Manager.
    private EntityManager em;

    // Seteamos nuestro Entity Manager a través del constructor.
    public ArticuloRepositorioImpl(EntityManager em) {
        this.em = em;
    }

    /* Producto 4 ≥ Refactoriazamos la clase para trabajar con Hibernate */

    /* Simplificamos al máximo la implementación de nuestro contrato con la interfaz ya que trasladaremos
    * toda la lógica a un servicio, para cumplir con las buenas prácticas. */

    // Obtenemos un listado de todos los artículos
    @Override
    public Listas<Articulo> findAll() {

        // Creamos la lista de Articulos que recibiremos de la BD.
        Listas<Articulo> articulos = new Listas<>();

        // Creamos la query usando el lenguaje de consultas de JPA.
        em.createQuery("Select a from Articulo a", Articulo.class)
                // Obtenemos el resultado de la consulta y lo convertimos en un stream.
                .getResultStream().forEach(
                // Pasamos el resultado a la lista de Articulos.
                articulos::add
        );

        // Retornamos la lista de Articulos.
        return articulos;
    }

    @Override
    public Articulo findById(Long id) {
        // Creamos la consulta con los métodos de Hibernate JPA.
        return em.find(Articulo.class, id);
    }

    @Override
    public Articulo findOne(String key) {
        // Creamos la consulta con los métodos de Hibernate JPA.
        return em.createQuery("select a from Articulo a where a.cod_articulo = :codigo", Articulo.class)
                // Asignamos el parámetro a la consulta.
                .setParameter("codigo", key)
                // Limitamos el resultado a un solo objeto.
                .setMaxResults(1)
                // Ejecutamos la consulta.
                .getSingleResult();
    }

    @Override
    public void save(Articulo articulo) {
        // Comenzamos por discriminar si se trata de un Create o un Update.
        // Para ello, comprobaremos si el artículo tiene un id asignado que funcionará como un flag.
        if (articulo.getId() != null && articulo.getId() > 0) {
            // Si el id es mayor que 0, significa que ya existe en la BD.
            // Por lo tanto, ejecutaremos un Update.
            em.merge(articulo);
        } else {
            // Si el id es menor o igual a 0, significa que no existe en la BD.
            // Por lo tanto, ejecutaremos un Create.
            em.persist(articulo);
        }
    }

    @Override
    public void delete(Long id) {
        // Creamos la consulta usando métodos de Hibernate JPA.
        // Usaremos el método remoce() para eliminar el artículo que recibe como argumento el artículo que queremos eliminar.
        // Tenemos que buscar el artículo por su Id, por lo que usaremos el método find() de la clase EntityManager.
        // Una vez localizado, eliminaremos el elemento.
        em.remove(em.find(Articulo.class, id));

        // TODO -> Pasar lógica a Servicio

        /*Repositorio<Pedido> pedidoRepo = new PedidoRepositorioImpl();
        if (pedidoRepo.findAll().getLista().stream()
            .anyMatch(ped -> ped.getArticulo().getId().equals((id)))) {
            System.out.println("No es posible eliminar el artículo porque está en algún pedido.");
            return false;
        }*/
    }

    @Override
    public int count() {
        // Creamos la consulta usando lenguaje HQL/JPQL. Si no hay ningún artículo nos devolverá 0.
        return em.createQuery("select count(a) from Articulo a", Long.class)
                // retornamos un único valor.
                .getSingleResult()
                // convertimos el resultado a int.
                .intValue();
    }


    @Override
    public Articulo getLast() {
        // Creamos la consulta usando lenguaje HQL/JPQL.
        // Obtenemos el último artículo de la BD recibiendo el primer resultado de la consulta
        // ordenada de forma descendente por el id.
        return em.createQuery("select a from Articulo a order by a.id desc", Articulo.class)
                // retornamos un único valor.
                .setMaxResults(1)
                // obtenemos el resultado.
                .getSingleResult();
    }

    @Override
    public boolean resetId() {
        // Creamos la consulta usando el método de Hibernate JPA para llamar a un procedimiento almacenado.
        // Si el procedimiento se ejecuta correctamente, nos devolverá true.
        return em.createStoredProcedureQuery("reset_id_articulos")
                .execute();
    }

    /* Producto 4 ≥ Ya no necesitamos métodos auxiliares para mapear los resultados de las consultas porque lo
    realiza automáticamente el framework. */

    /*// Creamos un método para mapear los ResultSet. Lo vamos a usar únicamente dentro de la clase.
    // Recibe el ResultSet como parámetro.
    @NotNull
    private static Articulo getArticulo(ResultSet res) throws SQLException {

        // Creamos un nuevo artículo. Para que se vea más ordenado, lo pasaremos por los métodos setter.
        Articulo articulo = new Articulo();
        articulo.setId(res.getLong("_id"));
        articulo.setCodArticulo(res.getString("cod_articulo"));
        articulo.setDescripcion(res.getString("descripcion"));
        articulo.setPvp(res.getDouble("pvp"));
        articulo.setGastosEnvio(res.getDouble("gastos_envio"));
        articulo.setTiempoPreparacion(res.getInt("tiempo_preparacion"));

        return articulo;
    }

    // Mapeamos un statement a partir de un artículo.
    // No es una función pura, pero modifica únicamente una variable que usamos en un método.
    private static void getStatement(Articulo articulo, CallableStatement stmt) throws SQLException {

        // Asignamos los parámetros a la consulta desde el artículo que recibimos por parámetro.
        // La elección del parámetro se hace por su posición.
        // Preparamos el parámetro de salida.
        stmt.registerOutParameter(1, Types.NUMERIC);

        stmt.setString(2, articulo.getCodArticulo());
        stmt.setString(3, articulo.getDescripcion());
        stmt.setDouble(4, articulo.getPvp());
        stmt.setDouble(5, articulo.getGastosEnvio());
        stmt.setInt(6, articulo.getTiempoPreparacion());
    }*/
}

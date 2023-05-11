package ciricefp.modelo.repositorio;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.Pedido;
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

/**
 * Esta clase implementa la interfaz Repositorio para nuestra entidad Articulo.
 * Esta clase será la encargada de gestionar los datos de la entidad Articulo.
 * Debe implementar todos los métodos de la interfaz Repositorio.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public class ArticuloRepositorioImpl implements Repositorio<Articulo> {

    // Añadimos el atributo para importar los valores del archivo .env
    private static final Dotenv dotenv = Dotenv.load();

    // Producto 4 -> Refactorizamos la clase para usar Entity Manager.
    // Creamos el atributo para nuestro Entity Manager.
    private EntityManager em;

    // Seteamos nuestro Entity Manager a través del constructor.
    public ArticuloRepositorioImpl(EntityManager em) {
        this.em = em;
    }

    /* Producto 4 ≥ Refactoriazamos la clase para trabajar con Hibernate */

    /* Simplificamos al máximos la implementación de nuestro contrato con la interfaz ya que trasladaremos
    * toda la lógica a un servicio, para cumplir con las buensa prácticas. */

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
        em.remove(em.find(Articulo.class, id));

        // TODO -> Pasar lógica a Servicio


        // Si el articulo aparece en algún pedido, no se podrá eliminar.
        Repositorio<Pedido> pedidoRepo = new PedidoRepositorioImpl();
        if (pedidoRepo.findAll().getLista().stream()
            .anyMatch(ped -> ped.getArticulo().getId().equals((id)))) {
            System.out.println("No es posible eliminar el artículo porque está en algún pedido.");
            return false;
        }

        // Producto 4 ≥ Reimplementamos el método para usar Entity Manager.
        // Creamos la conexión
        EntityManager em = getEntityManager();

        // Creamos la consulta, en este caso queremos eliminar un artículo por su Id.
        // Para ello usaremos el método remove() de la clase EntityManager.
        // Tenemos que bsucar el artículo por su Id, por lo que usaremos el método find() de la clase EntityManager.
        // Una vez localizado, eliminaremos el elemento.
        try {
            em.remove(em.find(Articulo.class, id));

            // Retrocedemos el contador de artículos.
            Articulo.retrocederContador();

            // Si todo ha ido bien, devolvemos true.
            return true;
        } catch (Exception e) {
            System.out.println(MessageFormat.format("No es posible eliminar el artículo con id {0}", id));
            e.printStackTrace();
        } finally {
            // Cerramos la conexión
            em.close();
        }
    }

    @Override
    public int count() {

        // Creamos la consulta usando lenguaje HQL/JPQL.
        return em.createQuery("select count(a) from Articulo a", Long.class)
                // retornamos un único valor.
                .getSingleResult()
                // convertimos el resultado a int.
                .intValue();


        // Producto 4 ≥ Reimplementamos el método para usar Entity Manager.
        // Creamos la conexión
        EntityManager em = getEntityManager();

        // variable de resultado, la definimos primero para poder devolver 0 en caso de producirse un error.
        int defaultValue = 0;

        // Creamos la consulta, en este caso queremos obtener el número de artículos.
        // Para ello usaremos un query en JPQL de la clase EntityManager.
        try {
            return  em.createQuery("select count(a) from Articulo a", Long.class)
                    // retornamos un único valor.
                    .getSingleResult()
                    // convertimos el resultado a int.
                    .intValue();
        } catch (Exception e) {
            System.out.println("No es posible obtener el número de registros de la tabla.");
            e.printStackTrace();
        } finally {
            // Cerramos la conexión
            em.close();
        }

        // Si ha habido algún error, devolvemos 0.
        return defaultValue;
    }


    @Override
    public Articulo getLast() {

        // Creamos la consulta usando lenguaje HQL/JPQL.
        return em.createQuery("select a from Articulo a order by a.id desc", Articulo.class)
                // retornamos un único valor.
                .setMaxResults(1)
                // obtenemos el resultado.
                .getSingleResult();
    }

    @Override
    public boolean resetId() {
        // Creamos la consulta usaando el método de Hibernate JPA para llamar a un procedimiento almacenado.
        em.createStoredProcedureQuery("reset_id_articulos")
                .execute();

        // Producto 4 ≥ Reimplementamos el método para usar Entity Manager.
        // Creamos la conexión
        EntityManager em = getEntityManager();



        // En caso de error retornamos false.
        return false;
    }

    /* Producto 4 ≥ Ya no necesitamos métodos auxiliares para mapear los resultados de las consultas. */

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

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

//    // Comenzamos por usar un método para crear la conexión a la BBDD.
//    private Connection getConnection(String tipo) {
//        return Conexion.getInstance(tipo);
//    }

    // Producto 4 -> Refactorizamos la clase para usar Entity Manager.
    private EntityManager getEntityManager() {
        return ConexionJpa.getEntityManagerFactory();
    }

    // Obtenemos un listado de todos los artículos
    @Override
    public Listas<Articulo> findAll() {

        // Creamos la lista de Articulos que recibiremos de la BD.
        Listas<Articulo> articulos = new Listas<>();

        // Producto 4 -> Refactorizamos el método para usar Entity Manager.
        // Creamos la conexión
        EntityManager em = getEntityManager();

        // Creamos la consulta. Retornamos un Stream de Articulos ya que nuestra clase List está personalizada
        // y no podemos usar directamente el método getResultList().
        try {
            em.createQuery("Select a from Articulo a", Articulo.class).getResultStream().forEach(
                    // Pasamos el resultado a la lista de Articulos.
                    articulos::add
            );
        } catch (Exception e) {
            System.out.println("No es posible obtener la lista de artículos.");
            e.printStackTrace();
        } finally {
            // Cerramos la conexión
            em.close();
        }



        /*// Creamos la sentencia SQL para la consulta.
        String sql = "CALL get_articulos()";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        try (CallableStatement stmt = getConnection(dotenv.get("ENV")).prepareCall(sql);
             ResultSet res = stmt.executeQuery()) {

            // Reseteamos el contador de artículos.
            Articulo.resetContador();

            // Recibimos la respuesta y la iteramos. Cada objeto que recibamos, lo convertiremos en un artículo y
            // lo añadiremos a la lista.
            while (res.next()) {

                // Obtenemos el artículo mediante la función de mapeado.
                Articulo articulo = getArticulo(res);

                // Añadimos el artículo a la lista.
                articulos.add(articulo);

                // Avanzamos el contador de artículos
                Articulo.avanzarContador();
            }
        } catch (SQLException e) {
            System.out.println("No es posible obtener la lista de artículos.");
            e.printStackTrace();
        }*/

        // Devolvemos la lista de artículos.
        return articulos;
    }

    @Override
    public Articulo findById(Long id) {
        // Producto 4 -> Reimplementamos el método para usar Entity Manager.
        // Creamos la conexión
        EntityManager em = getEntityManager();

        // Creamos la consulta, el método find() nos devuelve un objeto de la clase Articulo buscándolo por su id.
        try {
            return em.find(Articulo.class, id);
        } catch (Exception e) {
            System.out.println(MessageFormat.format("No es posible obtener el artículo con id {0}", id));
            e.printStackTrace();
        } finally {
            // Cerramos la conexión
            em.close();
        }

        /*// Creamos la sentencia SQL para la consulta.
        String sql = "CALL get_articulo_by_id(?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(dotenv.get("ENV")).prepareCall(sql)) {

            // Asignamos el parámetro a la consulta. La elección del parámetro se hace por su posición.
            stmt.setLong(1, id);

            // Ejecutamos la consulta. Manejamos el autoclose con el try-with-resources.
            try (ResultSet res = stmt.executeQuery()) {

                // Recibimos la respuesta y la asignamos al artículo.
                // Como solo hay un objeto, no es necesario iterar, sino que creamos bloque condicional.
                if (res.next()) {

                    // Obtenemos el artículo mediante la función de mapeado.
                    articulo = getArticulo(res);
                }
            }
        } catch (SQLException e) {
            System.out.println("No es posible obtener el artículo con id " + id);
            e.printStackTrace();
        }*/

        return null;
    }

    @Override
    public Articulo findOne(String key) {
        // Producto 4 ≥ Reimplementamos el método para usar Entity Manager.
        // Creamos la conexión
        EntityManager em = getEntityManager();

        // Creamos la consulta, en este caso queremos buscar un artículo por su código, no por su Id.
        // Solo podemos usar el método getSingleResult() si estamos seguros de que solo recibiremos un resultado.
        try {
            // Creamos una query personalizada para buscar el artículo por su código.
            return em.createQuery("select a from Articulo a where a.codigo = :codigo", Articulo.class)
                    // Asignamos el parámetro a la consulta.
                    .setParameter("codigo", key)
                    // Limitamos el resultado a un solo objeto.
                    .setMaxResults(1)
                    // Ejecutamos la consulta.
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println(MessageFormat.format("No es posible obtener el artículo con código {0}", key));
            e.printStackTrace();
        } finally {
            // Cerramos la conexión
            em.close();
        }



       /* // Creamos la sentencia SQL para la consulta.
        String sql = "CALL get_articulo_by_cod(?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(dotenv.get("ENV")).prepareCall(sql)) {

            // Asignamos el parámetro a la consulta. La elección del parámetro se hace por su posición.
            stmt.setString(1, key);

            // Ejecutamos la consulta. Manejamos el autoclose con el try-with-resources.
            try (ResultSet res = stmt.executeQuery()) {

                // Recibimos la respuesta y la asignamos al artículo.
                // Como solo hay un objeto, no es necesario iterar, sino que creamos bloque condicional.
                if (res.next()) {

                    // Obtenemos el artículo mediante la función de mapeado.
                    articulo = getArticulo(res);
                }
            }
        } catch (SQLException e) {
            System.out.println("No es posible obtener el artículo con código " + key);
            e.printStackTrace();
        }*/

        return null;
    }

    @Override
    public boolean save(Articulo articulo) {

        // Producto 4 -> Reimplementamos el método para usar Entity Manager.
        // Creamos la conexión
        EntityManager em = getEntityManager();

        // Comenzaremos por determinar si tenemos que ejecutar una acción Create o un Update.
        // Para ello, comprobaremos si el artículo tiene un id asignado que funcionará como un flag.
        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        // Evaluamos si el id es mayor que 0 ya que en otro caso podría ser tanto 0 como null.
        if (articulo.getId() != null && articulo.getId() > 0) {
            // Si el id es mayor que 0, significa que ya existe en la BD.
            // Por lo tanto, ejecutaremos un Update.

            try {
                // Como se trata de una query que escribe en nuestra BD, debemos iniciar una transacción.
                em.getTransaction().begin();

                // Ejecutamos el Update.
                Articulo updatedArt = em.merge(articulo);

                // Commit
                em.getTransaction().commit();

                // Devolvemos el resultado de la operación.
                return updatedArt != null;
            } catch (Exception e) {
                System.out.println(MessageFormat.format("No es posible actualizar el artículo con id {0}", articulo.getId()));

                // Rollback
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }

                e.printStackTrace();
            } finally {
                // Cerramos la conexión
                em.close();
            }
        } else {
            // Si el id es 0, significa que no existe en la BD.
            // Por lo tanto, ejecutaremos un Create.
            try {
                // Iniciamos la transacción.
                em.getTransaction().begin();

                em.persist(articulo);

                // Finalizamos la transacción.
                em.getTransaction().commit();

                return true;
            } catch (Exception e) {
                System.out.println(MessageFormat.format("No es posible crear el artículo con código {0}", articulo.getCodArticulo()));

                // Rollback
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }

                e.printStackTrace();
            } finally {
                // Cerramos la conexión
                em.close();
            }
        }

//        String sql = null;

        /*// Evaluamos primero si el id es mayor que 0 ya que en otro caso podría ser tanto 0 como null.
        if (articulo.getId() != null) {
            // Si el id es mayor que 0, significa que ya existe en la BD.
            // Por lo tanto, ejecutaremos un Update.
            sql = "call update_articulo(?, ?, ?, ?, ?, ?, ?)";
        } else {
            // Si el id es 0, significa que no existe en la BD.
            // Por lo tanto, ejecutaremos un Create.
            sql = "call add_articulo(?, ?, ?, ?, ?, ?)";
        }

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(dotenv.get("ENV")).prepareCall(sql)) {

            // Mapeamos el statement a partir del artículo que recibimos por parámetro.
            getStatement(articulo, stmt);

            // Si se trata de un Update, debemos asignar el id al parámetro de la consulta.
            if (articulo.getId() != null) {
                stmt.setLong(7, articulo.getId());
            }

            // Ejecutamos la consulta, devolverá el ID del artículo si se ha ejecutado correctamente
            // o -1 de lo contrario.
            stmt.executeUpdate();

            return stmt.getLong(1) > 0;
        } catch (SQLException e) {
            System.out.println("No es posible guardar el artículo.");
            e.printStackTrace();
        }*/

        // Si algo falla, devolvemos false.
        return false;
    }

    @Override
    public boolean delete(Long id) {

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

        /*// Creamos la sentencia SQL para la consulta.
        String sql = "call delete_articulo(?, ?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(dotenv.get("ENV")).prepareCall(sql)) {
            // Preparamos el parámetro de salida.
            stmt.registerOutParameter(1, Types.NUMERIC);

            // Asignamos el parámetro a la consulta. La elección del parámetro se hace por su posición.
            stmt.setLong(2, id);

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            // Si la consulta se ha ejecutado correctamente, devolverá el ID del artículo.
            if (stmt.getLong(1) > 0) {
                Articulo.retrocederContador();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("No es posible eliminar el artículo con id " + id);
            e.printStackTrace();
        }*/
        return false;
    }

    @Override
    public int count() {

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


        /*// Creamos la sentencia para realizar la consulta.
        String sql = "SELECT COUNT(_id) AS total FROM articulos";

        // Creamos la variable que recibirá el resultado.
        int total = 0;

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un Satetment ya que no recibimos parámetros.
        try (PreparedStatement stmt = getConnection(dotenv.get("ENV")).prepareStatement(sql);
            ResultSet res = stmt.executeQuery(sql)) {

            // Recibimos la respuesta y la asignamos al total.
            if (res.next()) {
                total = res.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("No es posible obtener el número de registros de la tabla.");
            e.printStackTrace();
        }*/

        // Si ha habido algún error, devolvemos 0.
        return defaultValue;
    }


    @Override
    public Articulo getLast() {

        // Producto 4 ≥ Reimplementamos el método para usar Entity Manager.
        // Creamos la conexión
        EntityManager em = getEntityManager();

        // Creamos la consulta, en este caso queremos obtener el último artículo.
        // Para ello usaremos un query en JPQL de la clase EntityManager que retorna el primer registro de la tabla
        // ordenada de forma descendente por el campo _id.
        try {
            // Devolveremos directamente el resultado.
            return em.createQuery("select a from Articulo a order by a.id desc", Articulo.class)
                    // retornamos un único valor.
                    .setMaxResults(1)
                    // obtenemos el resultado.
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("No es posible obtener el último registro de la tabla.");
            e.printStackTrace();
        } finally {
            // Cerramos la conexión
            em.close();
        }
/*
        // Creamos la sentencia para realizar la consulta.
        String sql = "SELECT * FROM articulos ORDER BY _id DESC LIMIT 1";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un Satetment ya que no recibimos parámetros.
        try (PreparedStatement stmt = getConnection(dotenv.get("ENV")).prepareStatement(sql);
            ResultSet res = stmt.executeQuery(sql)) {

            // Recibimos la respuesta y la asignamos al artículo.
            return res.next()? getArticulo(res) : null;

        } catch (SQLException e) {
            System.out.println("No es posible obtener el último registro de la tabla.");
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    public boolean isEmpty() {
        return count() == 0;
    }

    @Override
    public boolean resetId() {

        // Producto 4 ≥ Reimplementamos el método para usar Entity Manager.
        // Creamos la conexión
        EntityManager em = getEntityManager();

        // Creamos la consulta, en este caso queremos resetear el contador de la tabla.
        // Para ello usaremos una llamada a un procedimiento almacenado de la clase EntityManager.
        try {
            // Como es una acción de escritura, iniciamos una transacción.
            em.getTransaction().begin();

            // Ejecutamos el procedimiento.
            em.createStoredProcedureQuery("reset_id_articulos")
                    .execute();

            // Confirmamos la transacción.
            em.getTransaction().commit();

            // Devolvemos true.
            return true;
        } catch (Exception e) {
            System.out.println("No es posible resetear el contador de la tabla.");

            // Realizamos un rollback en caso de error.
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
        } finally {
            // Cerramos la conexión
            em.close();
        }

        /*// Creamos la sentencia para realizar la consulta.
        String sql = "call reset_id_articulos()";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un CallableStatement.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)){
            // Ejecutamos el procedimiento.
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("No es posible resetear el contador de la tabla.");
            e.printStackTrace();
        }*/
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

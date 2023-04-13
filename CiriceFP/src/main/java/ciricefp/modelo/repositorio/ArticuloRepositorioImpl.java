package ciricefp.modelo.repositorio;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.utils.Conexion;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

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

    // Comenzamos por usar un método para crear la conexión a la BBDD.
    private Connection getConnection(String tipo) {
        return Conexion.getInstance(tipo);
    }

    @Override
    public Listas<Articulo> findAll() {

        // Creamos la lista de Articulos que recibiremos de la BD.
        Listas<Articulo> articulos = new Listas<>();

        // Creamos la sentencia SQL para la consulta.
        String sql = "SELECT * FROM articulos";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        try (Statement stmt = getConnection(System.getenv("ENV")).createStatement();
             ResultSet res = stmt.executeQuery(sql)) {

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
        }

        // Devolvemos la lista de artículos.
        return articulos;
    }

    @Override
    public Articulo findById(Long id) {

        // Creamos el objeto que recibirá el objeto de la BD.
        Articulo articulo = null;

        // Creamos la sentencia SQL para la consulta.
        String sql = "SELECT * FROM articulos WHERE _id = ?";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (PreparedStatement stmt = getConnection(System.getenv("ENV")).prepareStatement(sql)) {

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
        }

        return articulo;
    }

    @Override
    public Articulo findOne(String key) {
        // Creamos el objeto que recibirá el objeto de la BD.
        Articulo articulo = null;

        // Creamos la sentencia SQL para la consulta.
        String sql = "SELECT * FROM articulos WHERE cod_articulo = ?";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (PreparedStatement stmt = getConnection(System.getenv("ENV")).prepareStatement(sql)) {

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
        }

        return articulo;
    }

    @Override
    public boolean save(Articulo articulo) {

        // Comenzaremos por determinar si tenemos que ejecutar una acción Create o un Update.
        // Para ello, comprobaremos si el artículo tiene un id asignado que funcionará como un flag.
        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        String sql = null;

        // Evaluamos primero si el id es mayor que 0 ya que en otro caso podría ser tanto 0 como null.
        if (articulo.getId() != null) {
            // Si el id es mayor que 0, significa que ya existe en la BD.
            // Por lo tanto, ejecutaremos un Update.
            sql = "UPDATE articulos SET cod_articulo = ?, descripcion = ?, pvp = ?, gastos_envio = ?, tiempo_preparacion = ? WHERE _id = ?";
        } else {
            // Si el id es 0, significa que no existe en la BD.
            // Por lo tanto, ejecutaremos un Create.
            sql = "INSERT INTO articulos (cod_articulo, descripcion, pvp, gastos_envio, tiempo_preparacion) VALUES (?, ?, ?, ?, ?)";
        }

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (PreparedStatement stmt = getConnection(System.getenv("ENV")).prepareStatement(sql)) {

            // Mapeamos el statement a partir del artículo que recibimos por parámetro.
            getStatement(articulo, stmt);

            // Si se trata de un Update, debemos asignar el id al parámetro de la consulta.
            if (articulo.getId() != null) {
                stmt.setLong(6, articulo.getId());
            }

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("No es posible guardar el artículo.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {

        // Creamos la sentencia SQL para la consulta.
        String sql = "DELETE FROM articulos WHERE _id = ?";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (PreparedStatement stmt = getConnection(System.getenv("ENV")).prepareStatement(sql)) {

            // Asignamos el parámetro a la consulta. La elección del parámetro se hace por su posición.
            stmt.setLong(1, id);

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            // Decrementamos el contador de artículos.
            Articulo.retrocederContador();

            return true;

        } catch (SQLException e) {
            System.out.println("No es posible eliminar el artículo con id " + id);
            e.printStackTrace();
        }
        return false;
    }

    // Creamos un método para contabilizar el número de registros de la tabla.

    @Override
    public int count() {

        // Creamos la sentencia para realizar la consulta.
        String sql = "SELECT COUNT(_id) AS total FROM articulos";

        // Creamos la variable que recibirá el resultado.
        int total = 0;

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un Satetment ya que no recibimos parámetros.
        try (Statement stmt = getConnection(System.getenv("ENV")).createStatement();
            ResultSet res = stmt.executeQuery(sql)) {

            // Recibimos la respuesta y la asignamos al total.
            if (res.next()) {
                total = res.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("No es posible obtener el número de registros de la tabla.");
            e.printStackTrace();
        }

        return total;
    }

    // Creamos un método para mapear los ResultSet. Lo vamos a usar únicamente dentro de la clase.
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
    private static void getStatement(Articulo articulo, PreparedStatement stmt) throws SQLException {

        // Asignamos los parámetros a la consulta desde el artículo que recibimos por parámetro.
        // La elección del parámetro se hace por su posición.
        stmt.setString(1, articulo.getCodArticulo());
        stmt.setString(2, articulo.getDescripcion());
        stmt.setDouble(3, articulo.getPvp());
        stmt.setDouble(4, articulo.getGastosEnvio());
        stmt.setInt(5, articulo.getTiempoPreparacion());
    }
}

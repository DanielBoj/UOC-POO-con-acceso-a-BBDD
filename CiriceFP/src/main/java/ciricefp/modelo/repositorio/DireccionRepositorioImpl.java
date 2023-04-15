package ciricefp.modelo.repositorio;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.Cliente;
import ciricefp.modelo.Direccion;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.utils.Conexion;

import java.sql.*;
import java.text.MessageFormat;

/**
 * Esta clase implementa la interfaz Repositorio para nuestra entidad Direccion.
 * Esta clase será la encargada de gestionar los datos de la entidad Direccion.
 * Debe implementar todos los métodos de la interfaz Repositorio.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public class DireccionRepositorioImpl implements Repositorio<Direccion> {

    // Comenzamos por usar un método para crear la conexión a la BBDD.
    private static Connection getConnection(String tipo) {
        return Conexion.getInstance(tipo);
    }

    @Override
    public Listas<Direccion> findAll() {

        // Creamos la lista de direcciones.
        Listas<Direccion> direcciones = new Listas<>();

        // Creamos la sentencia SQL para realizar al consulta.
        String sql = "CALL get_direcciones()";

        // Colocamos los recursos como argumentos del try-with-resources.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql);
             ResultSet res = stmt.executeQuery()) {

            // Recibimos la respuesta y la iteramos. Cada objeto que recibamos, lo convertiremos en una dirección y
            // lo añadiremos a la lista.
            while (res.next()) {

                // Obtenemos la dirección mediante la función de mapeado.
                Direccion direccion = getDireccion(res);

                // Añadimos la dirección a la lista.
                direcciones.add(direccion);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de direcciones.");
            e.printStackTrace();
        }

        // Devolvemos la lista de direcciones.
        return direcciones;
    }

    @Override
    public Direccion findById(Long id) {

        // Creamos el objeto que recibirá los datos de la BD.
        Direccion direccion = null;

        // Creamos la sentencia SQL para realizar al consulta.
        String sql = "CALL get_direccion_by_id(?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {

            // Asignamos el parámetro a la consulta.
            stmt.setLong(1, id);

            // Ejecutamos la consulta y obtenemos el resultado. Manejamos el autoclose con el try-with-resources.
            try (ResultSet res = stmt.executeQuery()) {

                // Recibimos la respuesta y la asignamos a la dirección.
                // Como solo hay un objeto, no es necesario iterar sino que usamos un bloque condicional.
                if (res.next()) {
                    // Usamos la función de mapeado para obtener la dirección.
                    direccion = getDireccion(res);
                }
            }
        } catch (SQLException e) {
            System.out.println(MessageFormat.format("Error al obtener la direccion con id {0}", id));
            e.printStackTrace();
        }

        return direccion;
    }

    // Direcciones no tiene ningún identificador único además de la llave, así que no será posible
    // implementar este método.
    @Override
    public Direccion findOne(String key) {

        return null;
    }

    @Override
    public boolean save(Direccion direccion) {
        // Comenzaremos por determinar si tenemos que ejecutar una acción Create o un Update.
        // Para ello, comprobaremos si la dirección tiene un id asignado que funcionará como un flag.
        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        String sql = null;

        if (direccion.getId() != null) {
            // Si el id no es nulo, significa que la dirección ya existe en la BD.
            // Creamos la sentencia para lanzar una sentencia UPDATE.
            sql = "call update_direccion(?, ?, ?, ?, ?, ?, ?)";
        } else {
            // Si el id es nulo, significa que la dirección no existe en la BD y debemos crearla.
            sql = "call add_direccion(?, ?, ?, ?, ?, ?)";
        }

        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        // Manejamos el autoclose con el try-with-resources.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {
            // Mapeamos el statement con los datos de la dirección.
            getStatement(direccion, stmt);


            // Añadimos el parámetro id si es necesario.
            if (direccion.getId() != null) {
                stmt.setLong(6, direccion.getId());
            }

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            // Si la consulta se ha ejecutado correctamente, devulve el id de la dirección.
            return stmt.getLong(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error al intentar crear la dirección en la base de datos.");
            e.printStackTrace();
        }

        // Si la consulta no se ha ejecutado correctamente, devolvemos false.
        return false;
    }

    @Override
    public boolean delete(Long id) {

        // Creamos la sentencia SQL para la consulta.
        String sql = "call delete_direccion(?, ?)";

        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        // Manejamos el autoclose con el try-with-resources.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {
            // Asignamos el parámetro a la consulta.
            stmt.setLong(2, id);

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            // Si la consulta se ha ejecutado correctamente, devulve el id de la dirección.
            return stmt.getLong(1) > 0;
        } catch (SQLException e) {
            System.out.println(MessageFormat.format("Error al intentar eliminar la dirección con id {0} " +
                    "de la base de datos.", id));
            e.printStackTrace();
        }

        // Si la consulta no se ha ejecutado correctamente, devolvemos false.
        return false;
    }

    @Override
    public int count() {

        // Creamos la sentencia SQL para la consulta.
        String sql = "SELECT COUNT(_id) AS total FROM direcciones";

        // Creamos la variable que recibirá el resultado.
        int total = 0;

        // Creamos la consulta a la BD mediante un Statement ya que no recibimos parámetros.
        // Manejamos el autoclose con el try-with-resources.
        try (Statement stmt = getConnection(System.getenv("ENV")).createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            // Recibimos el resultado y lo asignamos a la variable.
            if (res.next()) {
                total = res.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error al intentar obtener el número de direcciones en la base de datos.");
            e.printStackTrace();
        }

        return total;
    }

    @Override
    public Direccion getLast() {

        // Creamos la sentencia SQL para la consulta.
        String sql = "SELECT * FROM direcciones ORDER BY _id DESC LIMIT 1";

        // Ejecutamos el Statement como autoclose y obtenemos el resultado.
        try (Statement stmt = getConnection(System.getenv("ENV")).createStatement();
             ResultSet res = stmt.executeQuery(sql)) {

            // Si hay un resultado, lo devolvemos.
            return res.next()? getDireccion(res) : null;
        } catch (SQLException e) {
            System.out.println("Error al intentar obtener la última dirección de la base de datos.");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isEmpty() {
        return count() == 0;
    }

    @Override
    public boolean resetId() {
        // Creamos la sentencia para realizar la consulta.
        String sql = "call reset_id_direcciones()";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un CallableStatement.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)){
            // Ejecutamos el procedimiento.
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("No es posible resetear el contador de la tabla.");
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteAll() {
        // Creamos la sentencia SQL para la consulta.
        String sql = "CALL delete_direcciones(?)";

        // Manejamos el autoclose con el try-with-resources.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {
            // Preparamos el parámetro de salida.
            stmt.registerOutParameter(1, Types.INTEGER);

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            // Capturamos la variable OUT del query.
            if (stmt.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al intentar eliminar todas las direcciones de la base de datos.");
            e.printStackTrace();
        }

        return false;
    }

    // Creamos un método para mapear los ResultSet. Lo vamos a usar únicamente dentro de la clase.
    // Recibe el ResultSet como parámetro.
    private static Direccion getDireccion(ResultSet res) throws SQLException {

        // Creamos la dirección y obtenemos los datos del ResultSet.
        Direccion direccion = new Direccion();

        direccion.setId(res.getLong("_id"));
        direccion.setDireccion(res.getString("direccion"));
        direccion.setCiudad(res.getString("ciudad"));
        direccion.setProvincia(res.getString("provincia"));
        direccion.setCodigoPostal(res.getString("codigo_postal"));
        direccion.setPais(res.getString("pais"));

        return direccion;
    }

    // Mapeamos un statement a partir de una dirección.
    // No es una función pura, pero modifica únicamente una variable que usamos en un método.
    private static void getStatement(Direccion direccion, CallableStatement stmt) throws SQLException {
        // Asignamos los parámetros a la consulta desde el artículo que recibimos por parámetro.
        // La elección del parámetro se hace por su posición.
        stmt.registerOutParameter(1, Types.BIGINT);
        stmt.setString(2, direccion.getDireccion());
        stmt.setString(3, direccion.getCiudad());
        stmt.setString(4, direccion.getProvincia());
        stmt.setString(5, direccion.getCodigoPostal());
        stmt.setString(6, direccion.getPais());
    }
}

package ciricefp.modelo.repositorio;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.Cliente;
import ciricefp.modelo.Pedido;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.utils.Conexion;

import java.sql.*;
import java.text.MessageFormat;

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

    // Comenzamos por usar un método para crear la conexión a la BBDD.
    private Connection getConnection(String tipo) {
        return Conexion.getInstance(tipo);
    }

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

        // Creamos la sentencia SQL parar la consulta, en este caso manejaremos la complejidad de la
        // entidad apoyándonos en la lógica implementada en el módulo Repositorio.
        String sql = "CALL get_pedidos()";

        // Colocamos los recursos como argumentos del try-with-resources.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql);
             ResultSet res = stmt.executeQuery()) {

            // Recibimos la respuesta y la iteramos. Cada objeto que recibamos, lo convertiremos en un pedido y
            // lo añadiremos a la lista.
            while (res.next()) {

                // Obtenemos el pedido mediante la función de mapeado y asociamos las entidades asociadas.
                Pedido pedido = getPedido(res);

                // Actualizamos el total de pedidos
                Pedido.avanzarTotalPedidos();

                // Añadimos el pedido a la lista.
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.out.println("No ha sido posible obtener la lista de pedidos.");
            e.printStackTrace();
        }

        // Devolvemos la lista de pedidos.
        return pedidos;
    }

    @Override
    public Pedido findById(Long id) {

        // Creamos el objeto que contendrá el pedido.
        Pedido pedido = null;

        // Creamos la sentencia SQL que obtendrá el pedido por su id.
        String sql = "CALL get_pedido_by_id(?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {
            // Establecemos el parámetro de la consulta.

            stmt.setLong(1, id);

            // Ejecutamos la consulta y obtenemos el resultado. Manejamos el autoclose con un try-with-resources.
            try (ResultSet res = stmt.executeQuery()) {

                // Recibimos la respuesta y la asignamos al pedido.
                // Como solo hay un objeto, no es necesario iterar sino que usamos un bloque condicional.
                if (res.next()) {
                    pedido = getPedido(res);
                }
            }
        } catch (SQLException e) {
            System.out.println(MessageFormat.format("No ha sido posible obtener el pedido con id {0}", id));
            e.printStackTrace();
        }
        return pedido;
    }

    @Override
    public Pedido findOne(String key) {

        // Creamos el objeto que contendrá el pedido.
        Pedido pedido = null;

        // Creamos la sentencia SQL que obtendrá el pedido por su id.
        String sql = "CALL get_pedido_by_numero_pedido(?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {

            // Pasamos el parámetro de búsqueda a la consulta.
            stmt.setInt(1, Integer.parseInt(key));

            // Ejecutamos la consulta y obtenemos el resultado. Manejamos el autoclose con un try-with-resources.
            try (ResultSet res = stmt.executeQuery()) {

                    // Recibimos la respuesta y la asignamos al pedido.
                    // Como solo hay un objeto, no es necesario iterar sino que usamos un bloque condicional.
                    if (res.next()) {
                        pedido = getPedido(res);
                    }
            }
        } catch (SQLException e) {
            System.out.println(MessageFormat.format("No ha sido posible obtener el pedido con el número {0}", key));
            e.printStackTrace();
        }
        return pedido;
    }

    @Override
    public boolean save(Pedido pedido) {
        // Comenzaremos por determinar si tenemos que ejecutar una acción Create o un Update.
        // Para ello, comprobaremos si el artículo tiene un id asignado que funcionará como un flag.
        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        String sql = null;

        // Evaluamos si existe ID para el pedido.
        if (pedido.getId() != null) {
            // Si es así, actualizamos el pedido.
            sql = "call update_pedido(?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            // Si no, creamos un nuevo pedido.
            sql = "call add_pedido(?, ?, ?, ?, ?, ?, ?)";
        }

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {

            // Mapeamos el statement a partir del artículo que recibimos por parámetro.
            getStatement(pedido, stmt);

            // Si el pedido tiene id, lo establecemos como parámetro de la consulta para realizar la modificación.
            if (pedido.getId() != null) {
                stmt.setLong(8, pedido.getId());
            }

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            // Si la consulta ha sido exitosa, devuelve el id del pedido.
            return stmt.getLong(1) > 0;
        } catch (SQLException e) {
            System.out.println("No ha sido posible guardar el pedido en la base de datos.");
            e.printStackTrace();
        }

        // Si ha habido excepciones, devolvemos false.
        return false;
    }

    @Override
    public boolean delete(Long id) {

        // Creamos la sentencia SQL para eliminar el pedido.
        String sql = "call = delete_pedido(?, ?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {
            // Preparamos el parámetro de salida.
            stmt.registerOutParameter(1, Types.BIGINT);

            // Pasamos el argumento de la consulta.
            stmt.setLong(2, id);

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            // Si la consulta ha sido exitosa, devuelve el id del pedido.
            if (stmt.getLong(1) > 0) {
                // Si se ha eliminado el pedido, decrementamos el contador de pedidos.
                Pedido.decrementarTotalPedidos();
                // Devolvemos true.
                return true;
            }
        } catch (SQLException e) {
            System.out.println(MessageFormat.format("No ha sido posible eliminar el pedido con id {0}", id));
            e.printStackTrace();
        }

        // Si ha habido excepciones, devolvemos false.
        return false;
    }

    @Override
    public int count() {

        // Creamos la sentencia SQL para obtener el número de pedidos.
        String sql = "SELECT COUNT(_id) AS total FROM pedidos";

        // Creamos la variable que contendrá el total de pedidos.
        int total = 0;

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un Statement ya que no recibimos parámetros.
        try (PreparedStatement stmt = getConnection(System.getenv("ENV")).prepareStatement(sql);
             ResultSet res = stmt.executeQuery(sql)) {

            // Recibimos la respuesta y la asignamos al total.
            if (res.next()) {
                total = res.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("No ha sido posible obtener el número de pedidos.");
            e.printStackTrace();
        }

        return total;
    }

    @Override
    public Pedido getLast() {

        // Creamos la sentencia SQL para obtener el último pedido.
        String sql = "SELECT * FROM pedidos ORDER BY _id DESC LIMIT 1";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un Statement ya que no recibimos parámetros.
        try (PreparedStatement stmt = getConnection(System.getenv("ENV")).prepareStatement(sql);
            ResultSet res = stmt.executeQuery(sql)) {

            // Recibimos la respuesta y la asignamos al pedido.
            return res.next() ? getPedido(res) : null;
        } catch (SQLException e) {
            System.out.println("No ha sido posible obtener el último pedido.");
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
        }

        return false;
    }

    // Creamos un método para mapear un objeto Pedido a través de los resultador de una query.
    public Pedido getPedido(ResultSet res) throws SQLException {

        // Creamos el objeto principal que recibirá los datos de la BD.
        Pedido pedido = new Pedido();

        // Creamos los identificadores de las entidades asociadas.
        Long clienteId = null;
        Long articuloId = null;

        // Obtenemos los atributos de los datos de la entidad
        pedido.setId(res.getLong("_id"));
        pedido.setNumeroPedido(res.getInt("numero_pedido"));
        clienteId = res.getLong("cliente_id");
        articuloId = res.getLong("articulo_id");
        pedido.setUnidades(res.getInt("unidades"));
        // Debemos castear el tipo Date a LocalDate.
        pedido.setFechaPedido(res.getDate("fecha_pedido").toLocalDate());
        pedido.setEsEnviado(res.getBoolean("es_enviado"));

        // Una vez obtenidos los datos de la entidad, debemos obtener los datos de las entidades asociadas.
        // Para ello, debemos crear los objetos que recibirán los datos de las entidades asociadas y obtenerlos
        // de la base de datos mediante los métodos que hemos implementado en los repositorios de las entidades.

        // Creamos los objetos que recibirán los datos de las entidades asociadas.
        Repositorio<Cliente> clienteRepositorio = new ClienteRepositorioImpl();
        Repositorio<Articulo> articuloRepositorio = new ArticuloRepositorioImpl();

        Cliente cliente = clienteRepositorio.findById(clienteId);
        Articulo articulo = articuloRepositorio.findById(articuloId);

        // Una vez obtenidos los datos de las entidades asociadas, debemos asociarlas a la entidad principal.
        pedido.setCliente(cliente);
        pedido.setArticulo(articulo);

        return pedido;
    }

    // Creamos un método que nos permita mapear el statement a partir de un pedido.
    // Por la complejidad de este objeto, deberemos pasar los ID de las entidades asociadas.
    // De esta forma mantenemos la integridad referencial de la BD.
    private void getStatement(Pedido pedido, CallableStatement stmt) throws SQLException {

        // Mapeamos el statement a partir del artículo que recibimos por parámetro.
        // Preparamos el parámetro de salida.
        stmt.registerOutParameter(1, Types.BIGINT);
        stmt.setInt(2, pedido.getNumeroPedido());
        stmt.setLong(3, pedido.getCliente().getId());
        stmt.setLong(4, pedido.getArticulo().getId());
        stmt.setInt(5, pedido.getUnidades());
        // Tenemos que convertir el tipo de datos de LocalDate a Date para poder insertarlo en la BD.
        stmt.setDate(6, Date.valueOf(pedido.getFechaPedido()));
        stmt.setBoolean(7, pedido.getEsEnviado());
    }
}

package ciricefp.modelo.repositorio;

import ciricefp.modelo.*;
import ciricefp.modelo.interfaces.factory.IClienteFactory;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.utils.Conexion;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * Esta clase implementa la interfaz Repositorio para nuestra entidad Cliente.
 * Esta clase será la encargada de gestionar los datos de la entidad Cliente.
 * Debe implementar todos los métodos de la interfaz Repositorio.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public class ClienteRepositorioImpl implements Repositorio<Cliente> {

    // Comenzamos por usar un método para crear la conexión a la BBDD.
    private Connection getConnection(String tipo) {
        return Conexion.getInstance(tipo);
    }

    @Override
    public Listas<Cliente> findAll() {
        // Creamos la lista de Clientes que recibiremos de la BD.
        Listas<Cliente> clientes = new Listas<>();

        // Creamos la sentencia SQL para la consulta.
        String sql = "CALL get_clientes()";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql);
             ResultSet res = stmt.executeQuery()) {

            // Reseteamos el número de clientes.
            Cliente.resetTotalClientes();

            // Recibimos la respuesta y la iteramos. Cada objeto que recibamos, lo convertiremos en un artículo y
            // lo añadiremos a la lista.
            while (res.next()) {

                // Obtenemos el artículo mediante la función de mapeado.
                Cliente cliente = getCliente(res);

                // Añadimos el artículo a la lista.
                clientes.add(cliente);

                // Avanzamos el contador de artículos
                Cliente.advanceTotalClientes();
            }
        } catch (SQLException e) {
            System.out.println("No es posible obtener la lista de artículos.");
            e.printStackTrace();
        }

        // Devolvemos la lista de artículos.
        return clientes;
    }

    @Override
    public Cliente findById(Long id) {
        // Creamos el objeto que recibirá el objeto de la BD.
        Cliente cliente = null;

        // Creamos la sentencia SQL para la consulta.
        String sql = "CALL get_cliente_by_id(?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {

            // Asignamos el parámetro a la consulta. La elección del parámetro se hace por su posición.
            stmt.setLong(1, id);

            // Ejecutamos la consulta. Manejamos el autoclose con el try-with-resources.
            try (ResultSet res = stmt.executeQuery()) {

                // Recibimos la respuesta y la asignamos al artículo.
                // Como solo hay un objeto, no es necesario iterar, sino que creamos bloque condicional.
                if (res.next()) {

                    // Obtenemos el artículo mediante la función de mapeado.
                    cliente = getCliente(res);
                }
            }
        } catch (SQLException e) {
            System.out.println("No es posible obtener el artículo con id " + cliente);
            e.printStackTrace();
        }

        return cliente;
    }

    @Override
    public Cliente findOne(String key) {
        // Creamos el objeto que recibirá el objeto de la BD.
        Cliente cliente = null;

        // Creamos la sentencia SQL para la consulta.
        String sql = "call get_cliente_by_nif(?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {

            // Asignamos el parámetro a la consulta. La elección del parámetro se hace por su posición.
            stmt.setString(1, key);

            // Ejecutamos la consulta. Manejamos el autoclose con el try-with-resources.
            try (ResultSet res = stmt.executeQuery()) {

                // Recibimos la respuesta y la asignamos al artículo.
                // Como solo hay un objeto, no es necesario iterar, sino que creamos bloque condicional.
                if (res.next()) {

                    // Obtenemos el artículo mediante la función de mapeado.
                    cliente = getCliente(res);
                }
            }
        } catch (SQLException e) {
            System.out.println("No es posible obtener el artículo con id " + cliente);
            e.printStackTrace();
        }

        return cliente;
    }

    /* Al estar guardando un modelo de objeto complejo, realizaremos una inserción por pasos.
    * Primero, insertaremos los datos de la tabla padre de las entidades Cliente.
    * Después, insertaremos los datos de la tabla hija de las entidades Cliente.
    * A la vez, tenemos que tener en cuenta el manejo de la Dirección, que también es una entidad.
    */
    @Override
    public boolean save(Cliente cliente) {
        // Deberemos determinar si tenemos que ejecutar una acción Create o un Update.
        // Para ello, comprobaremos si el artículo tiene un id asignado que funcionará como un flag.
        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        String sql = null;

        // Flag para identificar el tipo
        String flag = null;

        // Controlador de exito
        boolean isSaved = false;

        // Evaluamos primero si el id es mayor que 0 ya que en otro caso podría ser tanto 0 como null.
        if (cliente.getId() != null) {
            // Si el id es mayor que 0, significa que ya existe en la BD.
            // Por lo tanto, ejecutaremos un Update.

            // Realizamos la inserción de los datos para la tabla padre de las entidades Cliente.
            // Empezamos por la tabla padre.
            sql = "call update_cliente(?, ?, ?, ?, ?, ?)";

            try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {

                // Preparamos el parámetro de salida.
                stmt.registerOutParameter(1, Types.BIGINT);

                // Generamos el modelo de cliente para la actualización.
                stmt.setString(2, cliente.getNombre());
                stmt.setLong(3, cliente.getDomicilio().getId());
                stmt.setString(4, cliente.getNif());
                stmt.setString(5, cliente.getEmail());
                stmt.setLong(6, cliente.getId());

                // Ejecutamos la consulta.
                stmt.executeUpdate();

                // Si la consulta se ha ejecutado correctamente, devovlerá el id del cliente.
                isSaved = stmt.getLong(1) > 0;

                // Modificamos la dirección del cliente.
                Repositorio<Direccion> repoDireccion = new DireccionRepositorioImpl();
                repoDireccion.save(cliente.getDomicilio());

                // A continuación, realizamos la inserción de los datos para la tabla hija de las
                // entidades Cliente.
                // Dadas las caracteríscas de nuestra lógica de negocio, no es necesario realizar
                // cambios si se trata de un cliente estandard.
                if (IClienteFactory.tipoCliente(cliente).equals("ClientePremium")) {
                    // Actualizamos la sentencia sql
                    sql = "call update_cliente_premium(?, ?, ?, ?)";

                    // Generamos la consulta con autoclose.
                    try (CallableStatement stmtPremium = getConnection(System.getenv("ENV")).prepareCall(sql)) {

                        // Preparamos el parámetro de salida.
                        stmt.registerOutParameter(1, Types.BIGINT);

                        // Generamos el modelo para la actualización del Cliente Premium.
                        stmtPremium.setDouble(2, IClienteFactory.getCuota(cliente));
                        stmtPremium.setDouble(3, IClienteFactory.getDescuento(cliente));
                        stmtPremium.setLong(4, cliente.getId());

                        // Ejecutamos la consulta.
                        stmtPremium.executeUpdate();

                        // Si la consulta se ha ejecutado correctamente, devovlerá el id del cliente.
                        isSaved = stmtPremium.getLong(1) > 0;
                    }
                }
            } catch (SQLException e) {
                System.out.println(MessageFormat.format("No es posible actualizar el cliente {0}", cliente.getNombre()));
                e.printStackTrace();
            }
        } else {
            // Si el id es 0, significa que no existe en la BD.
            // Por lo tanto, ejecutaremos un Insert.

            // Realizamos la inserción de los datos para la tabla padre de las entidades Cliente.
            sql = "call add_cliente(?, ?, ?, ?, ?)";

            // Generamos la consulta con autoclose.
            try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {
                // Preparamos el parámetro de salida.
                stmt.registerOutParameter(1, Types.NUMERIC);

                // Como se trata de un insert, primero debemos crear la Dirección asociada al cliente
                // manteniendo nuestra integridad referencial.
                Repositorio<Direccion> repoDireccion = new DireccionRepositorioImpl();

                // Generamos el modelo para la inserción del Cliente.
                stmt.setString(2, cliente.getNombre());

                // Insertamos la dirección y obtenemos el id generado.
                if (repoDireccion.save(cliente.getDomicilio())) {
                    stmt.setLong(3, Objects.requireNonNull(repoDireccion.getLast()).getId());
                } else {
                    System.out.println("No se ha podido crear la dirección");
                    return false;
                }
                stmt.setString(4, cliente.getNif());
                stmt.setString(5, cliente.getEmail());

                // Ejecutamos la consulta.
                if (stmt.executeUpdate() > 0) {
                    // Recibimos como respuesta el ID del cliente generado.
                    cliente.setId(stmt.getLong(1));

                    // Actualizamos el flag de éxito.
                    isSaved = true;
                }

                // A continuación debemos identificar de qué tipo de Cliente se trata.
                // Para ello, utilizaremos el método tipoCliente() de la clase Cliente.
                flag = IClienteFactory.tipoCliente(cliente);

                switch (flag) {
                    // Para un cliente estandard debemos indicar el ID del cliente en la tabla de
                    // clientes estandard.
                    case "ClienteEstandard" -> {
                        // Creamos la sentencia SQL para la consulta.
                        sql = "call add_cliente_estandard(?, ?)";

                        // Generamos la consulta con autoclose.
                        try (CallableStatement stmtEstandard = getConnection(System.getenv("ENV")).prepareCall(sql)) {
                            // Preparamos el parámetro de salida.
                            stmt.registerOutParameter(1, Types.NUMERIC);

                            // Generamos el modelo para la inserción del Cliente Estandard.
                            stmtEstandard.setLong(2, cliente.getId());

                            // Ejecutamos la consulta.
                            stmtEstandard.executeUpdate();

                            // Si la consulta se ha ejecutado correctamente, devovlerá el id del cliente.
                            isSaved = stmtEstandard.getLong(1) > 0;
                        }
                    }
                    // Para un cliente premium debemos indicar el descuento, la cuota anual y el código
                    // de socio en la tabla de clientes premium.
                    case "ClientePremium" -> {
                        // Creamos la sentencia SQL para la consulta.
                        sql = "call add_cliente_premium(?, ?, ?, ?, ?)";

                        // Generamos la consulta con autoclose.
                        try (CallableStatement stmtPremium = getConnection(System.getenv("ENV")).prepareCall(sql)) {
                            // Preparamos el parámetro de salida.
                            stmt.registerOutParameter(1, Types.NUMERIC);

                            // Generamos el modelo para la inserción del Cliente Premium.
                            stmtPremium.setLong(2, cliente.getId());
                            stmtPremium.setDouble(3, IClienteFactory.getCuota(cliente));
                            stmtPremium.setDouble(4, IClienteFactory.getDescuento(cliente));
                            stmtPremium.setString(5, IClienteFactory.getCodSocio(cliente));

                            // Ejecutamos la consulta.
                            stmtPremium.executeUpdate();

                            // Si la consulta se ha ejecutado correctamente, devovlerá el id del cliente.
                            isSaved = stmtPremium.getLong(1) > 0;
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println(MessageFormat.format("No es posible crear el cliente {0}", cliente.getNombre()));
                e.printStackTrace();
            }
        }

        // Devolvemos el resultado de la operación.
        return isSaved;
    }

    @Override
    public boolean delete(Long id) {
        // La eliminación constará de 2 pasos, primero eliminaremos la dirección y luego el cliente.
        // Capturamos el ID de la dirección del objeto Cliente para poder eliminarlo.

        // Si el cliente aparece en algún pedido, no se podrá eliminar.
        Repositorio<Pedido> pedidoRepo = new PedidoRepositorioImpl();
        if (pedidoRepo.findAll().getLista().stream()
                .anyMatch(ped -> ped.getCliente().getId().equals(id))) {
            System.out.println("No se puede eliminar el cliente porque está asociado a un pedido.");
            return false;
        }

        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        String sql = "call delete_cliente(?, ?)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (CallableStatement stmt = getConnection(System.getenv("ENV")).prepareCall(sql)) {
            // Preparamos el parámetro de salida.
            stmt.registerOutParameter(1, Types.NUMERIC);

            // Eliminamos el registro correspodiente a la dirección en la tabla correspondiente.
            Repositorio<Direccion> direccionRepo = new DireccionRepositorioImpl();
            // Primero obtenemos el objeto Direccion a partir del ID del cliente.
            // A continuación, obtenemos la ID de la dirección y la eliminamos.
            if (!direccionRepo.delete(findById(id).getDomicilio().getId())) {
                System.out.println("No se ha podido eliminar la dirección");
                return false;
            }

            // Preparamos el parámetro de salida.
            stmt.registerOutParameter(1, Types.NUMERIC);

            // Asignamos el parámetro a la consulta. La elección del parámetro se hace por su posición.
            stmt.setLong(2, id);

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            // Si la consulta se ha ejecutado correctamente, devolverá el id del cliente.
            if (stmt.getLong(1) > 0) {
                // Decrecemos el número de clientes
                Cliente.decreaseTotalClientes();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("No es posible eliminar el artículo con id " + id);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int count() {

        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        String sql = "SELECT COUNT(_id) AS total FROM clientes";

        // Creamos la variable que recibirá el resultado de la consulta.
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

    @Override
    public Cliente getLast() {

        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        String sql = "SELECT * FROM clientes c " +
                "LEFT JOIN clientes_estandard ce ON (ce.cliente_id = c._id) " +
                "LEFT JOIN clientes_premium cp ON (cp.cliente_id = c._id)" +
                "JOIN direcciones d ON (d._id = c.direccion_id)" +
                "ORDER BY c._id DESC LIMIT 1";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un Satetment ya que no recibimos parámetros.
        try (Statement stmt = getConnection(System.getenv("ENV")).createStatement();
            ResultSet res = stmt.executeQuery(sql)) {

            // Recibimos la respuesta y la asignamos al cliente.
            return res.next()? getCliente(res) : null;
        } catch (SQLException e) {
            System.out.println("No es posible obtener el último registro de la tabla.");
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
        String sql = "call reset_id_clientes()";

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

    // Creamos un método para mapear los ResultSet. Lo vamos a usar únicamente dentro de la clase.
    // Recibe el ResultSet como parámetro.
    @NotNull
    private Cliente getCliente(ResultSet res) throws SQLException {

        // Comprobamos qué tipo de cliente es y creamos la instancia de cliente y de dirección.
        Cliente cliente = null;
        Direccion direccion = new Direccion();

        // Asignamos los valores para dirección, podemos capturarlos por los nombres de las columnas.
        direccion.setId(res.getLong("direccion_id"));
        direccion.setDireccion(res.getString("direccion"));
        direccion.setCiudad(res.getString("ciudad"));
        direccion.setProvincia(res.getString("provincia"));
        direccion.setCodigoPostal(res.getString("codigo_postal"));
        direccion.setPais(res.getString("pais"));

        // Asignamos los valores comunes para los clientes. Trabajaremos con índices para identificar
        // los campos, ya que hay atributos que no queremos duplicar o que son null según el tipo de cliente.
        if (res.getLong(6) != 0) {
            cliente = new ClienteEstandard();

            cliente.setId(res.getLong(1));
            cliente.setNombre(res.getString(2));
            cliente.setDomicilio(direccion);
            cliente.setNif(res.getString(4));
            cliente.setEmail(res.getString(5));

        } else {
            cliente = new ClientePremium();

            cliente.setId(res.getLong(1));
            cliente.setNombre(res.getString(2));
            cliente.setDomicilio(direccion);
            cliente.setNif(res.getString(4));
            cliente.setEmail(res.getString(5));

            // Asignamos los valores específicos para los clientes premium.
            ((ClientePremium) cliente).setCuota(res.getDouble(10));
            ((ClientePremium) cliente).setDescuento(res.getDouble(11));
            ((ClientePremium) cliente).setCodSocio(res.getString(12));
        }

        return cliente;
    }
}

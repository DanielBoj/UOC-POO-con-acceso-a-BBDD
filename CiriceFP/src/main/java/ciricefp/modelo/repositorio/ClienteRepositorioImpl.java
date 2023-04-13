package ciricefp.modelo.repositorio;

import ciricefp.modelo.*;
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
        String sql = "SELECT * FROM clientes c " +
                "LEFT JOIN clientes_estandard ce ON (ce.cliente_id = c._id) " +
                "LEFT JOIN clientes_premium cp ON (cp.cliente_id = c._id)" +
                "JOIN direcciones d ON (d._id = c.direccion_id)";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        try (Statement stmt = getConnection(System.getenv("ENV")).createStatement();
             ResultSet res = stmt.executeQuery(sql)) {

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
        String sql = "SELECT * FROM clientes c " +
                "LEFT JOIN clientes_estandard ce ON (ce.cliente_id = c._id) " +
                "LEFT JOIN clientes_premium cp ON (cp.cliente_id = c._id)" +
                "JOIN direcciones d ON (d._id = c.direccion_id)" +
                "WHERE c._id = ?";

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
        String sql = "SELECT * FROM clientes c " +
                "LEFT JOIN clientes_estandard ce ON (ce.cliente_id = c._id) " +
                "LEFT JOIN clientes_premium cp ON (cp.cliente_id = c._id)" +
                "JOIN direcciones d ON (d._id = c.direccion_id)" +
                "WHERE c.nif = ?";

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
            sql = "UPDATE clientes SET nombre = ?, direccion_id = ?, nif = ?, email = ? WHERE _id = ?";

            try (PreparedStatement stmt = getConnection(System.getenv("ENV")).prepareStatement(sql)) {

                // Generamos el modelo de cliente para la actualización.
                stmt.setString(1, cliente.getNombre());
                stmt.setLong(2, cliente.getDomicilio().getId());
                stmt.setString(3, cliente.getNif());
                stmt.setString(4, cliente.getEmail());
                stmt.setLong(5, cliente.getId());

                // Ejecutamos la consulta.
                isSaved = stmt.executeUpdate() > 0;

                // A continuación, realizamos la inserción de los datos para la tabla hija de las
                // entidades Cliente.
                // Dadas las caracteríscas de nuestra lógica de negocio, no es necesario realizar
                // cambios si se trata de un cliente estandard.
                if (cliente.tipoCliente().equals("ClientePremium")) {
                    // Actualizamos la sentencia sql
                    sql = "UPDATE clientes_premium SET cuota_anual = ?, descuento = ?  WHERE cliente_id = ?";

                    // Generamos la consulta con autoclose.
                    try (PreparedStatement stmtPremium = getConnection(System.getenv("ENV")).prepareStatement(sql)) {

                        // Generamos el modelo para la actualización del Cliente Premium.
                        stmtPremium.setDouble(1, ((ClientePremium) cliente).getCuota());
                        stmtPremium.setDouble(2, ((ClientePremium) cliente).getDescuento());
                        stmtPremium.setLong(3, cliente.getId());

                        // Ejecutamos la consulta.
                        isSaved = stmtPremium.executeUpdate() > 0;
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
            sql = "INSERT INTO clientes (nombre, direccion_id, nif, email) VALUES (?, ?, ?, ?)";

            // Generamos la consulta con autoclose.
            try (PreparedStatement stmt = getConnection(System.getenv("ENV")).prepareStatement(sql)) {
                // Como se trata de un insert, primero debemos crear la Dirección asociada al cliente
                // manteniendo nuestra integridad referencial.
                Repositorio<Direccion> repoDireccion = new DireccionRepositorioImpl();

                // Generamos el modelo para la inserción del Cliente.
                stmt.setString(1, cliente.getNombre());
                // Insertamos la dirección y obtenemos el id generado.
                if (repoDireccion.save(cliente.getDomicilio())) {
                    stmt.setLong(2, Objects.requireNonNull(DireccionRepositorioImpl.getLast()).getId());
                } else {
                    System.out.println("No se ha podido crear la dirección");
                    return false;
                }
                stmt.setString(3, cliente.getNif());
                stmt.setString(4, cliente.getEmail());

                // Ejecutamos la consulta.
                isSaved = stmt.executeUpdate() > 0;

                // A continuación debemos identificar de qué tipo de Cliente se trata.
                // Para ello, utilizaremos el método tipoCliente() de la clase Cliente.
                flag = cliente.tipoCliente();

                switch (flag) {
                    // Para un cliente estandard debemos indicar el ID del cliente en la tabla de
                    // clientes estandard.
                    case "ClienteEstandard" -> {
                        // Creamos la sentencia SQL para la consulta.
                        sql = "INSERT INTO clientes_estandard (cliente_id) VALUES (?)";

                        // Generamos la consulta con autoclose.
                        try (PreparedStatement stmtEstandard = getConnection(System.getenv("ENV")).prepareStatement(sql)) {
                            // Generamos el modelo para la inserción del Cliente Estandard.
                            stmtEstandard.setLong(1, findOne(cliente.getNif()).getId());

                            // Ejecutamos la consulta.
                            isSaved = stmtEstandard.executeUpdate() > 0;
                        }
                    }
                    // Para un cliente premium debemos indicar el descuento, la cuota anual y el código
                    // de socio en la tabla de clientes premium.
                    case "ClientePremium" -> {
                        // Creamos la sentencia SQL para la consulta.
                        sql = "INSERT INTO clientes_premium (cliente_id, cuota_anual, descuento, cod_socio) " +
                                "VALUES (?, ?, ?, ?)";

                        // Generamos la consulta con autoclose.
                        try (PreparedStatement stmtPremium = getConnection(System.getenv("ENV")).prepareStatement(sql)) {
                            // Generamos el modelo para la inserción del Cliente Premium.
                            stmtPremium.setLong(1, findOne(cliente.getNif()).getId());
                            stmtPremium.setDouble(2, ((ClientePremium) cliente).getCuota());
                            stmtPremium.setDouble(3, ((ClientePremium) cliente).getDescuento());
                            stmtPremium.setString(4, ((ClientePremium) cliente).getCodSocio());

                            // Ejecutamos la consulta.
                            isSaved = stmtPremium.executeUpdate() > 0;
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

        // Creamos la sentencia SQL para la consulta. Recordamos que el id lo genera automáticamente la BD.
        String sql = "DELETE FROM clientes WHERE _id = ?";

        // Colocamos los recursos como argumentos del try-with-resources para que se cierren automáticamente.
        // Creamos la consulta a la BD mediante un PreparedStatement ya que recibimos un parámetro.
        try (PreparedStatement stmt = getConnection(System.getenv("ENV")).prepareStatement(sql)) {
            // Eliminamos el registro correspodiente a la dirección en la tabla correspondiente.
            Repositorio<Direccion> direccionRepo = new DireccionRepositorioImpl();
            direccionRepo.delete(findById(id).getDomicilio().getId());

            // Asignamos el parámetro a la consulta. La elección del parámetro se hace por su posición.
            stmt.setLong(1, id);

            // Ejecutamos la consulta.
            stmt.executeUpdate();

            // Decrecemos el número de clientes
            Cliente.decreaseTotalClientes();

            return true;

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

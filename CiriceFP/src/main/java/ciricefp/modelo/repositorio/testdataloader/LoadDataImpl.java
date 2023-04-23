package ciricefp.modelo.repositorio.testdataloader;

import ciricefp.modelo.utils.Conexion;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

/**
 * Esta clase implementa la lógica para realizar una carga inicial de datos de test en la BD.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public class LoadDataImpl implements LoadDataRepositorio {

    // Añadimos una clase para obtener los valores del archivo .env
    private static final Dotenv dotenv = Dotenv.load();

    // Comenzamos por usar un método para crear la conexión a la BBDD.
    private Connection getConnection(String tipo) {
        return Conexion.getInstance(tipo);
    }

    // Creamos un método para ejecutar la carag de datos, esta está scriptada dentro del sql en un procedimiento.
    @Override
    public int loadData () {

        // Creamos la sentencia sql para ejecutar el procedimiento.
        String sql = "call add_datos_test(?)";

        // Colocamos los recursos en un bloque try-with-resources y preparamas la ejecución de la sentencia.
        try (CallableStatement stmt = getConnection(dotenv.get("ENV")).prepareCall(sql)) {
            // Preparamaos el parámetro de salida.
            stmt.registerOutParameter(1, Types.INTEGER);

            // Ejecutamos la sentencia.
            stmt.executeUpdate();
            return stmt.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la carga de datos de test.");
            e.printStackTrace();
            return -1;
        }
    }

    // Comprobamos si existen datos en la BD.
    @Override
    public boolean checkData() {

        // Creamos la sentencia sql para ejecutar el procedimiento.
        String sql = "call check_datos(?)";

        // Colocamos los recursos en un bloque try-with-resources y preparamas la ejecución de la sentencia.
        try (CallableStatement stmt = getConnection(dotenv.get("ENV")).prepareCall(sql)) {
            // Preparamos el parámetro de salida.
            stmt.registerOutParameter(1, Types.NUMERIC);

            // Ejecutamos la sentencia.
            stmt.executeUpdate();
            return stmt.getBoolean(1);
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la carga de datos de test.");
            e.printStackTrace();
            return false;
        }
    }
}

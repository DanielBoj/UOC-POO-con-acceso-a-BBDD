package ciricefp.modelo.repositorio.testdataloader;

import ciricefp.modelo.utils.Conexion;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.sql.*;

/**
 * Esta clase implementa la lógica para realizar una carga inicial de datos de test en la BD.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public class LoadDataImpl implements LoadDataRepositorio {
    // Producto 4 -> Refactorizamos la clase para usar Entity Manager.
    // Creamos el atributo para nuestro Entity Manager.
    private final EntityManager em;

    // Seteamos nuestro Entity Manager a través del constructor.
    public LoadDataImpl(EntityManager em) {
        this.em = em;
    }

    /* Producto 4 ≥ Refactorizamos la clase para trabajar con Hibernate */

    /* Simplificamos al máximo la implementación de nuestro contrato con la interfaz ya que trasladaremos
     * toda la lógica a un servicio, para cumplir con las buenas prácticas. */

    // Añadimos una clase para obtener los valores del archivo .env
    /*private static final Dotenv dotenv = Dotenv.load();

    // Comenzamos por usar un método para crear la conexión a la BBDD.
    private Connection getConnection(String tipo) {
        return Conexion.getInstance(tipo);
    }*/

    // Creamos un método para ejecutar la carag de datos, esta está scriptada dentro del sql en un procedimiento.
    @Override
    public int loadData () {
        // Creamos la sentencia HQL/JPQL para ejecutar el procedimiento.
        StoredProcedureQuery query = em.createStoredProcedureQuery("add_datos_test")
                .registerStoredProcedureParameter(1, Integer.class, ParameterMode.OUT);
        // Ejecutamos la consulta y recogemos el valor de retorno.
        query.execute();

        // Capturamos el valor de retorno del procedimiento.
        return (int) query.getOutputParameterValue(1);

        /*// Creamos la sentencia sql para ejecutar el procedimiento.
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
        }*/
    }

    // Comprobamos si existen datos en la BD.
    @Override
    public boolean checkData() {
        // Creamos la sentencia HQL/JPQL para ejecutar el procedimiento.
        StoredProcedureQuery query = em.createStoredProcedureQuery("check_datos")
                .registerStoredProcedureParameter(1, Boolean.class, ParameterMode.OUT);

        // Ejecutamos la consulta y recogemos el valor de retorno.
        query.execute();

        // Capturamos el valor de retorno del procedimiento.
        return (boolean) query.getOutputParameterValue(1);

        /*// Creamos la sentencia sql para ejecutar el procedimiento.
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
        }*/
    }
}

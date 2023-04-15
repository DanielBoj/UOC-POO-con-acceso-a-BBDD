package ciricefp.modelo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Esta clase implementa la lógica para realizar el acceso a la base de datos.
 *
 * @author Cirice
 */
public class Conexion {

    private static String baseDatos;
    private static String login;
    private static String pass;
    private static String url;

    // Atributo Singleton
    private static Connection conn;

    // Opcional --> Lo usaremos si tenemos tiempo de implementar un sistema de encriptado del pass.
    private static String key;

    public Conexion(String tipoEntorno) {

        // Para la configuración capturamos variables env para seguir los protocolos de seguridad.
        // En el string url añadimos el parámetro
        // ?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Madrid
        // para evitar el error de la hora.
        if (tipoEntorno.equals("dev")) {
            baseDatos = "onlinestore_db";
            login = System.getenv("DB_LOCAL_USER");
            pass = System.getenv("DB_LOCAL_PASS");
            url = "jdbc:mysql://" + System.getenv("DB_LOCAL_URL") + "/" + baseDatos +
                    "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Madrid";//&noAccessToProcedureBodies=true";
        } else {
            baseDatos = "onlinestore_db";
            login = System.getenv("DB_PROD_USER");
            pass = System.getenv("DB_PROD_PASS");
            url = "jdbc:mysql://" + System.getenv("DB_PROD_URL") + "/" + baseDatos;
        }
        try {
            // Cargamos la conexión con un valor por defecto.
            conn = null;

            // Conectamos a la BD a través del Driver para MySQL
            conn = DriverManager.getConnection(url, login, pass);
            System.out.println("=====================================");
            System.out.println("Bienvenido a Matrix, Neo!");
            System.out.println("=====================================");
        } catch (SQLException e) {
            System.out.println("=====================================");
            System.out.println("Error al conectar con la base de datos");
            System.out.println("=====================================");
            e.printStackTrace();

            // Si sucede un error, nos aseguramos de que el recurso quede cerrado
            try {
                // Nos aseguramos de que la conexión no sea nula
                assert conn != null;
                // Cerramos la conexión
                conn.close();
                System.out.println("=====================================");
                System.out.println("Conexión cerrada");
                System.out.println("=====================================");
            } catch (SQLException eclose) {
                System.out.println("=====================================");
                System.out.println("Error al cerrar la conexión");
                System.out.println("=====================================");
                eclose.printStackTrace();
            }
        }
    }

    /* Getters & Setters */
    /* No van a haber setters para los atributos de la clase. Porque todos los valores
    ** se obtienen mediante env variables.
    */

    public String getBaseDatos() {
        return baseDatos;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        // TODO -> Codificar pass
        return pass;
    }

    public String getUrl() {
        return url;
    }

    public Connection getConn() {
        return conn;
    }

    // Queremos que el pass sea totalmente privado Todo Codificar pass

    @Override
    public String toString() {
        return "Conexion{" +
                "baseDatos='" + baseDatos + '\'' +
                ", login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                ", url='" + url + '\'' +
                ", conn='" + conn + '\'' +
                '}';
    }

    // Método Singleton para realizar la conexión a la BD.
    public static Connection getInstance(String tipoEntorno) {
        if (conn == null) {

            // Si el tipo de entorno es dev, se conecta a la BD de desarrollo.
            // Si no, se conecta a la BD de producción.
            if (tipoEntorno.equals("dev")) {
                new Conexion("dev");
            } else {
                new Conexion("prod");
            }
        }
        return conn;
    }

    // Método para cerrar la conexión a la BD.
    public static int closeConnection() {
        try {
            assert conn != null;
            conn.close();
            System.out.println("=====================================");
            System.out.println("Matrix cerrada, Neo!");
            System.out.println("=====================================");
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }
}

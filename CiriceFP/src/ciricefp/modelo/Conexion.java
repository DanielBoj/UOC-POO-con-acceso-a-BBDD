package ciricefp.modelo;

/**
 * Esta clase implementa la lógica para realizar el acceso a la base de datos.
 *
 * @author Cirice
 */
public class Conexion {

    private String baseDatos;
    private String login;
    private String pass;
    private String url;
    private String conn;

    // Opcional --> Lo usaremos si tenemos tiempo de implementar un sistema de encriptado del pass.
    private String key;

    public Conexion(String baseDatos, String login, String pass, String url, String conn) {
        this.baseDatos = baseDatos;
        this.login = login;
        this.pass = pass;
        this.url = url;
        this.conn = conn;
        // TODO --> this.key = Capturar de variable de entorno.
    }

    public String getBaseDatos() {
        return baseDatos;
    }

    public void setBaseDatos(String baseDatos) {
        this.baseDatos = baseDatos;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getConn() {
        return conn;
    }

    public void setConn(String conn) {
        this.conn = conn;
    }

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
}

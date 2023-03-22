package ciricefp.modelo;

/**
 * Interfaz para la generación de códigos hash. Lo usarán las clases ClientePremium y Artículo.
 *
 * @author Cirice
 */
public interface HashCode {

    String generateCodigo(String key);

    static String generateHash(String key) {
        // TODO --> Obtener código hash.
        // TODO --> Comprimir código hash.
        return "";
    }

    static String manageColission(String hash) {
        // TODO --> Manejar colisiones.
        return "";
    }
}

package ciricefp.modelo.interfaces;

/**
 * Interfaz para la generación de códigos hash. Lo usarán las clases ClientePremium y Artículo.
 *
 * @author Cirice
 */
public interface HashCode {
    // Tamaño máximo de códigos hash, lo usaremos como valor de compresión.
    int CODE_SIZE = 10000;
    // Método a implementar por las clases que implementen esta interfaz.
    String generateCodigo(String key);
    // TODO --> String manageColission(String hash);

    // Generamos el Hash mediante el método hashCode() de la clase String.
    static String generateHash(String key) {
        // Obtener código hash.
        int hash = key.hashCode();

        // Normalizar y retornar el código hash comprimido.
        hash = hash & 0x70000000;

        return Integer.toString(hash % CODE_SIZE);
    }
}

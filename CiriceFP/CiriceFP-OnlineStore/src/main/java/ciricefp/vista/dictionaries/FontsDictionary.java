package ciricefp.vista.dictionaries;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.HashMap;

/* Declaramos una clase estática para crear un diccionario con las fuentes personalizadas
 * que usaremos en nuestros estilos. */
public class FontsDictionary {
    private static final HashMap<String, Font> fontsDictionary = new HashMap<>();

    // Cargamos las fuentes en el diccionario
    static {
        addFont("title", Font.font("Roboto", FontWeight.BOLD, 20));
        addFont("header", Font.font("Roboto", FontWeight.BOLD, 16));
        addFont("subtitle", Font.font("Segoe UI", 14));
        addFont("text", Font.font("Segoe UI", 11));
        addFont("button", Font.font("Segoe UI", FontWeight.BOLD, 12));
        addFont("label", Font.font("Segoe UI", 10));
        addFont("warning", Font.font("Robot", FontWeight.BOLD, FontPosture.ITALIC, 11));
    }

    // Método para añadir una fuente al diccionario
    private static void addFont(String name, Font font) { fontsDictionary.put(name, font); }

    // Método para obtener una fuente del diccionario
    public static Font getFont(String name) { return fontsDictionary.get(name); }
}

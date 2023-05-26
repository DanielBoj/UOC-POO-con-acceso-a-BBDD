package ciricefp.vista.dictionaries;

import javafx.scene.paint.Color;
import java.util.HashMap;

/* Declaramos una clase estática para crear un diccionario con los colores personalizados
 * que usaremos en nuestros estilos. */
public class ColorsDictionary {
    private static final HashMap<String, Color> colorsDictionary = new HashMap<>();

    // Creamos el diccionario de colores, contendrá un String con el nombre del color y un String con el código hexadecimal
    static {
        addColor("background-dark", "#222831");
        addColor("background-medium", "#393e46");
        addColor("background-light", "#00adb5");
        addColor("orange", "#f96d00");
        addColor("orange-light", "#f5a855");
        addColor("text-light", "#f2f2f2");
        addColor("text-dark", "#222831");
        addColor("border-dark", "#222831");
        addColor("border-orange", "#f96d00");
    }

    // Método para cargar los colores en el diccionario
    private static void addColor(String name, String hexValue) {
        colorsDictionary.put(name, Color.valueOf(hexValue));
    }

    // Getter de colores
    public static Color getColor(String name) { return colorsDictionary.get(name); }
}

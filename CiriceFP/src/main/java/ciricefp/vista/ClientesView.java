package ciricefp.vista;

import ciricefp.modelo.Cliente;
import ciricefp.vista.dictionaries.ColorsDictionary;
import ciricefp.vista.dictionaries.FontsDictionary;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ClientesView {
    private static final Integer DEFAULT_WIDTH = 800;
    private static final Integer DEFAULT_HEIGHT = 600;
    // Obtenemos los rem para no trabajar con pixels
    private static final double rem = new Text("").getBoundsInParent().getHeight();
    private Pane pane;

    // Cargamos el controlador
    private ClientesController controller = new ClientesController();

    // Constructor por defecto
    public ClientesView() {
        pane = generatePane();
    }

    // Generamos la vista
    private static Pane generatePane() {
        // Construimos el layout
        GridPane root = new GridPane();
        root.setPadding(new Insets(0.5 * rem));
        root.setHgap(0.2 * rem);
        root.setVgap(0.2 * rem);
        // Establecemos el color de fondo y un borde al panel
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        root.setBorder(new Border(new BorderStroke(ColorsDictionary.getColor("border-light"), BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        // Submenú de opciones
        Pane subMenu = new VBox(3);
        subMenu.setPadding(new Insets(0.5 * rem));
        // Contenido
        Pane content = new GridPane();
        content.setPadding(new Insets(0.5 * rem));

        // Creamos los elementos
        // Generamos el título de la vista
        Text title = new Text("Clientes");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("title"));

        // Botones del submenú
        Button btnListar = new Button("Listar clientes");
        btnListar.setFont(FontsDictionary.getFont("button"));
        Button btnAnadir = new Button("Añadir un cliente");
        btnAnadir.setFont(FontsDictionary.getFont("button"));
        Button btnBuscar = new Button("Buscar cliente por NIF");
        btnBuscar.setFont(FontsDictionary.getFont("button"));
        Button btnFiltrar = new Button("Filtrar clientes por tipo");
        btnFiltrar.setFont(FontsDictionary.getFont("button"));

        // Añadimos los botones a la sección de submenú
        subMenu.getChildren().addAll(btnListar, btnAnadir, btnBuscar, btnFiltrar);

        // Sección de contenido
        Text contentText = new Text("Aquí irá el contenido");

        // Añádimos el contenido
        content.getChildren().add(contentText);


        // Añadimos los elementos al layout root
        root.add(title, 0, 0, 6, 2);
        root.add(subMenu, 0, 2, 2, 6);
        root.add(content, 3, 2, 6, 10);

        // Devolvemos el layout
        return root;
    }

    // Obtenemos el panel
    public Pane getPane() {
        return pane;
    }

    // Obtenemos una lista de los clientes
    private static ArrayList<Cliente> showClientes() {
        return null;
    }

}

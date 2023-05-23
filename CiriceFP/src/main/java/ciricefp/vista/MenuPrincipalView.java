package ciricefp.vista;

import ciricefp.vista.dictionaries.ColorsDictionary;
import ciricefp.vista.dictionaries.FontsDictionary;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/* Generamos una Scene para cargar el menú principal de la aplicación.
* Contendrá los 3 botones para acceder a los 3 submenús/ */
public class MenuPrincipalView {
    private static final Integer DEFAULT_WIDTH = 800;
    private static final Integer DEFAULT_HEIGHT = 600;
    // Obtenemos los rem para no trabajar con pixels
    private static final double rem = new Text("").getBoundsInParent().getHeight();
    private final Scene scene;
    private static MenuPrincipalController  controller = new MenuPrincipalController();

    public MenuPrincipalView(MenuPrincipalController srcController) {
        controller = srcController;
        scene = generateScene();
    }

    public Scene getScene() {
        return scene;
    }

    private static Scene generateScene() {
        // Empezamos por crear el layout
        // Usamos un BorderPane como componente raíz
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(1 * rem));
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);

        // En la parte superior irá el contenedor de navegación con el título y, abajo, una línea de botones
        // Creamos el contenedor de navegación
        Pane navSection = new VBox(0.5 * rem);
        root.setPadding(new Insets(0.5 * rem));
        Pane menu = new HBox(0.5 * rem);
        menu.setPadding(new Insets(0.5 * rem));

        // En la zona central irá el contenedor de contenido
        Pane contentSection = new StackPane();
        contentSection.setPadding(new Insets(1.5 * rem));

        // Creamos los elementos de la vista
        // Generamos el título de la vista
        Text title = new Text("Menú Principal");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("title"));

        // Generamos la sección de navegación
        // Creamos el banner
        // Capturamos la imagen que queremos mostrar
        Image logo = new Image("file:src/main/resources/assets/logo-cirice.png");
        // Creamos el componente que mostrará la imagen
        ImageView banner = new ImageView(logo);
        // Ajustamos el tamaño de la imagen
        banner.getStyleClass().add("banner");
        banner.setFitWidth(3 * rem);
        banner.setPreserveRatio(true);

        // Generamos los botones de acceso a los submenús ->
        // Mediante el evento setOnAction, cargamos un panel diferente en la zona central
        Button btArticulos = new Button("Artículos");
        btArticulos.setFont(FontsDictionary.getFont("button"));
        // Cargamos la vista de Artículos
        btArticulos.setOnAction(event -> {
            Pane articulosView = new ArticulosView(controller).getPane();
            contentSection.getChildren().clear();
            contentSection.getChildren().add(articulosView);
        });
        Button btClientes = new Button("Clientes");
        btClientes.setFont(FontsDictionary.getFont("button"));
        // Cargamos la vista de Clientes
        btClientes.setOnAction(event -> {
            Pane clientesView = new ClientesView().getPane();
            contentSection.getChildren().clear();
            contentSection.getChildren().add(clientesView);
        });
        Button btPedidos = new Button("Pedidos");
        btPedidos.setFont(FontsDictionary.getFont("button"));
        // Cargamos la vista de Pedidos
        btPedidos.setOnAction(event -> {
            Pane pedidosView = new PedidosView().getPane();
            contentSection.getChildren().clear();
            contentSection.getChildren().add(pedidosView);
        });

        // Cargamos los elementos en el contenedor de navegación
        menu.getChildren().addAll(banner, btArticulos, btClientes, btPedidos);
        navSection.getChildren().addAll(getMenu(),title, menu);

        // Creamos un contenido central por defecto, en este caso, el banner con el logo
        contentSection.getChildren().add(new ImageView(logo));

        // Creamos la vista general y la cargamos
        root.setTop(navSection);
        root.setCenter(contentSection);

        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        // Establecemos un fondo de color
        scene.setFill(ColorsDictionary.getColor("background-dark"));
        // Cargamos la hoja de estilos
        scene.getStylesheets().add("file:src/main/resources/styles.css");

        return scene;
    }

    public static MenuBar getMenu() {
        // Creamos el menú principal
        Menu fileMenu = new Menu("Archivo");

        // Añadimos el ítem del menú
        MenuItem exitItem = new MenuItem("_Salir");
        // Configuramos el evento
        exitItem.setOnAction(event -> Platform.exit());

        // Añadimos el ítem al menú
        fileMenu.getItems().add(exitItem);

        // Generamos la barra del menú
        MenuBar mainMenu = new MenuBar(fileMenu);

        return mainMenu;
    }
}

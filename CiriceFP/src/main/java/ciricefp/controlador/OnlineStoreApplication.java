package ciricefp.controlador;

import ciricefp.modelo.Datos;
import ciricefp.modelo.utils.ConexionJpa;
import ciricefp.vista.MenuPrincipalController;
import ciricefp.vista.MenuPrincipalView;
import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/* Está clase es el nuevo controlador principal de las vistas ya que se encarga de generar la GUI de la aplicación. */
public class OnlineStoreApplication extends Application {
    // Valores por defecto
    private static final String DEFAULT_TITLE = "Online Store";
    private static final Integer DEFAULT_WIDTH = 1280;
    private static final Integer DEFAULT_HEIGHT = 720;
    // Obtenemos los rem para no trabajar con pixels
    private static final double rem = new Text("").getBoundsInParent().getHeight();
    // Modelo MVC
    private Datos datos;
    private MenuPrincipalController menuController;
    private Controlador controlador;
    private static int exitValue = 0;
    @Override
    public void start(Stage primaryStage) {
        // Instanciamos el modelo MVC
        datos = new Datos();
        menuController = new MenuPrincipalController();
        controlador = new Controlador(datos, menuController);
        menuController.setControlador(controlador);
        datos.setControlador(controlador);

        // Instanciamos la conexión a la base de datos
        // Producto 4 -> Instanciamos el Entity Manager para poder conectarnos a la BD.
        EntityManager em = ConexionJpa.getEntityManagerFactory();
        datos.setEm(em);

        // Cargamos los datos inciales
        // Actualizamos los datos que necesitan las funciones automatizadas de la app
        // desde la BD.
        try {
            controlador.actualizarCodigosUnicos();
            controlador.actualizarContadores();
        } catch (Exception e) {
            System.out.println("Error al actualizar las listas de datos.");
            e.printStackTrace();
            exitValue = 2;
        }

        try {
            // Obtenemos la vista principal
            MenuPrincipalView menu = new MenuPrincipalView(menuController);

            // Configuramos la ventana
            primaryStage.setTitle(DEFAULT_TITLE);
            primaryStage.setWidth(DEFAULT_WIDTH);
            primaryStage.setHeight(DEFAULT_HEIGHT);
            // Añadimos la vista principal a la ventana
            primaryStage.setScene(menu.getScene());
            primaryStage.show();

        } catch (Exception e) {
            System.out.println("Error al cargar la vista principal");
            e.printStackTrace();
            exitValue = exitValue * 10 + 3;
        }

        // Cerramos la conexión a la BD cuando se cierra la app.
        primaryStage.setOnCloseRequest(event -> {
            try {
                this.stop();
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexión a la BD");
                e.printStackTrace();
                exitValue = exitValue * 10 + 4;
            }
        });
    }

    // Método main para lanzar la aplicación.
    public static void main(String[] args) {
        try {
            Application.launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            exitValue = exitValue * 10 + 5;
            System.exit(exitValue);
        }
    }

    // Cierre de la aplicación
    @Override
    public void stop() throws Exception {
        if (datos.getEm().isOpen()) datos.getEm().close();
        super.stop();
        System.exit(exitValue);
    }
}

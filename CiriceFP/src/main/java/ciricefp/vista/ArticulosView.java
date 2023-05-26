package ciricefp.vista;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.interfaces.IArticulo;
import ciricefp.vista.controladores.ArticulosController;
import ciricefp.vista.controladores.MenuPrincipalController;
import ciricefp.vista.dictionaries.ColorsDictionary;
import ciricefp.vista.dictionaries.FontsDictionary;
import ciricefp.vista.interfaces.IArticuloController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.hibernate.cfg.PropertyHolderBuilder;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Vista principal para el menú de Artículos, va a permitir listar los artículos, añadir un nuevo artículo
 * o eliminarlos.
 *
 * @author Cirice
 * @version 1.1
 * @since 05-2023
 * @see ArticulosController
 * @see MenuPrincipalController
 * @see Articulo
 */
public class ArticulosView {
    // Obtenemos los rem para no trabajar con pixels
    private static final double rem = new Text("").getBoundsInParent().getHeight();
    private final Pane pane;
    // Cargamos el controlador de la vista con los métodos que necesitamos
    private static IArticuloController controller;

    // Constructor por defecto
    public ArticulosView(MenuPrincipalController menuController) {
        controller = new ArticulosController(menuController);
        pane = generatePane();
    }

    // Constructor vacío
    public ArticulosView() {
        controller = new ArticulosController();
        pane = generatePane();
    }

    // Creamos la vista principal
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
        VBox subMenu = new VBox(3);
        subMenu.setPadding(new Insets(0.8 * rem));
        // Espaciamos los elementos
        subMenu.setSpacing(0.8 * rem);
        // Contenido
        Pane content = new GridPane();
        content.setPadding(new Insets(0.8 * rem));

        // Creamos los elementos
        // Generamos el título de la vista
        Text title = new Text("Artículos");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("title"));

        // Botones del submenú
        Button btnListar = new Button("Listar artículos");
        btnListar.setFont(FontsDictionary.getFont("button"));
        // Mostramos los artículos con el método showArticulos()
        btnListar.setOnAction(e -> {
            content.getChildren().clear();
            content.getChildren().add(showArticulos());
        });

        Button btnAnadir = new Button("Añadir un articulo");
        btnAnadir.setFont(FontsDictionary.getFont("button"));
        // Mostramos el formulario de creación de un nuevo artículo con addForm()
        btnAnadir.setOnAction(e -> {
            content.getChildren().clear();
            content.getChildren().add(addForm());
        });
        Button btnEliminar = new Button("Eliminar todos los artículos");
        btnEliminar.setFont(FontsDictionary.getFont("button"));
        // Eliminamos todos los artículos con el método deleteAll()
        btnEliminar.setOnAction(e -> {
            content.getChildren().clear();
            content.getChildren().add(deleteArticulos());
        });

        // Añadimos los botones a la sección de submenú
        subMenu.getChildren().addAll(btnListar, btnAnadir, btnEliminar);

        // Añádimos el contenido
        content.getChildren().add(showArticulos());

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

    // Card para mostrar la información de los artículos
    private static Pane showArticulos() {
        // Obtenemos la lista de artículos
        ArrayList<Articulo> articulos = new ArrayList<>();
        try {
            articulos = controller.listArticulos().orElseGet(() -> {
                throwErrorPane("lista");
                return new ArrayList<>();
            });
        } catch (Exception e) {
            throwErrorPane("excepcion");
            e.printStackTrace();
        }

        // Creamos el panel
        VBox root = new VBox();
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Creamos el panel para mostrar la información
        GridPane content = new GridPane();
        // Configuramos el pane en los mapeadores

        // Creamos los elementos
        // Generamos el título de la vista
        Text title = new Text("Listado de artículos");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("subtitle"));

        // Creamos la línea de botones
        // Botón anterior --> Se muestra si no es el primer artículo
        Button btnAnterior = new Button("Anterior");
        // Botón siguiente --> Se muestra si no es el último artículo
        Button btnSiguiente = new Button("Siguiente");
        // Creamos el panel para los botones
        HBox buttons = new HBox();
        buttons.setSpacing(0.5 * rem);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(btnAnterior, btnSiguiente);

        // Mostraremos un artículo en el pane y crearemos un botón para ir al siguiente
        // artículo
        // Creamos el panel para mostrar la información
        if (articulos.size() > 0) {
            final int[] ind = {0};

            // Convertimos articulos en un Final para poder usarlo en los listeners
            final ArrayList<Articulo> finalArticulos = articulos;

            // Mostramos el primer artículo
            content.getChildren().add(mapArticulo(finalArticulos.get(ind[0])));

            // Lógica anterior
            btnAnterior.setFont(FontsDictionary.getFont("button"));
            // Ocultamos el botón anterior si es el primer artículo
            btnAnterior.setVisible(false);
            btnAnterior.setOnAction(event -> {
                // Limpiamos los campos
                content.getChildren().remove(0, content.getChildren().size());
                if (ind[0] > 0) {
                    ind[0]--;
                    // Actualizamos el artículo
                    content.getChildren().add(mapArticulo(finalArticulos.get(ind[0])));

                    // Mostramos el botón siguiente si no es el último artículo
                    btnSiguiente.setVisible(ind[0] < finalArticulos.size() - 1);

                    // Ocultamos el botón si es el primer artículo
                    btnAnterior.setVisible(ind[0] > 0);
                }
            });

            // Lógica siguiente
            btnSiguiente.setFont(FontsDictionary.getFont("button"));
            // Si no es el último artículo, se muestra el botón
            btnSiguiente.setVisible(ind[0] < articulos.size() - 1);
            btnSiguiente.setOnAction(event -> {
                // Limpiamos los campos
                content.getChildren().remove(0, content.getChildren().size());
                if (ind[0] < finalArticulos.size() - 1) {
                    ind[0]++;
                    // Actualizamos el artículo
                    content.getChildren().add(mapArticulo(finalArticulos.get(ind[0])));

                    btnAnterior.setVisible(true);

                    // Ocultamos el botón si es el último artículo
                    btnSiguiente.setVisible(ind[0] < finalArticulos.size() - 1);
                }
            });

            // Añadimos el título y el contenido al panel
            root.getChildren().addAll(title, content, buttons);

        } else {
            // Si no hay artículos, mostramos un mensaje
            Text noArticulos = new Text("No hay artículos que mostrar.");
            noArticulos.setFont(FontsDictionary.getFont("subtitle"));
            noArticulos.setFill(ColorsDictionary.getColor("text-light"));
            root.getChildren().add(noArticulos);
        }
        return root;
    }

    // Formulario para añadir un artículo
    private static Pane addForm() {
        // Flag de validación observable -> Necesitamos 4 validaciones distintas
        Property<Boolean[]> validators = new SimpleObjectProperty<>(new Boolean[4]);
        // Seteamos los valores a false
        Arrays.fill(validators.getValue(), false);

        // Flag para mostrar la card
        Property<Boolean> showCard = new SimpleObjectProperty<>(false);

        // Construimos el layout
        VBox root = new VBox();
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Creamos los elementos
        // Generamos el título de la vista
        Text title = new Text("Añadir un artículo");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("subtitle"));

        // Creamos el formulario
        GridPane form = new GridPane();
        form.setPadding(new Insets(0.5 * rem));
        form.setHgap(0.8 * rem);
        form.setVgap(0.8 * rem);

        // Creamos la card para mostrar el resultado
        GridPane card = new GridPane();
        card.setPadding(new Insets(0.5 * rem));
        // Por defecto, la card está oculta
        card.setVisible(false);

        // Creamos los elementos del formulario
        // Descripción
        Label lblDescripcion = new Label("Descripción");
        lblDescripcion.setTextFill(ColorsDictionary.getColor("text-light"));
        lblDescripcion.setFont(FontsDictionary.getFont("label"));
        lblDescripcion.setAlignment(Pos.CENTER_LEFT);
        TextField txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Descripción del artículo");
        txtDescripcion.setAlignment(Pos.CENTER_LEFT);
        txtDescripcion.setPrefColumnCount(20);
        txtDescripcion.setFont(FontsDictionary.getFont("text"));
        // Validamos que haya texto
        txtDescripcion.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                txtDescripcion.setStyle("-fx-border-color: green;");
                // Tenemos que cambiar el valor de validators, no del elemento aislado para que el listener funcione
                validators.setValue(new Boolean[]{true, validators.getValue()[1], validators.getValue()[2], validators.getValue()[3]});
            } else {
                txtDescripcion.setStyle("-fx-border-color: red;");
            }
        });
        // Precio
        Label lblPrecio = new Label("Precio");
        lblPrecio.setTextFill(ColorsDictionary.getColor("text-light"));
        lblPrecio.setFont(FontsDictionary.getFont("label"));
        lblPrecio.setAlignment(Pos.CENTER_LEFT);
        TextField txtPrecio = new TextField();
        txtPrecio.setFont(FontsDictionary.getFont("text"));
        txtPrecio.setPromptText("Precio del artículo");
        txtPrecio.setAlignment(Pos.CENTER_LEFT);
        txtPrecio.setPrefColumnCount(20);
        lblPrecio.setLabelFor(txtPrecio);
        // Validamos que el precio sea un número
        txtPrecio.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Double.parseDouble(newValue);
                txtPrecio.setStyle("-fx-border-color: green;");
                // Tenemos que cambiar el valor de validators, no del elemento aislado para que el listener funcione
                validators.setValue(new Boolean[]{validators.getValue()[0], true, validators.getValue()[2], validators.getValue()[3]});
            } catch (NumberFormatException e) {
                txtPrecio.setStyle("-fx-border-color: red;");
            }
        });
        // Gastos de envío
        Label lblGastosEnvio = new Label("Gastos de envío");
        lblGastosEnvio.setTextFill(ColorsDictionary.getColor("text-light"));
        lblGastosEnvio.setFont(FontsDictionary.getFont("label"));
        lblGastosEnvio.setAlignment(Pos.CENTER_LEFT);
        TextField txtGastosEnvio = new TextField();
        txtGastosEnvio.setFont(FontsDictionary.getFont("text"));
        txtGastosEnvio.setPromptText("Gastos de envío del artículo");
        txtGastosEnvio.setAlignment(Pos.CENTER_LEFT);
        txtGastosEnvio.setPrefColumnCount(20);
        lblGastosEnvio.setLabelFor(txtGastosEnvio);
        // Validamos que los gastos de envío sean un número
        txtGastosEnvio.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Double.parseDouble(newValue);
                txtGastosEnvio.setStyle("-fx-border-color: green;");
                validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], true, validators.getValue()[3]});
            } catch (NumberFormatException e) {
                txtGastosEnvio.setStyle("-fx-border-color: red;");
            }
        });
        // Días de preparación
        Label lblDiasPreparacion = new Label("Días de preparación");
        lblDiasPreparacion.setTextFill(ColorsDictionary.getColor("text-light"));
        lblDiasPreparacion.setFont(FontsDictionary.getFont("label"));
        lblDiasPreparacion.setAlignment(Pos.CENTER_LEFT);
        TextField txtDiasPreparacion = new TextField();
        txtDiasPreparacion.setFont(FontsDictionary.getFont("text"));
        txtDiasPreparacion.setPromptText("Días de preparación del artículo");
        txtDiasPreparacion.setAlignment(Pos.CENTER_LEFT);
        txtDiasPreparacion.setPrefColumnCount(20);
        lblDiasPreparacion.setLabelFor(txtDiasPreparacion);
        // Validamos que los días de preparación sean un número
        txtDiasPreparacion.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Integer.parseInt(newValue);
                txtDiasPreparacion.setStyle("-fx-border-color: green;");
                validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], validators.getValue()[2], true});
            } catch (NumberFormatException e) {
                txtDiasPreparacion.setStyle("-fx-border-color: red;");
            }
        });

        // Botón de añadir
        Button btnAnadir = new Button("Añadir");
        btnAnadir.setFont(FontsDictionary.getFont("button"));
        // Cargamos los datos y los enviamos al controlador
        btnAnadir.setOnAction(event -> {
            String description = txtDescripcion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            double gastosEnvio = Double.parseDouble(txtGastosEnvio.getText());
            int diasPreparacion = Integer.parseInt(txtDiasPreparacion.getText());

            // Enviamos los datos al controlador
            if (controller.addArticulo(description, precio, gastosEnvio, diasPreparacion)) {
                // Si se ha añadido correctamente, limpiamos los campos
                txtDescripcion.clear();
                txtPrecio.clear();
                txtGastosEnvio.clear();
                txtDiasPreparacion.clear();

                // Mostramos el mensaje de éxito
                throwExitPane("crear");

                // Mostramos una card con el nuevo pedido
                ArrayList<Articulo> articulos = controller.listArticulos().orElseGet(ArrayList::new);
                card.getChildren().add(mapArticulo(articulos.get(articulos.size() - 1)));
                showCard.setValue(true);
            } else {
                // Si no se ha añadido correctamente, mostramos el mensaje de error
                throwErrorPane("crear");
            }
        });
        // Por defecto, el botón de añadir estará desactivado
        btnAnadir.setDisable(true);
        //System.out.println(Arrays.stream(validators.getValue()).findAny().map(value -> !value).orElse(false));
        // Si las validaciones son correctas, el botón de añadir se activará
        validators.addListener(
                (observable, oldValue, newValue) -> btnAnadir.setDisable(!Arrays.stream(validators.getValue()).allMatch(value -> value))
        );

        // Botón de cancelar
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setFont(FontsDictionary.getFont("button"));
        // Limpiamos los campos del formulario
        btnCancelar.setOnAction(event -> {
            txtDescripcion.clear();
            txtPrecio.clear();
            txtDiasPreparacion.clear();
        });

        // Añadimos los elementos al formulario
        form.add(lblDescripcion, 0, 0);
        form.add(txtDescripcion, 1, 0);
        form.add(lblPrecio, 0, 1);
        form.add(txtPrecio, 1, 1);
        form.add(lblGastosEnvio, 0, 2);
        form.add(txtGastosEnvio, 1, 2);
        form.add(lblDiasPreparacion, 0, 3);
        form.add(txtDiasPreparacion, 1, 3);
        form.add(btnAnadir, 0, 4);
        form.add(btnCancelar, 1, 4);

        showCard.addListener((observable, oldValue, newValue) -> {
            if (newValue) card.setVisible(true);
        });

        // Añadimos los elementos al layout root
        root.getChildren().addAll(title, form, card);
        return root;
    }

    // Eliminamos los artículos
    private static Pane deleteArticulos() {
        VBox root = new VBox();
        root.setPadding(new Insets(0.8 * rem));
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Creamos el título
        VBox title = new VBox();
        title.setPadding(new Insets(0.8 * rem));
        title.setBackground(new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY)));
        title.setSpacing(0.5 * rem);
        Text titleText = new Text("Eliminar artículos");
        titleText.setFont(FontsDictionary.getFont("title"));
        titleText.setFill(ColorsDictionary.getColor("text-light"));
        Text subtitleText = new Text("Solo se eliminarán los artículos que no estén en ningún pedido");
        subtitleText.setFont(FontsDictionary.getFont("subtitle"));
        subtitleText.setFill(ColorsDictionary.getColor("text-light"));
        title.getChildren().addAll(titleText, subtitleText);

        Pane resultPane = new Pane();

        // Creamos el botón para eliminar los artículos
        Button btnEliminar = new Button("Eliminar artículos");
        btnEliminar.setFont(FontsDictionary.getFont("button"));
        btnEliminar.setOnAction(event -> {
            controller.clearArticulos().ifPresentOrElse(data -> {
                // Si se ha eliminado correctamente, mostramos el mensaje de éxito
                throwExitPane("eliminar");
                // Mostramos el resultado
                resultPane.getChildren().clear();
                Text resultText = new Text("Se han eliminado " + data + " artículos");
                resultText.setFont(FontsDictionary.getFont("text"));
                resultText.setFill(ColorsDictionary.getColor("text-light"));
                resultPane.getChildren().add(resultText);

            }, () -> {
                // Si no se ha eliminado correctamente, mostramos el mensaje de error
                throwErrorPane("eliminar");
                // Mostramos el resultado
                resultPane.getChildren().clear();
                Text resultText = new Text("No se ha eliminado ningún artículo");
                resultText.setFont(FontsDictionary.getFont("text"));
                resultText.setFill(ColorsDictionary.getColor("text-light"));
                resultPane.getChildren().add(resultText);
            });
        });

        // Añadimos los elementos al layout root
        root.getChildren().addAll(title, btnEliminar, resultPane);
        return root;
    }

    // Ventana de error
    private static void throwErrorPane(String type) {
        // Creamos el diálogo
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Ha habido un error");

        // Creamos el mensaje
        switch (type) {
            case "lista" -> errorAlert.setContentText("No se ha podido obtener la lista de artículos");
            case "crear" -> errorAlert.setContentText("No se ha podido añadir el artículo");
            case "eliminar" -> errorAlert.setContentText("No se ha podido eliminar el artículo");
            case "excepcion" -> errorAlert.setContentText("Ha ocurrido un error no definido");
            default -> errorAlert.setContentText("Ha ocurrido un error");
        }
        // Estilamos el diálogo
        errorAlert.getDialogPane().getStylesheets().add("file:src/main/resources/styles.css");

        // Mostramos el diálogo
        errorAlert.showAndWait();
    }

    // Ventana de éxito
    private static void throwExitPane(String type) {
        // Creamos el diálogo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Artículos");

        // Creamos el mensaje
        switch (type) {
            case "crear" -> alert.setContentText("Se ha creado el artículo correctamente");
            case "eliminar" -> alert.setContentText("Se ha eliminado el artículo correctamente");
        }

        // Estilamos el diálogo
        alert.getDialogPane().getStylesheets().add("file:src/main/resources/styles.css");

        // Mostramos el diálogo
        alert.showAndWait();
    }

    // Mapeamos el artículo
    private static Pane mapArticulo(Articulo srcArticulo) {
        // Creamos el panel
        GridPane content = new GridPane();
        content.setPadding(new Insets(0.5 * rem));
        content.setHgap(0.2 * rem);
        content.setVgap(0.2 * rem);

        // Creamos los elementos
        // Creamos los elementos del panel
        // Código
        Label lblCodigo = new Label("Código");
        lblCodigo.setFont(FontsDictionary.getFont("label"));
        lblCodigo.setTextFill(ColorsDictionary.getColor("text-light"));
        Text codigo = new Text(srcArticulo.getCodArticulo());
        lblCodigo.setLabelFor(codigo);
        // Descripción
        Label lblDescripcion = new Label("Descripción");
        lblDescripcion.setFont(FontsDictionary.getFont("label"));
        lblDescripcion.setTextFill(ColorsDictionary.getColor("text-light"));
        Text descripcion = new Text(srcArticulo.getDescripcion());
        lblDescripcion.setLabelFor(descripcion);
        // Precio
        Label lblPrecio = new Label("Precio");
        lblPrecio.setFont(FontsDictionary.getFont("label"));
        lblPrecio.setTextFill(ColorsDictionary.getColor("text-light"));
        Text precio = new Text(Double.toString(srcArticulo.getPvp()) + "€");
        lblPrecio.setLabelFor(precio);
        // Gastos de envío
        Label lblGastos = new Label("Gastos de envío");
        lblGastos.setFont(FontsDictionary.getFont("label"));
        lblGastos.setTextFill(ColorsDictionary.getColor("text-light"));
        Text gastos = new Text(Double.toString(srcArticulo.getGastosEnvio()) + "€");
        lblGastos.setLabelFor(gastos);
        // Días de preparación
        Label lblDias = new Label("Días de preparación");
        lblDias.setFont(FontsDictionary.getFont("label"));
        lblDias.setTextFill(ColorsDictionary.getColor("text-light"));
        Text dias = new Text(Integer.toString(srcArticulo.getTiempoPreparacion()));
        lblDias.setLabelFor(dias);

        // Añadimos los elementos al panel
        content.add(lblCodigo, 0, 0);
        content.add(codigo, 1, 0);
        content.add(lblDescripcion, 0, 1);
        content.add(descripcion, 1, 1);
        content.add(lblPrecio, 0, 2);
        content.add(precio, 1, 2);
        content.add(lblGastos, 0, 3);
        content.add(gastos, 1, 3);
        content.add(lblDias, 0, 4);
        content.add(dias, 1, 4);

        return content;
    }
}

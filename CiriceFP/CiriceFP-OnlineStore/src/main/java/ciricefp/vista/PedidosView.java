package ciricefp.vista;

import ciricefp.modelo.ClientePremium;
import ciricefp.modelo.Pedido;
import ciricefp.vista.controladores.ArticulosController;
import ciricefp.vista.controladores.ClientesController;
import ciricefp.vista.controladores.MenuPrincipalController;
import ciricefp.vista.controladores.PedidosController;
import ciricefp.vista.dictionaries.ColorsDictionary;
import ciricefp.vista.dictionaries.FontsDictionary;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class PedidosView {
    // Obtenemos los rem para no trabajar con pixels
    private static final double rem = new Text("").getBoundsInParent().getHeight();
    private final Pane pane;
    // Cargamos el controlador
    private static PedidosController controller;

    // Constructor por defecto
    public PedidosView(MenuPrincipalController menuController) {
        controller = new PedidosController(menuController);
        pane = generatePane();
    }
    // Constructor vacío
    public PedidosView() {
        controller = new PedidosController();
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
        Pane subMenu = new VBox(5);
        subMenu.setPadding(new Insets(0.5 * rem));
        // Contenido
        Pane content = new GridPane();
        content.setPadding(new Insets(0.5 * rem));

        // Creamos los elementos
        // Generamos el título de la vista
        Text title = new Text("Pedidos");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("title"));

        // Botones del submenú
        Button btnListar = new Button("Listar pedidos");
        btnListar.setFont(FontsDictionary.getFont("button"));
        // Listamos todos los pedidos
        btnListar.setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(showPedidos());
        });

        Button btnAnadir = new Button("Añadir un pedido");
        btnAnadir.setFont(FontsDictionary.getFont("button"));
        // Añadimos un pedido nuevo
        btnAnadir.setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(addPedido());
        });

        Button btnActualizarEstado = new Button("Actualizar estado");
        btnActualizarEstado.setFont(FontsDictionary.getFont("button"));
        // Actualizamos el estado de los pedidos
        btnActualizarEstado.setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(updateEstado());
        });

        Button btnFiltrarPendientes = new Button("Mostrar pedidos pendientes");
        btnFiltrarPendientes.setFont(FontsDictionary.getFont("button"));
        // Filtramos los pedidos pendientes
        btnFiltrarPendientes.setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(showPedidosPendientes());
        });

        Button btnFiltrarPorCliente = new Button("Mostrar pedidos por cliente");
        btnFiltrarPorCliente.setFont(FontsDictionary.getFont("button"));
        // Filtramos los pedidos por cliente
        btnFiltrarPorCliente.setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(showPedidosByNif());
        });

        // Añadimos los botones a la sección de submenú
        subMenu.getChildren().addAll(btnListar, btnAnadir, btnActualizarEstado, btnFiltrarPendientes, btnFiltrarPorCliente);

        // Añádimos el contenido
        content.getChildren().add(showPedidos());

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

    // Mostramos todos los pedidos
    private static Pane showPedidos() {
        // Obtenemos la lista de los pedidos
        ArrayList<Pedido> pedidos = new ArrayList<>();
        try {
            pedidos = controller.listPedidos().orElseGet(() -> {
                throwErrorPane("lista");
                return new ArrayList<>();
            });
        } catch (Exception e) {
            throwErrorPane("excepcion");
            e.printStackTrace();
        }

        // Creamos los paneles
        VBox root = new VBox();
        root.setPadding(new Insets(0.5 * rem));
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Panel para el contenido
        GridPane content = new GridPane();
        // La configuración del pane se realiza en los mapeadores

        // Creamos los elementos
        // Generamos el título de la vista
        Text title = new Text("Listado de pedidos");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("subtitle"));

        // Creamos la línea de botones
        // Botón anterior --> Se muestra si no es el primer artículo
        Button btnAnterior = new Button("Anterior");
        // Botón siguiente --> Se muestra si no es el último artículo
        Button btnSiguiente = new Button("Siguiente");
        // Implementamos un botón para eliminar pedidos
        Button btnEliminar = new Button("Eliminar pedidos");

        // Creamos el panel para los botones
        HBox buttons = new HBox();
        buttons.setSpacing(0.5 * rem);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(btnAnterior, btnSiguiente, btnEliminar);

        // Mostramos un pedido en el pane y crearemos botones para navegar por el listado
        if (!pedidos.isEmpty()) {
            int [] ind = {0};

            // Convertimos la lista de pedidos en un final para poderlo usar en los listeners
            final ArrayList<Pedido> finalPedidos = pedidos;

            // Limpiamos el contenido
            content.getChildren().clear();
            // Mostramos el primer pedido
            content.getChildren().add(mapPedido(finalPedidos.get(ind[0])));

            // Lógica anterior
            btnAnterior.setFont(FontsDictionary.getFont("button"));
            // Ocultamos el botón anterior si es el primer artículo
            btnAnterior.setVisible(false);
            btnAnterior.setOnAction(event -> {
                // Limpiamos los campos
                content.getChildren().remove(0, content.getChildren().size());
                if (ind[0] > 0) {
                    ind[0]--;
                    // Actualizamos el pedido
                    content.getChildren().add(mapPedido(finalPedidos.get(ind[0])));
                    // Mostramos el botón siguiente si no es el último artículo
                    btnSiguiente.setVisible(ind[0] < finalPedidos.size() - 1);

                    // Ocultamos el botón si es el primer artículo
                    btnAnterior.setVisible(ind[0] > 0);
                }
            });

            // Lógica siguiente
            btnSiguiente.setFont(FontsDictionary.getFont("button"));
            // Si no es el último artículo, se muestra el botón
            btnSiguiente.setVisible(ind[0] < finalPedidos.size() - 1);
            btnSiguiente.setOnAction(event -> {
                // Limpiamos los campos
                content.getChildren().remove(0, content.getChildren().size());
                if (ind[0] < finalPedidos.size() - 1) {
                    ind[0]++;
                    // Actualizamos el pedido
                    content.getChildren().add(mapPedido(finalPedidos.get(ind[0])));
                    btnAnterior.setVisible(true);

                    // Ocultamos el botón si es el último artículo
                    btnSiguiente.setVisible(ind[0] < finalPedidos.size() - 1);
                }
            });

            // Lógica eliminar --> El botón solo se muestra si el pedido no está entregado
            btnEliminar.setFont(FontsDictionary.getFont("button"));
            btnEliminar.setVisible(!finalPedidos.get(ind[0]).getEsEnviado());
            // Si no está enviado, permitimos eliminar el pedido
            btnEliminar.setOnAction(e -> {
                // Necesitaremos pasar el pedido al método
                controller.deletePedido(finalPedidos.get(ind[0])).ifPresentOrElse(res -> {
                    // Mostramos un mensaje según el resultado
                    if (res) {
                        throwExitPane("eliminar");

                        // Eliminamos también el pedido de la lista
                        finalPedidos.remove(ind[0]);

                        // Actualizamos el panel
                        content.getChildren().clear();
                    } else {
                        throwErrorPane("eliminar");
                    }
                }, () -> {
                    throwErrorPane("execepcion");
                });
            });

            // Añadimos el título y el contenido al panel
            root.getChildren().addAll(title, content, buttons);
        } else {
            // Si no hay pedidos, mostramos un mensaje
            Text noPedidos = new Text("No existen pedidos registrados");
            noPedidos.setFill(ColorsDictionary.getColor("text-light"));
            noPedidos.setFont(FontsDictionary.getFont("text"));
            root.getChildren().addAll(title, noPedidos);
        }
        return root;
    }

    // El método addPedido llamará a los formularios par crear un pedido según queramos crear un nuevo cliente
    // o usemos un cliente existente.
    private static Pane addPedido() {
        // Controlador extra para obtener el NIF del último cliente añadido
        ClientesController clienteController = new ClientesController(controller.getMenu());

        // Construimos el layout
        VBox root = new VBox();
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Panel para los botones de opción
        HBox optsPane = new HBox();
        optsPane.setAlignment(Pos.CENTER);
        optsPane.setSpacing(0.5 * rem);

        // Panel para formulario
        GridPane formPane = new GridPane();

        // Creamos los elementos
        // Generamos el título de la vista
        Text title = new Text("Añadir un pedido");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("subtitle"));

        // Creamos el grupo de radio buttons
        ToggleGroup optsCrearPedido = new ToggleGroup();
        // Creamos los radio buttons
        RadioButton rbClienteNuevo = new RadioButton("Cliente nuevo");
        rbClienteNuevo.setFont(FontsDictionary.getFont("label"));
        rbClienteNuevo.setTextFill(ColorsDictionary.getColor("text-light"));
        RadioButton rbClienteExistente = new RadioButton("Cliente existente");
        rbClienteExistente.setFont(FontsDictionary.getFont("label"));
        rbClienteExistente.setTextFill(ColorsDictionary.getColor("text-light"));
        // Añadimos los radio buttons al grupo
        rbClienteNuevo.setToggleGroup(optsCrearPedido);
        rbClienteExistente.setToggleGroup(optsCrearPedido);

        // Creamos la lógica de los radio buttons
        optsCrearPedido.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Si se selecciona cliente nuevo, se muestra el formulario para crear un cliente
                if (newValue.equals(rbClienteNuevo)) {
                    // Lanazamos el formulario para crear un cliente
                    Stage windowAddCliente = addCliente();
                    windowAddCliente.show();

                    // Capturamos el evento de cerrar la ventana -> Tenemos que llegar a la propiedad NIF del cliente
                    windowAddCliente.setOnCloseRequest(closeEvent -> {
                        // Necesitamos obtener el último nif añadido a la BD. Para ello, usamos el controlador de clientes.
                        ArrayList<String> nifs = clienteController.listClientesNif().orElseGet(ArrayList::new);

                        // Llamamos al formulario de creación de pedido adaptado para un nuevo cliente
                        formPane.getChildren().clear();
                        formPane.setVisible(true);
                        // Obtenemos el último NIF añadido
                        formPane.getChildren().add(addFormPedido(nifs.get(nifs.size() - 1)));
                    });
                } else {
                    // Añadimos el formulario para crear un pedido con un cliente existente
                    formPane.getChildren().clear();
                    formPane.setVisible(true);
                    formPane.getChildren().add(addFormPedido());
                }
            }
        });

        // Añadimos los radio buttons al panel
        optsPane.getChildren().addAll(rbClienteNuevo, rbClienteExistente);
        // Cargamos los elementos en el layout
        root.getChildren().addAll(title, optsPane, formPane);
        return root;
    }

    // Creamos un formulario para añadir un pedido
    private static Pane addFormPedido() {
        // Necesitamos los controladores para clientes y artículos
        ArticulosController articulosController = new ArticulosController(controller.getMenu());
        ClientesController clientesController = new ClientesController(controller.getMenu());

        // Generamos un observable para los validarores, necesitaremos 3
        Property<Boolean[]> validators = new SimpleObjectProperty<>(new Boolean[3]);
        // Setteamos los valores en false
        Arrays.fill(validators.getValue(), false);

        // Observable para crear el pedido cargado
        Property<Pedido> newPedido = new SimpleObjectProperty<>(new Pedido());

        // Creamos el layout
        VBox root = new VBox();
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);
        root.setPadding(new Insets(0.5 * rem));

        // Creamos el formulario
        VBox formBox = new VBox();
        formBox.setSpacing(0.5 * rem);

        GridPane form = new GridPane();
        form.setPadding(new Insets(0.5 * rem));
        form.setHgap(0.8 * rem);
        form.setVgap(0.8 * rem);

        // Creamos una card que muestra el pedido creado
        GridPane card = new GridPane();
        card.setVisible(false);

        // Necesitamos una lista con los clientes con los nifs de los clientes y un diccionario con los códigos de los artículos y sus nombres
        ArrayList<String> nifs = clientesController.listClientesNif().orElseGet(() -> {
            throwErrorPane("noClientes");
            return new ArrayList<>();
        });
        HashMap<String, String> articulosMap = new HashMap<>();
        articulosController.listArticulos().ifPresentOrElse(data -> {
            // Creamos un diccionario con los códigos y nombres de los artículos
            if (!data.isEmpty()) {
                data.forEach(articulo -> articulosMap.put(articulo.getCodArticulo(), articulo.getDescripcion()));
            } else {
                // Si no hay artículos, mostramos un mensaje
                throwErrorPane("noArticulos");
            }
        }, () -> {
            throwErrorPane("execepcion");
        });

        // Creamos los elementos del formulario
        // Nif cliente
        Label lblNif = new Label("NIF cliente");
        lblNif.setTextFill(ColorsDictionary.getColor("text-light"));
        lblNif.setFont(FontsDictionary.getFont("text"));
        lblNif.setAlignment(Pos.CENTER_LEFT);
        ComboBox<String> nif = new ComboBox<>();
        nif.setEditable(true);
        nif.getItems().addAll(nifs);
        nif.setPromptText("NIF cliente");
        lblNif.setLabelFor(nif);
        // Añadimos un listener para que se muestre si el cliente no existe
        nif.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!nifs.contains(newValue)) {
                nif.setStyle("-fx-border-color: red");
            } else {
                nif.setStyle("-fx-border-color: none");
                validators.setValue(new Boolean[]{true, validators.getValue()[1], validators.getValue()[2]});
            }
        });
        // Artículo
        // Escogemos el código de artículo y mostramos la descripción
        Label lblArticulo = new Label("Artículo");
        lblArticulo.setTextFill(ColorsDictionary.getColor("text-light"));
        lblArticulo.setFont(FontsDictionary.getFont("text"));
        lblArticulo.setAlignment(Pos.CENTER_LEFT);
        ComboBox<String> articulo = new ComboBox<>();
        articulo.setEditable(true);
        articulo.getItems().addAll(articulosMap.keySet());
        articulo.setPromptText("Código artículo");
        lblArticulo.setLabelFor(articulo);
        // Añadimos un listener para que se muestre un mensaje si el artículo no existe
        articulo.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!articulosMap.containsKey(newValue)) {
                articulo.setStyle("-fx-border-color: red");
            } else {
                articulo.setStyle("-fx-border-color: none");
                validators.setValue(new Boolean[]{validators.getValue()[0], true, validators.getValue()[2]});
            }
        });
        // Capturamos la descripción del artículo en nuestro diccionario
        Text descripcionArticulo = new Text();
        descripcionArticulo.setFill(ColorsDictionary.getColor("text-light"));
        descripcionArticulo.setFont(FontsDictionary.getFont("subtitle"));
        // Añadimos un listener para que se muestre la descripción del artículo
        articulo.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            descripcionArticulo.setText(articulosMap.getOrDefault(newValue, ""));
        });
        // Cantidad
        Label lblCantidad = new Label("Cantidad");
        lblCantidad.setTextFill(ColorsDictionary.getColor("text-light"));
        lblCantidad.setFont(FontsDictionary.getFont("text"));
        lblCantidad.setAlignment(Pos.CENTER_LEFT);
        TextField cantidad = new TextField();
        cantidad.setPrefColumnCount(10);
        cantidad.setPromptText("Cantidad");
        lblCantidad.setLabelFor(cantidad);
        // Comprobamos que el valor introducido sea un número
        cantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Integer.parseInt(newValue);
                cantidad.setStyle("-fx-border-color: green");
                validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], true});
            } catch (NumberFormatException e) {
                cantidad.setStyle("-fx-border-color: red");
            }
        });
        // Botón para añadir el pedido
        Button btnAdd = new Button("Añadir");
        btnAdd.setPrefWidth(20 * rem);
        btnAdd.setFont(FontsDictionary.getFont("button"));
        btnAdd.setTextFill(ColorsDictionary.getColor("text-light"));
        // El botón solo estará disponible si los campos son válidos
        btnAdd.setDisable(true);
        validators.addListener(
                (observable, oldValue, newValue) -> btnAdd.setDisable(!Arrays.stream(validators.getValue()).allMatch(value -> value))
        );

        // Creamos la lógica de añadir el pedido
        btnAdd.setOnAction(event -> {
            // Comprobamos que la cantidad sea mayor que 0
            if (Integer.parseInt(cantidad.getText()) > 0) {
                // Creamos el pedido
                controller.addPedido(nif.getEditor().getText(), articulo.getEditor().getText(), Integer.parseInt(cantidad.getText()))
                        .ifPresentOrElse(data -> {
                    // Si se ha creado correctamente, mostramos un mensaje
                    newPedido.setValue(data);
                    throwExitPane("crear");
                }, () -> throwErrorPane("crear"));
                // Limpiamos los campos del formulario
                nif.getEditor().clear();
                articulo.getEditor().clear();
                cantidad.clear();

                // Añadimos el nuevo pedido a la card
                card.getChildren().add(mapPedido(newPedido.getValue()));
            } else {
                // Si la cantidad es menor o igual que 0, mostramos un mensaje
                throwErrorPane("cantidad");
            }
        });

        // Botón cancelar, limpia los campos del formulario
        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefWidth(20 * rem);
        btnCancel.setFont(FontsDictionary.getFont("button"));
        btnCancel.setTextFill(ColorsDictionary.getColor("text-light"));
        // Creamos la lógica de cancelar el pedido
        btnCancel.setOnAction(event -> {
            nif.getEditor().clear();
            articulo.getEditor().clear();
            cantidad.clear();
        });

        // Creamos el pane de los botones
        HBox buttons = new HBox();
        buttons.setSpacing(2 * rem);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.getChildren().addAll(btnAdd, btnCancel);

        // Añadimos los elementos al form
        form.addRow(0, lblNif, nif);
        form.addRow(1, lblArticulo, articulo, descripcionArticulo);
        form.addRow(2, lblCantidad, cantidad);
        formBox.getChildren().addAll(form, buttons);

        // Si se ha creado el pedido, lo mapeamos a un pane
        newPedido.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                card.setVisible(true);
            }
        });

        // Añadimos los elementos al layout
        root.getChildren().addAll(formBox, card);
        return root;
    }

    // Override del método para adaptarlo a un nuevo cliente
    private static Pane addFormPedido(String actNif) {
        // Necesitamos los controladores para clientes y artículos
        ArticulosController articulosController = new ArticulosController(controller.getMenu());

        // Generamos un observable para los validarores, necesitaremos 3
        Property<Boolean[]> validators = new SimpleObjectProperty<>(new Boolean[2]);
        // Setteamos los valores en false
        Arrays.fill(validators.getValue(), false);

        // Observable para crear el pedido cargado
        Property<Pedido> newPedido = new SimpleObjectProperty<>(new Pedido());

        // Creamos el layout
        VBox root = new VBox();
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);
        root.setPadding(new Insets(0.5 * rem));

        // Creamos el formulario
        VBox formBox = new VBox();
        formBox.setSpacing(0.5 * rem);

        GridPane form = new GridPane();
        form.setPadding(new Insets(0.5 * rem));
        form.setHgap(0.8 * rem);
        form.setVgap(0.8 * rem);

        // Creamos una card que muestra el pedido creado
        GridPane card = new GridPane();
        card.setVisible(false);

        // Necesitamos un diccionario con los códigos de los artículos y sus nombres
        HashMap<String, String> articulosMap = new HashMap<>();
        articulosController.listArticulos().ifPresentOrElse(data -> {
            // Creamos un diccionario con los códigos y nombres de los artículos
            if (!data.isEmpty()) {
                data.forEach(articulo -> articulosMap.put(articulo.getCodArticulo(), articulo.getDescripcion()));
            } else {
                // Si no hay artículos, mostramos un mensaje
                throwErrorPane("noArticulos");
            }
        }, () -> {
            throwErrorPane("execepcion");
        });

        // Creamos los elementos del formulario
        // Nif cliente
        Label lblNif = new Label("NIF cliente");
        lblNif.setTextFill(ColorsDictionary.getColor("text-light"));
        lblNif.setFont(FontsDictionary.getFont("text"));
        lblNif.setAlignment(Pos.CENTER_LEFT);
        TextField nifCliente = new TextField(actNif);
        nifCliente.setFont(FontsDictionary.getFont("text"));
        nifCliente.setEditable(false);
        nifCliente.setDisable(true);
        lblNif.setLabelFor(nifCliente);
        // Artículo
        // Escogemos el código de artículo y mostramos la descripción
        Label lblArticulo = new Label("Artículo");
        lblArticulo.setTextFill(ColorsDictionary.getColor("text-light"));
        lblArticulo.setFont(FontsDictionary.getFont("text"));
        lblArticulo.setAlignment(Pos.CENTER_LEFT);
        ComboBox<String> articulo = new ComboBox<>();
        articulo.setEditable(true);
        articulo.getItems().addAll(articulosMap.keySet());
        articulo.setPromptText("Código artículo");
        lblArticulo.setLabelFor(articulo);
        // Añadimos un listener para que se muestre un mensaje si el artículo no existe
        articulo.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!articulosMap.containsKey(newValue)) {
                articulo.setStyle("-fx-border-color: red");
            } else {
                articulo.setStyle("-fx-border-color: none");
                validators.setValue(new Boolean[]{validators.getValue()[0], true, validators.getValue()[2]});
            }
        });
        // Capturamos la descripción del artículo en nuestro diccionario
        Text descripcionArticulo = new Text();
        descripcionArticulo.setFill(ColorsDictionary.getColor("text-light"));
        descripcionArticulo.setFont(FontsDictionary.getFont("subtitle"));
        // Añadimos un listener para que se muestre la descripción del artículo
        articulo.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            descripcionArticulo.setText(articulosMap.getOrDefault(newValue, ""));
        });
        // Cantidad
        Label lblCantidad = new Label("Cantidad");
        lblCantidad.setTextFill(ColorsDictionary.getColor("text-light"));
        lblCantidad.setFont(FontsDictionary.getFont("text"));
        lblCantidad.setAlignment(Pos.CENTER_LEFT);
        TextField cantidad = new TextField();
        cantidad.setPrefColumnCount(10);
        cantidad.setPromptText("Cantidad");
        lblCantidad.setLabelFor(cantidad);
        // Comprobamos que el valor introducido sea un número
        cantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Integer.parseInt(newValue);
                cantidad.setStyle("-fx-border-color: green");
                validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], true});
            } catch (NumberFormatException e) {
                cantidad.setStyle("-fx-border-color: red");
            }
        });
        // Botón para añadir el pedido
        Button btnAdd = new Button("Añadir");
        btnAdd.setPrefWidth(20 * rem);
        btnAdd.setFont(FontsDictionary.getFont("button"));
        btnAdd.setTextFill(ColorsDictionary.getColor("text-light"));
        // El botón solo estará disponible si los campos son válidos
        btnAdd.setDisable(true);
        validators.addListener(
                (observable, oldValue, newValue) -> btnAdd.setDisable(!Arrays.stream(validators.getValue()).allMatch(value -> value))
        );

        // Creamos la lógica de añadir el pedido
        btnAdd.setOnAction(event -> {
            // Comprobamos que la cantidad sea mayor que 0
            if (Integer.parseInt(cantidad.getText()) > 0) {
                // Creamos el pedido
                controller.addPedido(actNif, articulo.getEditor().getText(), Integer.parseInt(cantidad.getText()))
                        .ifPresentOrElse(data -> {
                            // Si se ha creado correctamente, mostramos un mensaje
                            newPedido.setValue(data);
                            throwExitPane("crear");
                        }, () -> throwErrorPane("crear"));
                // Limpiamos los campos del formulario
                nifCliente.clear();
                articulo.getEditor().clear();
                cantidad.clear();

                // Añadimos el nuevo pedido a la card
                card.getChildren().add(mapPedido(newPedido.getValue()));
            } else {
                // Si la cantidad es menor o igual que 0, mostramos un mensaje
                throwErrorPane("cantidad");
            }
        });

        // Botón cancelar, limpia los campos del formulario
        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefWidth(20 * rem);
        btnCancel.setFont(FontsDictionary.getFont("button"));
        btnCancel.setTextFill(ColorsDictionary.getColor("text-light"));
        // Creamos la lógica de cancelar el pedido
        btnCancel.setOnAction(event -> {
            nifCliente.clear();
            articulo.getEditor().clear();
            cantidad.clear();
        });

        // Creamos el pane de los botones
        HBox buttons = new HBox();
        buttons.setSpacing(2 * rem);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.getChildren().addAll(btnAdd, btnCancel);

        // Añadimos los elementos al form
        form.addRow(0, lblNif, nifCliente);
        form.addRow(1, lblArticulo, articulo, descripcionArticulo);
        form.addRow(2, lblCantidad, cantidad);
        formBox.getChildren().addAll(form, buttons);

        // Si se ha creado el pedido, lo mapeamos a un pane
        newPedido.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                card.setVisible(true);
            }
        });

        // Añadimos los elementos al layout
        root.getChildren().addAll(formBox, card);
        return root;
    }

    // Ventana de creación de clientes
    private static Stage addCliente () {
        // Creamos la ventana
        Stage windowNuevoCLiente = new Stage();
        windowNuevoCLiente.setTitle("Añadir un cliente");
        windowNuevoCLiente.initModality(Modality.APPLICATION_MODAL);

        // Creamos el panel principal
        VBox root = new VBox();
        root.setSpacing(0.5 * rem);
        root.setPadding(new Insets(0.5 * rem));
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);

        // Usaremos el formulario de la vista de clientes
        GridPane form = new GridPane();

        // Creamos la escena
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(ColorsDictionary.getColor("background-dark"));
        windowNuevoCLiente.setScene(scene);

        // Creamos el formulario
        ClientesView clientesView = new ClientesView(controller.getMenu());
        form.getChildren().add(clientesView.getAddCliente());

        // Creamos botones
        HBox buttons = new HBox();
        buttons.setSpacing(2 * rem);
        buttons.setAlignment(Pos.CENTER_LEFT);
        Button btnAdd = new Button("Ok");
        btnAdd.setPrefWidth(20 * rem);
        btnAdd.setFont(FontsDictionary.getFont("button"));
        btnAdd.setDisable(true);

        Button btnCancel = new Button("Cancelar");
        btnCancel.setPrefWidth(20 * rem);
        btnCancel.setFont(FontsDictionary.getFont("button"));
        // Este botón cancela la creación del cliente
        btnCancel.setOnAction(event -> {
            // Cerramos la ventana
            windowNuevoCLiente.close();
        });

        // Añadimos los elementos al layout
        root.getChildren().add(form);

        return windowNuevoCLiente;
    }

    // Actualizamos el estado de los pedidos
    private static Pane updateEstado() {
        // Creamos un panel con una card informativa del resultado
        Pane root = new Pane();
        root.setPadding(new Insets(0.5 * rem));
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);

        // Creamos el mensaje que estará actualizándose en función del resultado
        Text message = new Text("Actualizando pedidos...");
        message.setFill(ColorsDictionary.getColor("text-light"));
        message.setFont(FontsDictionary.getFont("title"));
        message.setTextAlignment(TextAlignment.CENTER);
        message.setWrappingWidth(30 * rem);

        // Añadimos el mensaje al panel
        root.getChildren().add(message);

        // Llamamos al método para actualizar el estado de los pedidos
        controller.updatePedidos().ifPresentOrElse(
                // Si se ha actualizado algún pedido, se muestra el número de pedidos actualizados
                value -> {
                    if (value == 0) {
                        throwExitPane("actualizar-0");
                    } else {
                        throwExitPane("actualizar", value);
                    }
                },
                // Si no se ha actualizado ningún pedido, se muestra un mensaje
                () -> throwErrorPane("actualizar")
        );

        return root;
    }

    // Mostramos los pedidos pendientes
    private static Pane showPedidosPendientes() {
        // Contenedor para la lista de pedidos
        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        try {
            pedidosPendientes = controller.listPedidosPendientes().orElseGet(() -> {
                throwErrorPane("lista");
                return new ArrayList<>();
            });
        } catch (Exception e) {
            throwErrorPane("excepcion");
            e.printStackTrace();
        }

        // Creamos los paneles
        VBox root = new VBox();
        root.setPadding(new Insets(0.5 * rem));
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Panel para el contenido
        GridPane content = new GridPane();
        // La configuración del pane se realiza en los mapeadores

        // Creamos el título
        Text title = new Text("Pedidos pendientes de envío");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("title"));
        title.setTextAlignment(TextAlignment.CENTER);

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

        // Mostramos un pedido en el pane y crearemos botones para navegar por el listado
        if (!pedidosPendientes.isEmpty()) {
            int [] ind = {0};

            // Convertimos la lista de pedidos en un final para poderlo usar en los listeners
            final ArrayList<Pedido> finalPedidos = pedidosPendientes;

            // Limpiamos el contenido
            content.getChildren().clear();
            // Mostramos el primer pedido
            content.getChildren().add(mapPedido(finalPedidos.get(ind[0])));

            // Lógica anterior
            btnAnterior.setFont(FontsDictionary.getFont("button"));
            // Ocultamos el botón anterior si es el primer artículo
            btnAnterior.setVisible(false);
            btnAnterior.setOnAction(event -> {
                // Limpiamos los campos
                content.getChildren().remove(0, content.getChildren().size());
                if (ind[0] > 0) {
                    ind[0]--;
                    // Actualizamos el pedido
                    content.getChildren().add(mapPedido(finalPedidos.get(ind[0])));
                    // Mostramos el botón siguiente si no es el último artículo
                    btnSiguiente.setVisible(ind[0] < finalPedidos.size() - 1);

                    // Ocultamos el botón si es el primer artículo
                    btnAnterior.setVisible(ind[0] > 0);
                }
            });

            // Lógica siguiente
            btnSiguiente.setFont(FontsDictionary.getFont("button"));
            // Si no es el último artículo, se muestra el botón
            btnSiguiente.setVisible(ind[0] < finalPedidos.size() - 1);
            btnSiguiente.setOnAction(event -> {
                // Limpiamos los campos
                content.getChildren().remove(0, content.getChildren().size());
                if (ind[0] < finalPedidos.size() - 1) {
                    ind[0]++;
                    // Actualizamos el pedido
                    content.getChildren().add(mapPedido(finalPedidos.get(ind[0])));
                    btnAnterior.setVisible(true);

                    // Ocultamos el botón si es el último artículo
                    btnSiguiente.setVisible(ind[0] < finalPedidos.size() - 1);
                }
            });

            // Añadimos el título y el contenido al panel
            root.getChildren().addAll(title, content, buttons);
        } else {
            // Si no hay pedidos, mostramos un mensaje
            Text noPedidos = new Text("No existen pedidos que mostrar.");
            noPedidos.setFill(ColorsDictionary.getColor("text-light"));
            noPedidos.setFont(FontsDictionary.getFont("text"));
            root.getChildren().addAll(title, noPedidos);
        }
        return root;
    }

    // Mostramos una lista de pedidos filtrados por el nif del cliente
    private static Pane showPedidosByNif() {
        // Necesitamos un controlador para obtener la información de los clientes
        ClientesController clientesController = new ClientesController(controller.getMenu());
        // Obtenemos una lista con los NIFs de los clientes para hacer la búsqueda más interactiva.
        ArrayList<String> nifs = new ArrayList<>();
        try {
            nifs = clientesController.listClientesNif().orElseGet(() -> {
                throwErrorPane("lista");
                return new ArrayList<>();
            });
        } catch (Exception e) {
            throwErrorPane("excepcion");
            e.printStackTrace();
        }

        // Creamos los paneles
        VBox root = new VBox();
        root.setPadding(new Insets(0.5 * rem));
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Panel para el contenido
        GridPane content = new GridPane();
        // La configuración del pane se realiza en los mapeadores

        // Creamos el título
        // Más adelante añadiremos el NIF del cliente
        Text title = new Text("Pedidos del cliente con NIF:");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("title"));
        title.setTextAlignment(TextAlignment.CENTER);

        // Creamos la línea de botones
        // Botón anterior --> Se muestra si no es el primer artículo
        Button btnAnterior = new Button("Anterior");
        btnAnterior.setVisible(false);
        // Botón siguiente --> Se muestra si no es el último artículo
        Button btnSiguiente = new Button("Siguiente");
        btnSiguiente.setVisible(false);
        // Creamos el panel para los botones
        HBox buttons = new HBox();
        buttons.setSpacing(0.5 * rem);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(btnAnterior, btnSiguiente);

        // Si hay clientes, creamos el formulario
        if (!nifs.isEmpty()) {
            // Creamos el formulario
            HBox form = new HBox();
            form.setSpacing(0.5 * rem);

            // Creamos los campos del formulario
            Label lblNif = new Label("NIF");
            lblNif.setFont(FontsDictionary.getFont("label"));
            lblNif.setTextFill(ColorsDictionary.getColor("text-light"));
            // Creamos el campo input -> Vamos a usar un ComboBox para cargar las opciones y
            // también admitir un input textual.
            ComboBox<String> inputNif = new ComboBox<>();
            // Convertimos el elemento en editable
            inputNif.setEditable(true);
            // Cargamos los elementos -> Cargamos la lista de nifs
            inputNif.getItems().addAll(nifs);
            // Establecemos el texto por defecto
            inputNif.setPromptText("Introduce el NIF del cliente");
            // Añadimos un listener para que se muestre un mensaje si el cliente no existe
            final ArrayList<String> finalNifs = nifs;
            inputNif.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                if (!finalNifs.contains(newValue)) {
                    inputNif.setStyle("-fx-border-color: red");
                } else {
                    inputNif.setStyle("-fx-border-color: none");
                }
            });

            // Creamos el botón de búsqueda
            Button btnBuscar = new Button("Buscar...");
            btnBuscar.setFont(FontsDictionary.getFont("button"));
            // Creamos la lógica del botón
            btnBuscar.setOnAction(event -> {
                // Modificamos el título
                title.setText("Pedidos del cliente con NIF: " + inputNif.getValue());
                // Obtenemos los pedidos del cliente
                ArrayList<Pedido> pedidosCliente = controller.listPedidosCliente(inputNif.getValue()).orElseGet(() -> {
                    throwErrorPane("lista");
                    return new ArrayList<>();
                });

                // Limpiamos los campos
                content.getChildren().clear();

                // Si hay pedidos, los mostramos
                if (!pedidosCliente.isEmpty()) {
                    // Creamos las variables finales
                    final int[] ind = {0};
                    final ArrayList<Pedido> finalPedidos = pedidosCliente;

                    // Mostramos el primer pedido
                    content.getChildren().add(mapPedido(finalPedidos.get(ind[0])));

                    // Lógica anterior
                    btnAnterior.setFont(FontsDictionary.getFont("button"));
                    // Ocultamos el botón anterior si es el primer artículo
                    btnAnterior.setVisible(false);
                    btnAnterior.setOnAction(e -> {
                        // Limpiamos los campos
                        content.getChildren().remove(0, content.getChildren().size());
                        if (ind[0] > 0) {
                            ind[0]--;
                            // Actualizamos el pedido
                            content.getChildren().add(mapPedido(finalPedidos.get(ind[0])));
                            // Mostramos el botón siguiente si no es el último artículo
                            btnSiguiente.setVisible(ind[0] < finalPedidos.size() - 1);

                            // Ocultamos el botón si es el primer artículo
                            btnAnterior.setVisible(ind[0] > 0);
                        }
                    });

                    // Lógica siguiente
                    btnSiguiente.setFont(FontsDictionary.getFont("button"));
                    // Si no es el último artículo, se muestra el botón
                    btnSiguiente.setVisible(ind[0] < finalPedidos.size() - 1);
                    btnSiguiente.setOnAction(e -> {
                        // Limpiamos los campos
                        content.getChildren().remove(0, content.getChildren().size());
                        if (ind[0] < finalPedidos.size() - 1) {
                            ind[0]++;
                            // Actualizamos el pedido
                            content.getChildren().add(mapPedido(finalPedidos.get(ind[0])));
                            btnAnterior.setVisible(true);

                            // Ocultamos el botón si es el último artículo
                            btnSiguiente.setVisible(ind[0] < finalPedidos.size() - 1);
                        }
                    });
                } else {
                    // Si no hay pedidos, mostramos un mensaje
                    // Añadimos el nif al título
                    title.setText("Pedidos del cliente con NIF: " + inputNif.getValue());
                    Text noPedidos = new Text("No existen pedidos que mostrar.");
                    noPedidos.setFill(ColorsDictionary.getColor("text-light"));
                    noPedidos.setFont(FontsDictionary.getFont("subtitle"));
                    root.getChildren().addAll(noPedidos);
                }
            });
            // Añadimos los elementos al formulario
            form.getChildren().addAll(lblNif, inputNif, btnBuscar);
            // Añadimos los elementos al panel principal
            root.getChildren().addAll(title, form, content, buttons);
        } else {
            Text noClientes = new Text("No existen clientes que mostrar.");
            noClientes.setFill(ColorsDictionary.getColor("text-light"));
            noClientes.setFont(FontsDictionary.getFont("subtitle"));
            root.getChildren().clear();
            root.getChildren().addAll(noClientes);
        }

        return root;
    }

    // Ventana de error
    private static void throwErrorPane(String type) {
        // Creamos el diálogo
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Ha habido un error");

        // Creamos el mensaje
        switch (type) {
            case "lista" -> errorAlert.setContentText("No se ha podido obtener la lista de pedidos");
            case "crear" -> errorAlert.setContentText("No se ha podido añadir el pedido");
            case "filtrar" -> errorAlert.setContentText("No se ha podido filtrar la lista de pedidos");
            case "buscar" -> errorAlert.setContentText("No se ha podido encontrar el pedido");
            case "actualizar" -> errorAlert.setContentText("No se ha podido actualizar el estado de los pedidos");
            case "eliminar" -> errorAlert.setContentText("No se ha podido eliminar el pedido");
            case "noArticulos" -> errorAlert.setContentText("No se ha podido obtener la lista de artículos");
            case "noClientes" -> errorAlert.setContentText("No se ha podido obtener la lista de clientes");
            case "excepcion" -> errorAlert.setContentText("Ha ocurrido un error no definido");
            case "cantidad" -> errorAlert.setContentText("La cantidad no puede ser menor que 1");
            default -> errorAlert.setContentText("Ha ocurrido un error");
        }
        // Estilamos el diálogo
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String cssFile = Objects.requireNonNull(classLoader.getResource("styles.css")).toExternalForm();
        errorAlert.getDialogPane().getStylesheets().add(cssFile);

        // Mostramos el diálogo
        errorAlert.showAndWait();
    }

    // Ventana de éxito
    private static void throwExitPane(String type) {
        // Creamos el diálogo
        Alert exitAlert = new Alert(Alert.AlertType.INFORMATION);
        exitAlert.setTitle("Pedidos");
        exitAlert.setHeaderText("Operación realizada con éxito");

        // Creamos el mensaje
        switch (type) {
            case "crear" -> exitAlert.setContentText("Se ha añadido el pedido");
            case "actualizar-0" -> exitAlert.setContentText("No hay pedidos para actualizar");
            case "eliminar" -> exitAlert.setContentText("Se ha eliminado el pedido");
        }

        // Estilamos el diálogo
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String cssFile = Objects.requireNonNull(classLoader.getResource("styles.css")).toExternalForm();
        exitAlert.getDialogPane().getStylesheets().add(cssFile);

        // Mostramos el diálogo
        exitAlert.showAndWait();
    }
    // Override con contador
    private static void throwExitPane(String type, int count) {
        // Creamos el diálogo
        Alert exitAlert = new Alert(Alert.AlertType.INFORMATION);
        exitAlert.setTitle("Pedidos");
        exitAlert.setHeaderText("Operación realizada con éxito");

        // Creamos el mensaje
        exitAlert.setContentText("Se han actualizado " + count + " pedidos");

        // Estilamos el diálogo
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String cssFile = Objects.requireNonNull(classLoader.getResource("styles.css")).toExternalForm();
        exitAlert.getDialogPane().getStylesheets().add(cssFile);

        // Mostramos el diálogo
        exitAlert.showAndWait();
    }

    // Mapeador para los pedidos
    private static Pane mapPedido(Pedido pedido) {
        // Creamos el panel para mostrar la información
        GridPane content = new GridPane();
        content.setPadding(new Insets(0.5 * rem));
        content.setHgap(0.2 * rem);
        content.setVgap(0.2 * rem);
        content.getStyleClass().add("pedido-pane");
        // Añadimos un borde al panel
        content.setBorder(new Border(new BorderStroke(ColorsDictionary.getColor("border-orange"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // Creamos los elementos del panel
        // Número de pedido
        Label lblNumeroPedido = new Label("Número de pedido");
        lblNumeroPedido.setFont(FontsDictionary.getFont("label"));
        lblNumeroPedido.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtNumeroPedido = new Text(String.valueOf(pedido.getNumeroPedido()));
        lblNumeroPedido.setLabelFor(txtNumeroPedido);
        // Fecha de pedido
        Label lblFechaPedido = new Label("Fecha de pedido");
        lblFechaPedido.setFont(FontsDictionary.getFont("label"));
        lblFechaPedido.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtFechaPedido = new Text(pedido.getFechaPedido().toString());
        lblFechaPedido.setLabelFor(txtFechaPedido);

        // Datos del cliente
        Label lblClienteNif = new Label("NIF");
        lblClienteNif.setFont(FontsDictionary.getFont("label"));
        lblClienteNif.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtClienteNif = new Text(pedido.getCliente().getNif());
        lblClienteNif.setLabelFor(txtClienteNif);
        // Nombre del cliente
        Label lblClienteNombre = new Label("Nombre");
        lblClienteNombre.setFont(FontsDictionary.getFont("label"));
        lblClienteNombre.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtClienteNombre = new Text(pedido.getCliente().getNombre());
        lblClienteNombre.setLabelFor(txtClienteNombre);
        // Si el cliente es premium, añadimos el número de socio
        Label lblCLienteNumeroSocio = new Label("Cod socio");
        lblCLienteNumeroSocio.setFont(FontsDictionary.getFont("label"));
        lblCLienteNumeroSocio.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtClienteNumeroSocio = new Text();
        lblCLienteNumeroSocio.setLabelFor(txtClienteNumeroSocio);
        if (pedido.getCliente().getClass().getSimpleName().equals("ClientePremium")) {
            txtClienteNumeroSocio = new Text(String.valueOf(((ClientePremium) pedido.getCliente()).getCodSocio()));
        }

        // Datos del artículo
        Label lblCodArticulo = new Label("Código");
        lblCodArticulo.setFont(FontsDictionary.getFont("label"));
        lblCodArticulo.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtCodArticulo = new Text(pedido.getArticulo().getCodArticulo());
        lblCodArticulo.setLabelFor(txtCodArticulo);
        // Unidades pedidas
        Label lblunidades = new Label("Unidades");
        lblunidades.setFont(FontsDictionary.getFont("label"));
        lblunidades.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtUnidades = new Text(String.valueOf(pedido.getUnidades()));
        lblunidades.setLabelFor(txtUnidades);
        // Precio de venta
        Label lblPrecioVenta = new Label("PVP");
        lblPrecioVenta.setFont(FontsDictionary.getFont("label"));
        lblPrecioVenta.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtPrecioVenta = new Text(String.valueOf(pedido.getArticulo().getPvp()));
        lblPrecioVenta.setLabelFor(txtPrecioVenta);
        // Total línea
        Label lblTotalLinea = new Label("Subtotal");
        lblTotalLinea.setFont(FontsDictionary.getFont("label"));
        lblTotalLinea.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtTotalLinea = new Text(String.valueOf(pedido.getUnidades() * pedido.getArticulo().getPvp()));
        lblTotalLinea.setLabelFor(txtTotalLinea);

        // Línea de totales
        // Gastos de envío
        Label lblGastosEnvio = new Label("Gastos de envío");
        lblGastosEnvio.setFont(FontsDictionary.getFont("label"));
        lblGastosEnvio.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtGastosEnvio = new Text(pedido.precioEnvio() + "€");
        lblGastosEnvio.setLabelFor(txtGastosEnvio);
        Label lblTotal = new Label("Total");
        lblTotal.setFont(FontsDictionary.getFont("label"));
        lblTotal.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtTotal = new Text(pedido.precioTotal() + "€");
        lblTotal.setLabelFor(txtTotal);

        // Estado del pedido
        Label lblEstado = new Label("Estado");
        lblEstado.setFont(FontsDictionary.getFont("label"));
        lblEstado.setTextFill(ColorsDictionary.getColor("text-dark"));
        Text txtEstado = new Text(pedido.getEsEnviado() ? "Enviado" : "Pendiente de envío");
        lblEstado.setLabelFor(txtEstado);

        // Pintamos los separadores
        Separator separatorA = new Separator();
        separatorA.setOrientation(Orientation.HORIZONTAL);
        separatorA.setPrefHeight(0.2 * rem);
        Separator separatorB = new Separator();
        separatorB.setOrientation(Orientation.HORIZONTAL);
        separatorB.setPrefHeight(0.2 * rem);
        Separator separatorC = new Separator();
        separatorC.setOrientation(Orientation.HORIZONTAL);
        separatorC.setPrefHeight(0.2 * rem);

        // Diseñamos el panel
        // Pane cliente Premium
        if (pedido.getCliente().getClass().getSimpleName().equals("Cliente Premium")) {
            content.addRow(0, lblNumeroPedido, txtNumeroPedido, lblFechaPedido, txtFechaPedido);
            content.addRow(1, lblClienteNif, txtClienteNif, lblClienteNombre, txtClienteNombre);
            content.addRow(2, lblCLienteNumeroSocio, txtClienteNumeroSocio);
            content.add(separatorA, 0, 3, GridPane.REMAINING, 1);
            content.addRow(4, lblCodArticulo, lblunidades, lblPrecioVenta, lblTotalLinea);
            content.addRow(5, txtCodArticulo, txtUnidades, txtPrecioVenta, txtTotalLinea);
            content.add(separatorB, 0, 6, GridPane.REMAINING, 1);
            content.addRow(7, lblGastosEnvio, lblTotal);
            content.addRow(8, txtGastosEnvio, txtTotal);
            content.add(separatorC, 0, 9, GridPane.REMAINING, 1);
            content.addRow(10, lblEstado, txtEstado);

            return content;
        }

        // Pane cliente estandard
        content.addRow(0, lblNumeroPedido, txtNumeroPedido, lblFechaPedido, txtFechaPedido);
        content.addRow(1, lblClienteNif, txtClienteNif, lblClienteNombre, txtClienteNombre);
        content.add(separatorA, 0, 2, GridPane.REMAINING, 1);
        content.addRow(3, lblCodArticulo, lblunidades, lblPrecioVenta, lblTotalLinea);
        content.addRow(4, txtCodArticulo, txtUnidades, txtPrecioVenta, txtTotalLinea);
        content.add(separatorB, 0, 5, GridPane.REMAINING, 1);
        content.addRow(6, lblGastosEnvio, lblTotal);
        content.addRow(7, txtGastosEnvio, txtTotal);
        content.add(separatorC, 0, 8, GridPane.REMAINING, 1);
        content.addRow(9, lblEstado, txtEstado);

        return content;
    }

}

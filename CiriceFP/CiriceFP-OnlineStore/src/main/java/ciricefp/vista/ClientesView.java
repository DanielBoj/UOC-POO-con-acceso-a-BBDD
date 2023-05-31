package ciricefp.vista;

import ciricefp.modelo.Cliente;
import ciricefp.modelo.ClienteEstandard;
import ciricefp.modelo.ClientePremium;
import ciricefp.vista.controladores.ClientesController;
import ciricefp.vista.controladores.MenuPrincipalController;
import ciricefp.vista.dictionaries.ColorsDictionary;
import ciricefp.vista.dictionaries.FontsDictionary;
import ciricefp.vista.interfaces.IClienteController;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Vista principal para el menú de Clientes, va a permitir:
 * Listar los clientes, añadir un nuevo cliente, filtrar los clientes por tipo y buscar un cliente por NIF/
 *
 * @author Cirice
 * @version 1.1
 * @since 05-2023
 * @see MenuPrincipalController
 * @see ClientesController
 * @see Cliente
 */
public class ClientesView {
    // Obtenemos los rem para no trabajar con pixels
    private static final double rem = new Text("").getBoundsInParent().getHeight();
    private final Pane pane;
    // Cargamos el controlador
    private static IClienteController controller;

    // Constructor por defecto
    public ClientesView(MenuPrincipalController menuController) {
        controller = new ClientesController(menuController);
        pane = generatePane();
    }

    // Constructor vacío
    public ClientesView() {
        controller = new ClientesController();
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
        VBox subMenu = new VBox(3);
        subMenu.setPadding(new Insets(0.5 * rem));
        // Espaciamos los elementos
        subMenu.setSpacing(0.8 * rem);
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
        // Mostramos la lista de clientes
        btnListar.setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(showClientes());
        });

        Button btnAnadir = new Button("Añadir un cliente");
        btnAnadir.setFont(FontsDictionary.getFont("button"));
        // Añadimos un nuevo cliente
        btnAnadir.setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(addCliente());
        });

        Button btnBuscar = new Button("Buscar cliente por NIF");
        btnBuscar.setFont(FontsDictionary.getFont("button"));
        // Buscamos un cliente por NIF
        btnBuscar.setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(showClienteByNif());
        });

        Button btnFiltrar = new Button("Filtrar clientes por tipo");
        btnFiltrar.setFont(FontsDictionary.getFont("button"));
        // Filtramos los clientes por tipo
        btnFiltrar.setOnAction(event -> {
            content.getChildren().clear();
            content.getChildren().add(filterClientes());
        });

        // Añadimos los botones a la sección de submenú
        subMenu.getChildren().addAll(btnListar, btnAnadir, btnBuscar, btnFiltrar);

        // Añadimos el contenido
        content.getChildren().add(showClientes());


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
    private static Pane showClientes() {
        // Obtenemos la lista de los clientes
        ArrayList<Cliente> clientes = new ArrayList<>();
        try {
            clientes = controller.listClientes().orElseGet(() -> {
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
        Text title = new Text("Listado de clientes");
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

        // Mostraremos un cliente en el pane y crearemos un botón para ir al siguiente
        // cliente
        // Creamos el panel para mostrar la información
        if (!clientes.isEmpty()) {
            final int[] ind = {0};

            // Convertimos clientes en un Final para poder usarlo en los listeners
            final ArrayList<Cliente> finalClientes = clientes;

            //  Mostramos el primer cliente
            // Discriminamos el tipo de cliente
            if (finalClientes.get(ind[0]).getClass().getSimpleName().equals("ClientePremium")) {
                content.getChildren().add(mapPremium((ClientePremium) finalClientes.get(ind[0])));
            } else {
                content.getChildren().add(mapEstandard(finalClientes.get(ind[0])));
            }

            // Lógica anterior
            btnAnterior.setFont(FontsDictionary.getFont("button"));
            // Ocultamos el botón anterior si es el primer artículo
            btnAnterior.setVisible(false);
            btnAnterior.setOnAction(event -> {
                // Limpiamos los campos
                content.getChildren().remove(0, content.getChildren().size());
                if (ind[0] > 0) {
                    ind[0]--;
                    // Actualizamos el cliente
                    // Discriminamos el tipo de cliente
                    if (finalClientes.get(ind[0]).getClass().getSimpleName().equals("ClientePremium")) {
                        content.getChildren().add(mapPremium((ClientePremium) finalClientes.get(ind[0])));
                    } else {
                        content.getChildren().add(mapEstandard(finalClientes.get(ind[0])));
                    }
                    // Mostramos el botón siguiente si no es el último artículo
                    btnSiguiente.setVisible(ind[0] < finalClientes.size() - 1);

                    // Ocultamos el botón si es el primer artículo
                    btnAnterior.setVisible(ind[0] > 0);
                }
            });

            // Lógica siguiente
            btnSiguiente.setFont(FontsDictionary.getFont("button"));
            // Si no es el último artículo, se muestra el botón
            btnSiguiente.setVisible(ind[0] < finalClientes.size() - 1);
            btnSiguiente.setOnAction(event -> {
                // Limpiamos los campos
                content.getChildren().remove(0, content.getChildren().size());
                if (ind[0] < finalClientes.size() - 1) {
                    ind[0]++;
                    // Actualizamos el cliente
                    // Discriminamos el tipo de cliente
                    if (finalClientes.get(ind[0]).getClass().getSimpleName().equals("ClientePremium")) {
                        content.getChildren().add(mapPremium((ClientePremium) finalClientes.get(ind[0])));
                    } else {
                        content.getChildren().add(mapEstandard(finalClientes.get(ind[0])));
                    }
                    btnAnterior.setVisible(true);

                    // Ocultamos el botón si es el último artículo
                    btnSiguiente.setVisible(ind[0] < finalClientes.size() - 1);
                }
            });

            // Añadimos el título y el contenido al panel
            root.getChildren().addAll(title, content, buttons);
        } else {
            // Si no hay artículos, mostramos un mensaje
            Text noClientes = new Text("No hay clientes que mostrar.");
            noClientes.setFont(FontsDictionary.getFont("subtitle"));
            noClientes.setFill(ColorsDictionary.getColor("text-light"));
            root.getChildren().add(noClientes);
        }
        return root;
    }

    // Creamos un nuevo cliente --> Declaramos el método public porque lo vamos a usar también desde la
    // vista de pedidos
    private static Pane addCliente() {
        // Necesitaremos un observable para 8 validadores
        Property<Boolean[]> validators = new SimpleObjectProperty<>(new Boolean[8]);
        // Seteamos los valores a false
        Arrays.fill(validators.getValue(), false);

        // Observable para el tipo de cliente
        Property<String> tipoCliente = new SimpleObjectProperty<>("ESTANDARD");

        // Cliente creado
        Property<Cliente> nuevoCliente = new SimpleObjectProperty<>(new ClienteEstandard());

        // Creamos el layout
        VBox root = new VBox();
        root.setPadding(new Insets(0.5 * rem));
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Permitiremos la selección del tipo de cliente mediante radio buttons
        HBox optTipoCliente = new HBox();
        optTipoCliente.setSpacing(0.5 * rem);

        VBox formBox = new VBox();
        formBox.setSpacing(0.5 * rem);

        GridPane form = new GridPane();
        form.setHgap(0.8 * rem);
        form.setVgap(0.8 * rem);
        form.setAlignment(Pos.CENTER_LEFT);

        HBox buttons = new HBox();
        buttons.setSpacing(0.5 * rem);
        buttons.setAlignment(Pos.CENTER);

        // Creamos una card para mostrar el cliente
        GridPane card = new GridPane();
        card.setVisible(false);

        // Creamos los elementos
        // Generamos el título de la vista
        Text title = new Text("Añadir cliente");
        title.setFill(ColorsDictionary.getColor("text-light"));
        title.setFont(FontsDictionary.getFont("subtitle"));

        // Selección de tipo de cliente -> Hay que agrupar los radio buttons en un ToggleGroup
        ToggleGroup grpTipoCliente = new ToggleGroup();
        RadioButton rbEstandard = new RadioButton("Estandard");
        rbEstandard.setUserData("ESTANDARD");
        rbEstandard.setFont(FontsDictionary.getFont("label"));
        rbEstandard.setTextFill(ColorsDictionary.getColor("text-light"));
        rbEstandard.setToggleGroup(grpTipoCliente);
        // Seleccionamos estandard por defecto
        rbEstandard.setSelected(true);
        RadioButton rbPremium = new RadioButton("Premium");
        rbPremium.setUserData("PREMIUM");
        rbPremium.setFont(FontsDictionary.getFont("label"));
        rbPremium.setTextFill(ColorsDictionary.getColor("text-light"));
        rbPremium.setToggleGroup(grpTipoCliente);

        // Agregamos la lógica de los radio buttons
        grpTipoCliente.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (grpTipoCliente.getSelectedToggle() != null) {
                tipoCliente.setValue(grpTipoCliente.getSelectedToggle().getUserData().toString());
            }
        });

        // Añadimos los radio buttons al layout
        optTipoCliente.getChildren().addAll(rbEstandard, rbPremium);

        // Creamos los elementos del formulario
        // NIF
        Label lblNif = new Label("NIF");
        lblNif.setFont(FontsDictionary.getFont("label"));
        lblNif.setTextFill(ColorsDictionary.getColor("text-light"));
        lblNif.setAlignment(Pos.CENTER_LEFT);
        TextField txtNif = new TextField();
        txtNif.setFont(FontsDictionary.getFont("text"));
        txtNif.setPromptText("NIF");
        txtNif.setAlignment(Pos.CENTER_LEFT);
        txtNif.setPrefColumnCount(20);
        lblNif.setLabelFor(txtNif);
        // Validamos la introducción de un NIF o CIF
        txtNif.textProperty().addListener((observable, oldValue, newValue) -> {
            // Necesitamos una variable por si hay que generar el substring.
            String actualNif = newValue;
            // Si el id es un NIE eliminamos la X, Y o Z inicial para validar el resto
            if (newValue.startsWith("X") || newValue.startsWith("Y") || newValue.startsWith("Z")) {
                actualNif = newValue.substring(1);
            }

            // Validamos NIF y CIF
            if (!actualNif.matches("\\d{8}[A-HJ-NP-TV-Z]") &&
                    !actualNif.matches("[ABCDEFGHJKLMNPQRSUVW]\\d{7}[0-9A-J]")) {
                txtNif.setStyle("-fx-border-color: red");
            } else {
                txtNif.setStyle("-fx-border-color: green");
                validators.setValue(new Boolean[]{true, validators.getValue()[1], validators.getValue()[2], validators.getValue()[3], validators.getValue()[4], validators.getValue()[5], validators.getValue()[6], validators.getValue()[7]});
            }
        });
        // Nombre
        Label lblNombre = new Label("Nombre");
        lblNombre.setFont(FontsDictionary.getFont("label"));
        lblNombre.setTextFill(ColorsDictionary.getColor("text-light"));
        lblNombre.setAlignment(Pos.CENTER_LEFT);
        TextField txtNombre = new TextField();
        txtNombre.setFont(FontsDictionary.getFont("text"));
        txtNombre.setPromptText("Nombre");
        txtNombre.setAlignment(Pos.CENTER_LEFT);
        txtNombre.setPrefColumnCount(20);
        lblNombre.setLabelFor(txtNombre);
        // Validamos la introducción de un nombre
        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            // Permitimos letras mayúsculas y minúsculas y espacios
            if (!newValue.matches("[A-Za-z\\u00F1 ]*") || newValue.length() < 3) {
                txtNombre.setStyle("-fx-border-color: red");
            } else {
                txtNombre.setStyle("-fx-border-color: green");
                validators.setValue(new Boolean[]{validators.getValue()[0], true, validators.getValue()[2], validators.getValue()[3], validators.getValue()[4], validators.getValue()[5], validators.getValue()[6], validators.getValue()[7]});
            }
        });
        /// Email
        Label lblEmail = new Label("Email");
        lblEmail.setFont(FontsDictionary.getFont("label"));
        lblEmail.setTextFill(ColorsDictionary.getColor("text-light"));
        lblEmail.setAlignment(Pos.CENTER_LEFT);
        TextField txtEmail = new TextField();
        txtEmail.setFont(FontsDictionary.getFont("text"));
        txtEmail.setPromptText("Email");
        txtEmail.setAlignment(Pos.CENTER_LEFT);
        txtEmail.setPrefColumnCount(20);
        lblEmail.setLabelFor(txtEmail);
        // Validamos la introducción de un email
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            // Usamos Apache Commons para validar el email
            if (!EmailValidator.getInstance().isValid(newValue)) {
                txtEmail.setStyle("-fx-border-color: red");
            } else {
                txtEmail.setStyle("-fx-border-color: green");
                validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], true, validators.getValue()[3], validators.getValue()[4], validators.getValue()[5], validators.getValue()[6], validators.getValue()[7]});
            }
        });
        // Dirección
        Label lblDireccion = new Label("Dirección");
        lblDireccion.setFont(FontsDictionary.getFont("label"));
        lblDireccion.setTextFill(ColorsDictionary.getColor("text-light"));
        lblDireccion.setAlignment(Pos.CENTER_LEFT);
        TextField txtDireccion = new TextField();
        txtDireccion.setFont(FontsDictionary.getFont("text"));
        txtDireccion.setPromptText("Dirección");
        txtDireccion.setAlignment(Pos.CENTER_LEFT);
        txtDireccion.setPrefColumnCount(20);
        lblDireccion.setLabelFor(txtDireccion);
        // Validamos la introducción de una dirección
        txtDireccion.textProperty().addListener((observable, oldValue, newValue) -> {
            // Permitimos letras mayúsculas y minúsculas, números, comas, - y espacios
            if (!newValue.matches("[A-Za-z0-9,\\-\\u00F1 ]*") || newValue.length() < 3) {
                txtDireccion.setStyle("-fx-border-color: red");
            } else {
                txtDireccion.setStyle("-fx-border-color: green");
                validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], validators.getValue()[2], true, validators.getValue()[4], validators.getValue()[5], validators.getValue()[6], validators.getValue()[7]});
            }
        });
        // Población
        Label lblPoblacion = new Label("Población");
        lblPoblacion.setFont(FontsDictionary.getFont("label"));
        lblPoblacion.setTextFill(ColorsDictionary.getColor("text-light"));
        lblPoblacion.setAlignment(Pos.CENTER_LEFT);
        TextField txtPoblacion = new TextField();
        txtPoblacion.setFont(FontsDictionary.getFont("text"));
        txtPoblacion.setPromptText("Población");
        txtPoblacion.setAlignment(Pos.CENTER_LEFT);
        txtPoblacion.setPrefColumnCount(20);
        lblPoblacion.setLabelFor(txtPoblacion);
        // Validamos la introducción de una población
        txtPoblacion.textProperty().addListener((observable, oldValue, newValue) -> {
            // Permitimos letras mayúsculas y minúsculas y espacios
            if (!newValue.matches("[A-Za-z\\u00F1 ]*") || newValue.length() < 3) {
                txtPoblacion.setStyle("-fx-border-color: red");
            } else {
                txtPoblacion.setStyle("-fx-border-color: green");
                validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], validators.getValue()[2], validators.getValue()[3], true, validators.getValue()[5], validators.getValue()[6], validators.getValue()[7]});
            }
        });
        // Provincia
        Label lblProvincia = new Label("Provincia");
        lblProvincia.setFont(FontsDictionary.getFont("label"));
        lblProvincia.setTextFill(ColorsDictionary.getColor("text-light"));
        lblProvincia.setAlignment(Pos.CENTER_LEFT);
        TextField txtProvincia = new TextField();
        txtProvincia.setFont(FontsDictionary.getFont("text"));
        txtProvincia.setPromptText("Provincia");
        txtProvincia.setAlignment(Pos.CENTER_LEFT);
        txtProvincia.setPrefColumnCount(20);
        lblProvincia.setLabelFor(txtProvincia);
        // Validamos la introducción de una provincia
        txtProvincia.textProperty().addListener((observable, oldValue, newValue) -> {
            // Permitimos letras mayúsculas y minúsculas y espacios
            if (!newValue.matches("[A-Za-z\\u00F1 ]*") || newValue.length() < 3) {
                txtProvincia.setStyle("-fx-border-color: red");
            } else {
                txtProvincia.setStyle("-fx-border-color: green");
                validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], validators.getValue()[2], validators.getValue()[3], validators.getValue()[4], true, validators.getValue()[6], validators.getValue()[7]});
            }
        });
        // Código postal -> Formato String
        Label lblCodigoPostal = new Label("Código postal");
        lblCodigoPostal.setFont(FontsDictionary.getFont("label"));
        lblCodigoPostal.setTextFill(ColorsDictionary.getColor("text-light"));
        lblCodigoPostal.setAlignment(Pos.CENTER_LEFT);
        TextField txtCodigoPostal = new TextField();
        txtCodigoPostal.setFont(FontsDictionary.getFont("text"));
        txtCodigoPostal.setPromptText("Código postal");
        txtCodigoPostal.setAlignment(Pos.CENTER_LEFT);
        txtCodigoPostal.setPrefColumnCount(5);
        lblCodigoPostal.setLabelFor(txtCodigoPostal);
        // Validamos la introducción de un código postal
        txtCodigoPostal.textProperty().addListener((observable, oldValue, newValue) -> {
            // Permitimos números y un máximo de 5 caracteres
            if (newValue.length() < 5) {
                txtCodigoPostal.setStyle("-fx-border-color: red");
            } else {
                // Comprobamos que solo se hayan introducido números
                try {
                    Integer.parseInt(newValue);
                    txtCodigoPostal.setStyle("-fx-border-color: green");
                    validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], validators.getValue()[2], validators.getValue()[3], validators.getValue()[4], validators.getValue()[5], true, validators.getValue()[7]});
                } catch (NumberFormatException e) {
                    txtCodigoPostal.setStyle("-fx-border-color: red");
                }
            }
        });
        // País
        Label lblPais = new Label("País");
        lblPais.setFont(FontsDictionary.getFont("label"));
        lblPais.setTextFill(ColorsDictionary.getColor("text-light"));
        lblPais.setAlignment(Pos.CENTER_LEFT);
        TextField txtPais = new TextField();
        txtPais.setFont(FontsDictionary.getFont("text"));
        txtPais.setPromptText("País");
        txtPais.setAlignment(Pos.CENTER_LEFT);
        txtPais.setPrefColumnCount(20);
        lblPais.setLabelFor(txtPais);
        // Validamos la introducción de un país
        txtPais.textProperty().addListener((observable, oldValue, newValue) -> {
            // Permitimos letras mayúsculas y minúsculas y espacios
            if (!newValue.matches("[A-Za-z\\u00F1 ]*")) {
                txtPais.setStyle("-fx-border-color: red");
            } else {
                txtPais.setStyle("-fx-border-color: green");
                validators.setValue(new Boolean[]{validators.getValue()[0], validators.getValue()[1], validators.getValue()[2], validators.getValue()[3], validators.getValue()[4], validators.getValue()[5], validators.getValue()[6], true});
            }
        });

        // Creamos el botón de confirmación y el de cancelación
        Button btnAnadir = new Button("Añadir");
        btnAnadir.setFont(FontsDictionary.getFont("button"));
        // El botón estará desactivado hasta que todos los campos sean válidos
        btnAnadir.setDisable(true);
        validators.addListener(
                (observable, oldValue, newValue) -> btnAnadir.setDisable(!Arrays.stream(validators.getValue()).allMatch(value -> value))
        );

        // Cargamos los datos y los enviamos al controlador
        btnAnadir.setOnAction(event -> {
            // Creamos un cliente con los datos introducidos
            controller.addCliente(txtNombre.getText(), txtNif.getText(), txtEmail.getText(), txtDireccion.getText(),
                    txtPoblacion.getText(), txtProvincia.getText(), txtCodigoPostal.getText(), txtPais.getText(),
                    tipoCliente.getValue())
                    .ifPresentOrElse(data -> {
                        nuevoCliente.setValue(data);
                        throwExitPane();
                    }, () -> throwErrorPane("crear"));
            // Limpiamos el formulario
            txtNif.clear();
            txtNombre.clear();
            txtEmail.clear();
            txtDireccion.clear();
            txtPoblacion.clear();
            txtProvincia.clear();
            txtCodigoPostal.clear();
            txtPais.clear();

            // Rellenamos la card con los datos del cliente creado
            if (nuevoCliente.getValue().getClass().getSimpleName().equals("Premium")) {
                card.getChildren().add(mapPremium((ClientePremium) nuevoCliente.getValue()));
            } else {
                card.getChildren().add(mapEstandard(nuevoCliente.getValue()));
            }
        });

        // Creamos el botón de cancelación
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setFont(FontsDictionary.getFont("button"));
        // Si pulsamos el botón, limpiamos el formulario
        btnCancelar.setOnAction(event -> {
            txtNif.clear();
            txtNombre.clear();
            txtEmail.clear();
            txtDireccion.clear();
            txtPoblacion.clear();
            txtProvincia.clear();
            txtCodigoPostal.clear();
            txtPais.clear();
        });

        // Creamos el panel de botones
        buttons.getChildren().addAll(btnAnadir, btnCancelar);

        // Creamos el pane del formulario
        form.addRow(0, lblNif, txtNif);
        form.addRow(1, lblNombre, txtNombre, lblEmail, txtEmail);
        form.addRow(2, lblDireccion, txtDireccion);
        form.addRow(3, lblPoblacion, txtPoblacion, lblProvincia, txtProvincia);
        form.addRow(4, lblCodigoPostal, txtCodigoPostal, lblPais, txtPais);
         formBox.getChildren().addAll(form, buttons);

        // Gestionamos la visibiliad del card, necesitamos un listener para nuevo cliente
        nuevoCliente.addListener((observable, oldValue, newValue) -> {
            if (newValue.getId() != null) card.setVisible(true);
        });

        root.getChildren().addAll(title, optTipoCliente, formBox, card);
        return root;
    }

    // Obtenemos un cliente por su NIF
    private static Pane showClienteByNif() {
        // Obtenemos una lista con los NIFs de los clientes para hacer la búsqueda más interactiva.
        ArrayList<String> nifs = new ArrayList<>();
        try {
            nifs = controller.listClientesNif().orElseGet(() -> {
                throwErrorPane("lista");
                return new ArrayList<>();
            });
        } catch (Exception e) {
            throwErrorPane("excepcion");
            e.printStackTrace();
        }

        // Vamos a necesitar un formulario
        // Creamos el panel raíz
        VBox root = new VBox();
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Card para el cliente
        GridPane card = new GridPane();

        // Creamos el título
        Text title = new Text("Buscar cliente por NIF");
        title.setFont(FontsDictionary.getFont("subtitle"));
        title.setFill(ColorsDictionary.getColor("text-light"));

        // Si hay clientes, creamos el formulario
        if (nifs.size() > 0) {
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
                // Capturamos el nif del formulario y buscamos el cliente.
                Cliente actualCliente = controller.buscarCliente(inputNif.getValue()).orElseGet(() -> {
                    throwErrorPane("nif");
                    return null;
                });

                // Componemos la card
                card.getChildren().clear();
                // Discriminamos el cliente
                assert actualCliente != null;
                if (actualCliente.getClass().getSimpleName().equals("ClientePremium")) {
                    card.getChildren().add(mapPremium((ClientePremium) actualCliente));
                } else {
                    card.getChildren().add(mapEstandard(actualCliente));
                }
            });

            // Añadimos el formulario
            form.getChildren().addAll(lblNif, inputNif, btnBuscar);

            // Cargamos los elementos de la vista
            root.getChildren().addAll(title, form, card);
        } else {
            // Si no hay clientes, mostramos un mensaje
            Text noClientes = new Text("No hay clientes que mostrar.");
            noClientes.setFont(FontsDictionary.getFont("subtitle"));
            noClientes.setFill(ColorsDictionary.getColor("text-light"));
            root.getChildren().addAll(title, noClientes);
        }
        return root;
    }

    // Obtenemos una lista de los clientes filtrados
    private static Pane filterClientes() {
        // Necesitaremos una lista de clientes
        ArrayList<Cliente> clientes = new ArrayList<>();

        // Creamos el panel raíz
        VBox root = new VBox();
        // Establecemos el color de fondo
        Background background = new Background(new BackgroundFill(ColorsDictionary.getColor("background-dark"), CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(background);
        // Espaciado entre elementos
        root.setSpacing(0.5 * rem);

        // Necesitaremos un panel formulario
        HBox form = new HBox();
        form.setSpacing(0.5 * rem);

        // Y un pane para el contenido devuelto
        GridPane content = new GridPane();

        // Creamos el título
        Text title = new Text("Mostrar clientes por tipo");
        title.setFont(FontsDictionary.getFont("subtitle"));
        title.setFill(ColorsDictionary.getColor("text-light"));

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

        // Comprobamos que haya clientes
        if (controller.listClientes().isPresent()) {
            // Creamos los campos del formulario
            Label lblTipo = new Label("Esocge el tipo de cliente");
            lblTipo.setFont(FontsDictionary.getFont("label"));
            lblTipo.setTextFill(ColorsDictionary.getColor("text-light"));
            // Creamos el campo select -> Usamos un RadioButton para seleccionar el tipo de cliente
            // Para gestionar que solo se pueda seleccionar uno, usamos un ToggleGroup
            ToggleGroup tipoCliente = new ToggleGroup();
            // Creamos los radio buttons
            RadioButton rbEstandard = new RadioButton("Estandard");
            rbEstandard.setFont(FontsDictionary.getFont("button"));
            rbEstandard.setTextFill(ColorsDictionary.getColor("text-light"));
            RadioButton rbPremium = new RadioButton("Premium");
            rbPremium.setFont(FontsDictionary.getFont("button"));
            rbPremium.setTextFill(ColorsDictionary.getColor("text-light"));
            // Añadimos los radio buttons al grupo
            rbEstandard.setToggleGroup(tipoCliente);
            rbPremium.setToggleGroup(tipoCliente);

            // Añadimos los elementos al formulario
            form.getChildren().addAll(lblTipo, rbEstandard, rbPremium);

            // Cuando seleccionemos el radio button, se ejecutará la acción
            tipoCliente.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                // Comprobamos que el nuevo valor no sea nulo
                if (newValue != null) {
                    // Limpiamos el contenido
                    content.getChildren().clear();
                    // Vaciamos la lista
                    clientes.clear();
                    // Obtenemos el valor del radio button seleccionado
                    String tipo = ((RadioButton) newValue).getText();
                    // Devolvemos los clientes filtrados
                    clientes.addAll(controller.filtrarClientes(tipo).orElseGet(ArrayList::new));

                    // Necesitamos una variable final
                    final int[] ind = {0};
                    ArrayList<Cliente> finalClientes = clientes;
                    // Comprobamos que haya clientes
                    if (!clientes.isEmpty()) {
                        // Discriminar el tipo de cliente y obtener el primer cliente
                        if (tipo.equals("Estandard")) {
                            content.getChildren().add(mapEstandard(finalClientes.get(ind[0])));
                        } else {
                            content.getChildren().add(mapPremium((ClientePremium) finalClientes.get(ind[0])));
                        }

                        // Añadimos la lógica de los botones
                        // Lógica anterior
                        btnAnterior.setFont(FontsDictionary.getFont("button"));
                        // Ocultamos el botón anterior si es el primer artículo
                        btnAnterior.setVisible(false);
                        btnAnterior.setOnAction(event -> {
                            // Limpiamos los campos
                            content.getChildren().remove(0, content.getChildren().size());
                            if (ind[0] > 0) {
                                ind[0]--;
                                // Actualizamos el cliente
                                // Discriminamos el tipo de cliente
                                if (finalClientes.get(ind[0]).getClass().getSimpleName().equals("ClientePremium")) {
                                    content.getChildren().add(mapPremium((ClientePremium) finalClientes.get(ind[0])));
                                } else {
                                    content.getChildren().add(mapEstandard(finalClientes.get(ind[0])));
                                }

                                // Mostramos el botón siguiente si no es el último artículo
                                btnSiguiente.setVisible(ind[0] < finalClientes.size() - 1);

                                // Ocultamos el botón si es el primer artículo
                                btnAnterior.setVisible(ind[0] > 0);
                            }
                        });

                        // Lógica siguiente
                        btnSiguiente.setFont(FontsDictionary.getFont("button"));
                        // Si no es el último artículo, se muestra el botón
                        btnSiguiente.setVisible(ind[0] < finalClientes.size() - 1);
                        btnSiguiente.setOnAction(event -> {
                            // Limpiamos los campos
                            content.getChildren().remove(0, content.getChildren().size());
                            if (ind[0] < finalClientes.size() - 1) {
                                ind[0]++;
                                // Actualizamos el cliente
                                // Discriminamos el tipo de cliente
                                if (finalClientes.get(ind[0]).getClass().getSimpleName().equals("ClientePremium")) {
                                    content.getChildren().add(mapPremium((ClientePremium) finalClientes.get(ind[0])));
                                } else {
                                    content.getChildren().add(mapEstandard(finalClientes.get(ind[0])));
                                }

                                btnAnterior.setVisible(true);

                                // Ocultamos el botón si es el último artículo
                                btnSiguiente.setVisible(ind[0] < finalClientes.size() - 1);
                            }
                        });
                    }
                }
            });
            // Añadimos los elementos al pane
            root.getChildren().addAll(title, form, content, buttons);
        } else {
            // Si no hay clientes, mostramos un mensaje
            Text noClientes = new Text("No existen clientes registrados.");
            noClientes.setFont(FontsDictionary.getFont("subtitle"));
            noClientes.setFill(ColorsDictionary.getColor("text-light"));

            // Creamos el pane
            root.getChildren().addAll(title, noClientes);
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
            case "lista" -> errorAlert.setContentText("No se ha podido obtener la lista de clientes");
            case "crear" -> errorAlert.setContentText("No se ha podido añadir el cliente");
            case "filtrar" -> errorAlert.setContentText("No se ha podido filtrar la lista de clientes");
            case "buscar" -> errorAlert.setContentText("No se ha podido eliminar el cliente");
            case "excepcion" -> errorAlert.setContentText("Ha ocurrido un error no definido");
            case "nif" -> errorAlert.setContentText("No existe ningún cliente con ese NIF");
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
    private static void throwExitPane() {
        // Creamos el diálogo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Clientes");

        // Creamos el mensaje
        alert.setContentText("Se ha creado el cliente correctamente");

        // Estilamos el diálogo
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String cssFile = Objects.requireNonNull(classLoader.getResource("styles.css")).toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssFile);

        // Mostramos el diálogo
        alert.showAndWait();
    }

    // Mapeamos un cliente estandard
    private static Pane mapEstandard(Cliente srcCliente) {
        // Creamos el panel para mostrar la información
        GridPane content = new GridPane();
        content.setPadding(new Insets(0.5 * rem));
        content.setHgap(0.2 * rem);
        content.setVgap(0.2 * rem);

        // Creamos los elementos del panel
        // NIF
        Label lblNif = new Label("NIF");
        lblNif.setFont(FontsDictionary.getFont("label"));
        lblNif.setTextFill(ColorsDictionary.getColor("text-light"));
        Text nif = new Text(srcCliente.getNif());
        lblNif.setLabelFor(nif);
        // Nombre
        Label lblNombre = new Label("Nombre");
        lblNombre.setFont(FontsDictionary.getFont("label"));
        lblNombre.setTextFill(ColorsDictionary.getColor("text-light"));
        Text nombre = new Text(srcCliente.getNombre());
        lblNombre.setLabelFor(nombre);
        // Email
        Label lblEmail = new Label("Email");
        lblEmail.setFont(FontsDictionary.getFont("label"));
        lblEmail.setTextFill(ColorsDictionary.getColor("text-light"));
        Text email = new Text(srcCliente.getEmail());
        lblEmail.setLabelFor(email);
        // Dirección
        Label lblDomicilio = new Label("Domicilio");
        lblDomicilio.setFont(FontsDictionary.getFont("label"));
        lblDomicilio.setTextFill(ColorsDictionary.getColor("text-light"));
        Text domicilio = new Text(srcCliente.getDomicilio().getDireccion());
        lblDomicilio.setLabelFor(domicilio);
        // Población
        Label lblPoblacion = new Label("Población");
        lblPoblacion.setFont(FontsDictionary.getFont("label"));
        lblPoblacion.setTextFill(ColorsDictionary.getColor("text-light"));
        Text poblacion = new Text(srcCliente.getDomicilio().getCiudad());
        lblPoblacion.setLabelFor(poblacion);
        // Provincia
        Label lblProvincia = new Label("Provincia");
        lblProvincia.setFont(FontsDictionary.getFont("label"));
        lblProvincia.setTextFill(ColorsDictionary.getColor("text-light"));
        Text provincia = new Text(srcCliente.getDomicilio().getProvincia());
        lblProvincia.setLabelFor(provincia);
        // Código postal
        Label lblCodigoPostal = new Label("Código postal");
        lblCodigoPostal.setFont(FontsDictionary.getFont("label"));
        lblCodigoPostal.setTextFill(ColorsDictionary.getColor("text-light"));
        Text codigoPostal = new Text(srcCliente.getDomicilio().getCodigoPostal());
        lblCodigoPostal.setLabelFor(codigoPostal);
        // País
        Label lblPais = new Label("País");
        lblPais.setFont(FontsDictionary.getFont("label"));
        lblPais.setTextFill(ColorsDictionary.getColor("text-light"));
        Text pais = new Text(srcCliente.getDomicilio().getPais());
        lblPais.setLabelFor(pais);

        // Añadimos los elementos al panel
        content.add(lblNif, 0, 0);
        content.add(nif, 1, 0);
        content.add(lblNombre, 2, 0);
        content.add(nombre, 3, 0);
        content.add(lblEmail, 0, 1);
        content.add(email, 1, 1);
        content.add(lblDomicilio, 0, 2);
        content.add(domicilio, 1, 2, 3, 1);
        content.add(lblPoblacion, 0, 3);
        content.add(poblacion, 1, 3);
        content.add(lblProvincia, 2, 3);
        content.add(provincia, 3, 3);
        content.add(lblCodigoPostal, 0, 4);
        content.add(codigoPostal, 1, 4);
        content.add(lblPais, 2, 4);
        content.add(pais, 3, 4);

        // Devolvemos el pane
        return content;
    }

    // Mapeamos un cliente premium
    private static Pane mapPremium(ClientePremium srcCliente) {
        // Creamos el panel para mostrar la información
        GridPane content = new GridPane();
        content.setPadding(new Insets(0.5 * rem));
        content.setHgap(0.2 * rem);
        content.setVgap(0.2 * rem);

        // Creamos los elementos del panel
        // NIF
        Label lblNif = new Label("NIF");
        lblNif.setFont(FontsDictionary.getFont("label"));
        lblNif.setTextFill(ColorsDictionary.getColor("text-light"));
        Text nif = new Text(srcCliente.getNif());
        lblNif.setLabelFor(nif);
        // Nombre
        Label lblNombre = new Label("Nombre");
        lblNombre.setFont(FontsDictionary.getFont("label"));
        lblNombre.setTextFill(ColorsDictionary.getColor("text-light"));
        Text nombre = new Text(srcCliente.getNombre());
        lblNombre.setLabelFor(nombre);
        // Email
        Label lblEmail = new Label("Email");
        lblEmail.setFont(FontsDictionary.getFont("label"));
        lblEmail.setTextFill(ColorsDictionary.getColor("text-light"));
        Text email = new Text(srcCliente.getEmail());
        lblEmail.setLabelFor(email);
        // Dirección
        Label lblDomicilio = new Label("Domicilio");
        lblDomicilio.setFont(FontsDictionary.getFont("label"));
        lblDomicilio.setTextFill(ColorsDictionary.getColor("text-light"));
        Text domicilio = new Text(srcCliente.getDomicilio().getDireccion());
        lblDomicilio.setLabelFor(domicilio);
        // Población
        Label lblPoblacion = new Label("Población");
        lblPoblacion.setFont(FontsDictionary.getFont("label"));
        lblPoblacion.setTextFill(ColorsDictionary.getColor("text-light"));
        Text poblacion = new Text(srcCliente.getDomicilio().getCiudad());
        lblPoblacion.setLabelFor(poblacion);
        // Provincia
        Label lblProvincia = new Label("Provincia");
        lblProvincia.setFont(FontsDictionary.getFont("label"));
        lblProvincia.setTextFill(ColorsDictionary.getColor("text-light"));
        Text provincia = new Text(srcCliente.getDomicilio().getProvincia());
        lblProvincia.setLabelFor(provincia);
        // Código postal
        Label lblCodigoPostal = new Label("Código postal");
        lblCodigoPostal.setFont(FontsDictionary.getFont("label"));
        lblCodigoPostal.setTextFill(ColorsDictionary.getColor("text-light"));
        Text codigoPostal = new Text(srcCliente.getDomicilio().getCodigoPostal());
        lblCodigoPostal.setLabelFor(codigoPostal);
        // País
        Label lblPais = new Label("País");
        lblPais.setFont(FontsDictionary.getFont("label"));
        lblPais.setTextFill(ColorsDictionary.getColor("text-light"));
        Text pais = new Text(srcCliente.getDomicilio().getPais());
        lblPais.setLabelFor(pais);
        // Creamos los campos específicos de cliente premium
        // Código de cliente
        Label lblCodigoSocio = new Label("Código de socio");
        lblCodigoSocio.setFont(FontsDictionary.getFont("label"));
        lblCodigoSocio.setTextFill(ColorsDictionary.getColor("text-light"));
        Text codigoSocio = new Text(srcCliente.getCodSocio());
        lblCodigoSocio.setLabelFor(codigoSocio);
        // Cuota de socio
        Label lblCuota = new Label("Cuota anual de socio");
        lblCuota.setFont(FontsDictionary.getFont("label"));
        lblCuota.setTextFill(ColorsDictionary.getColor("text-light"));
        Text cuota = new Text(Double.toString(srcCliente.getCuota()) + "€");
        lblCuota.setLabelFor(cuota);
        // Descuento de socio
        Label lblDescuento = new Label("Descuento de socio");
        lblDescuento.setFont(FontsDictionary.getFont("label"));
        lblDescuento.setTextFill(ColorsDictionary.getColor("text-light"));
        Text descuento = new Text(Double.toString(srcCliente.getDescuento() * 100) + "%");
        lblDescuento.setLabelFor(descuento);

        // Añadimos los elementos al panel
        content.add(lblNif, 0, 0);
        content.add(nif, 1, 0);
        content.add(lblNombre, 2, 0);
        content.add(nombre, 3, 0);
        content.add(lblEmail, 0, 1);
        content.add(email, 1, 1);
        content.add(lblDomicilio, 0, 2);
        content.add(domicilio, 1, 2, 3, 1);
        content.add(lblPoblacion, 0, 3);
        content.add(poblacion, 1, 3);
        content.add(lblProvincia, 2, 3);
        content.add(provincia, 3, 3);
        content.add(lblCodigoPostal, 0, 4);
        content.add(codigoPostal, 1, 4);
        content.add(lblPais, 2, 4);
        content.add(pais, 3, 4);

        // Añadimos el contenido premium
        content.add(lblCodigoSocio, 0, 5);
        content.add(codigoSocio, 1, 5);
        content.add(lblCuota, 0, 6);
        content.add(cuota, 1, 6);
        content.add(lblDescuento, 2, 6);
        content.add(descuento, 3, 6);

        // Devolvemos el pane
        return content;
    }

    // Método para devolver el formulario de creación de clientes a otra vista.
    public Pane getAddCliente() {
        return addCliente();
    }
}

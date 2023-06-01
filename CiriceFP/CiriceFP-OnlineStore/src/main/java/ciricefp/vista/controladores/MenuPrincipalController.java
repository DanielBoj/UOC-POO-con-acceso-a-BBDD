package ciricefp.vista.controladores;

import ciricefp.controlador.Controlador;
import ciricefp.modelo.Articulo;
import ciricefp.modelo.Cliente;
import ciricefp.modelo.Pedido;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

/**
 * Esta clase implementa la lógica de negocio de la vista principal de la aplicación.
 * Esta vista ejerce también como controlador de las vistas de artículos, clientes y pedidos.
 * Para ello, delega en los métodos del Controlador Principal que necesita como atributo.
 * Esta clase es la encargada de mostrar el menú principal de la aplicación y de llamar a los métodos particulares
 * de cada una de las vistas de artículos, clientes y pedidos.
 * Se encarga de manejar la actualización de las vistas a través del Controlador y tiene que ser independiente del
 * modelo.
 *
 * @author Cirice
 */
public class MenuPrincipalController {
    // Atributos de clase.
    private Controlador controlador;
    private ArticulosController articulosController;
    private ClientesController clientesController;
    private PedidosController pedidosController;

    // Búffer de entrada de datos por teclado.
    Scanner teclado = new Scanner(System.in);

    // Constructor sin argumentos, instancia la clase y queda a la espera del controlador.
    public MenuPrincipalController() {
        this.articulosController = new ArticulosController(this);
        this.clientesController = new ClientesController(this);
        this.pedidosController = new PedidosController(this);
    }

    // Constructor con argumentos, instancia la clase y recibe el controlador y las vistas
    public MenuPrincipalController(Controlador controlador, ArticulosController articulosController, ClientesController clientesController, PedidosController pedidosController) {
        this.controlador = controlador;
        this.articulosController = articulosController;
        this.clientesController = clientesController;
        this.pedidosController = pedidosController;
    }

    /* Getters & Setters */
    public Controlador getControlador() {
        return controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public ArticulosController getVistaArticulos() {
        return articulosController;
    }

    public void setVistaArticulos(ArticulosController articulosController) {
        this.articulosController = articulosController;
    }

    public ClientesController getVistaClientes() {
        return clientesController;
    }

    public void setVistaClientes(ClientesController clientesController) {
        this.clientesController = clientesController;
    }

    public PedidosController getVistaPedidos() {
        return pedidosController;
    }

    public void setVistaPedidos(PedidosController pedidosController) {
        this.pedidosController = pedidosController;
    }

    @Override
    public String toString() {
        return "MenuPrincipalController{" +
                "controlador=" + controlador +
                ", articulosController=" + articulosController +
                ", clientesController=" + clientesController +
                ", pedidosController=" + pedidosController +
                '}';
    }


    // Métodos para mostrar las vistas de los menús --> Producto 5: Manejado por GUI
    // Métodos que manejan las acciones implementadas en el controlador.
    // Los métodos llaman a la lógica implementada en el controlador y devuelven el resultado.

    /* Articulos */
    // Añadimos un nuevo artículo recibiendo los datos por teclado.
    public boolean createArticulo(String descripcion,
                                   double pvp,
                                   double gastosEnvio,
                                   int tiempoPreparacion) {
        // Producto 5 --> Adaptamos el método a GUI
        // Creamos un objeto Articulo con los datos recibidos para manejar el posible retorno de un objeto null.
        return controlador.createArticulo(descripcion, pvp, gastosEnvio, tiempoPreparacion) != null;
    }

    // Mostramos por pantalla una lista de artículos
    public Optional<ArrayList<Articulo>> listArticulos() {
        /* Producto 5 --> Adaptamos el método a GUI */
        return Optional.of(controlador.listArticulos());
    }

    // Buscamos un artículo por su código.
    public Articulo searchArticulo(@NotNull String codigo) {

        // Primero comprobamos que la lista no esté vacía.
        if (!controlador.listArticulos().isEmpty()) {
            try{
                // Devolvemos el artículo o null si no existe.
                return controlador.searchArticulo(codigo);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("El artículo no existe o no hay artículos en la lista.");
        return null;
    }

    // Limpiamos la lista de artículos.
    public int clearArticulos() {
        // Primero comprobamos que la lista no esté vacía.
        if (!controlador.listArticulos().isEmpty()) {
            return controlador.clearArticulos().size();
        }
        return 0;
    }

    /* Clientes */

    // Añadimos un nuevo cliente recibiendo los datos por teclado.
    public Optional<Cliente> createCliente(String nombre,
                                 String domicilio,
                                 String poblacion,
                                 String provincia,
                                 String cp,
                                 String pais,
                                 String nif,
                                 String email,
                                 String tipo) {
        // Producto 5 --> Adaptamos el método a GUI
        return Optional.of(controlador.createCliente(nombre, domicilio, poblacion, provincia, cp, pais, nif, email, tipo));
    }

    // Mostramos por pantalla una lista de clientes
    public Optional<ArrayList<Cliente>> listClientes() {
        // Producto 5 --> Adaptamos el método a GUI
        return Optional.of(controlador.listClientes());
    }

    // Buscamos un cliente por su NIF
    public Optional<Cliente> searchCliente(@NotNull String nif) {
        // Primero comprobamos que la lista no esté vacía.
        if (!controlador.listClientes().isEmpty()) {
            return Optional.of(controlador.searchCliente(nif));
        }
        return Optional.empty();
    }

    public Optional<ArrayList<String>> listClientesNif() {
        // Creamos la lista de nifs
        ArrayList<String> nifs = new ArrayList<>();
        // Obtenemos los clientes
        ArrayList<Cliente> clientes = controlador.listClientes();
        // Iteramos los clientes y añadimos el nif a la lista
        clientes.forEach(
                (cliente) -> nifs.add(cliente.getNif())
        );
        // Devolvemos la lista
        return nifs.size() > 0 ? Optional.of(nifs) : Optional.of(new ArrayList<>());
    }

    // Filtramos los clientes por tipo y mostramos la lista obtenida por pantalla
    public Optional<ArrayList<Cliente>> filterClientesByType(String tipo) {
        // Producto 5 --> Adaptamos el método a GUI
        return Optional.of(controlador.filterClientesByType(tipo));
    }

    /* Pedidos */
    public Optional<Pedido> createPedido(String nifCliente, String codArticulo, int cantidad) {

        // Asignamos el cliente al pedido
        Cliente cliente = controlador.searchCliente(nifCliente);

        // Comprobamos si existe el artículo
        Articulo articulo = controlador.searchArticulo(codArticulo);
        if (articulo == null) {
            return Optional.empty();
        }

        // Creamos el pedido
        if (cliente != null && cantidad > 0) {
            try {
                return Optional.of(controlador.createPedido(cliente, articulo, cantidad));
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
        // Si no se ha creado el pedido devolvemos false
        return Optional.empty();
    }

    public boolean controlNif(String nif) {

        if (!controlador.listClientes().isEmpty()) {
            try {
                Cliente cliente = controlador.searchCliente(nif);

                if (cliente == null) {
                    System.out.println("El cliente no existe.");
                }

                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return false;
    }

    // Mostramos todos los pedidos por pantalla.
    public Optional<ArrayList<Pedido>> listPedidos() {
        // Producto 5 --> Adaptamos el método a GUI
        return Optional.of(controlador.listPedidos());
    }

    // Actualizamos el estado de los pedidos de la lista si cumplen con las condiciones de fecha de envío.
    public Optional<Integer> actualizarEstadoPedidos() {
        // Producto 5 --> Adaptamos el método a GUI
        // Primero comprobamos que la lista no esté vacía.
        if (controlador.listPedidos().isEmpty()) {
            return Optional.of(0);
        }
        // Actualizamos el estado de los pedidos
        return Optional.of(controlador.actualizarEstadoPedidos());
    }

   // Borramos un pedido de la lista, las comprobaciones se hacen en el front.
    public Optional<Boolean> deletePedido(Pedido actualPedido) {
        // Producto 5 --> Adaptamos el método a GUI
        // Primero comprobamos que la lista no esté vacía.
        if (!controlador.listPedidos().isEmpty()) {
            // Actualizamos el estado de los pedidos para que no haya errores
            actualizarEstadoPedidos();

            // Borramos el pedido
            return controlador.deletePedido(actualPedido) != null ? Optional.of(true) :
                    Optional.of(false);
        }
        // Si ha habido cualquier problema, devolvemos un Optional vacío.
        return Optional.empty();
    }

    // Obtenemos una lista de pedidos filtrados por el estado
    public Optional<ArrayList<Pedido>> filterPedidosByEstado(String opt) {
        // Producto 5 --> Adaptamos el método a GUI
        // Primero comprobamos que la lista no esté vacía.
        if (!controlador.listPedidos().isEmpty()) {
            // Actualizamos el estado de los pedidos para evitar errores
            controlador.actualizarEstadoPedidos();

            // Obtenemos la lista de pedidos filtrados por el estado
            return Optional.of(controlador.filterPedidosByEstado(opt));
        }
        // Si ha habido cualquier problema, devolvemos un Optional vacío.
        return Optional.empty();
    }

    // Obtenemos una lista de los pedidos de un cliente filtrándolos por su nif.
    public Optional<ArrayList<Pedido>> filterPedidosByCliente(@NotNull String nif) {
        // Producto 5 --> Adaptamos el método a GUI
        // Comprobamos que la lista contenga elementos
        if (!controlador.listPedidos().isEmpty()) {
            // Actualizamos el estado de los pedidos para evitar errores
            controlador.actualizarEstadoPedidos();

            // Obtenemos la lista de pedidos filtrados por el cliente
            return Optional.of(controlador.filterPedidosByCliente(nif));
        }
        // Si ha habido cualquier problema, devolvemos un Optional vacío.
        return Optional.empty();
    }

    // Preguntamos al usuario si desea cargar los datos de test de la BD y los intentanos cargar.
    // Si la BD tiene datos no se realiza la consulta.
    public void loadTestData() {
        /*// Preguntamos al usuario si desea cargar los datos de test de la BD.
        System.out.println("¿Desea cargar los datos de test de la BD?");
        System.out.println("1. Si");
        System.out.println("0. No");
        char opt = pedirOpcion(2);

        // Creamos un flag para comprobar el resultado.
        int res = 0;

        switch (opt) {
            case '1' -> {
                res = controlador.loadTestData();

                // Enviamos la respuesta al usuario.
                System.out.println("Comprobando si hay datos en la BD...");
                switch (res) {
                    case 0 -> System.out.println("La BD ya tiene datos.");
                    case 1 -> System.out.println("Datos cargados correctamente.");
                    case -1 -> System.out.println("Error al cargar los datos.");
                }

                System.out.println("Pulsa ENTER para continuar.");
                // Esperamos una pulsación de tecla para mostrar el siguiente artículo.
                teclado.nextLine();
                System.out.println("==================================");
            }
            case '0' -> System.out.println("No se han cargado los datos de test.");
        }*/
    }

    // TODO -> Validación de datos
}
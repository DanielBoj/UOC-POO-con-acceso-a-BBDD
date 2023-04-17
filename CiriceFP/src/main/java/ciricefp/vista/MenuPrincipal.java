package ciricefp.vista;

import ciricefp.controlador.Controlador;
import ciricefp.modelo.Articulo;
import ciricefp.modelo.Cliente;
import ciricefp.modelo.Pedido;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
public class MenuPrincipal {
    // Atributos de clase.
    private Controlador controlador;
    private VistaArticulos vistaArticulos;
    private VistaClientes vistaClientes;
    private VistaPedidos vistaPedidos;

    // Búffer de entrada de datos por teclado.
    Scanner teclado = new Scanner(System.in);

    // Constructor sin argumentos, instancia la clase y queda a la espera del controlador.
    public MenuPrincipal() {
        this.vistaArticulos = new VistaArticulos(this);
        this.vistaClientes = new VistaClientes(this);
        this.vistaPedidos = new VistaPedidos(this);
    }

    // Constructor con argumentos, instancia la clase y recibe el controlador y las vistas
    public MenuPrincipal(Controlador controlador, VistaArticulos vistaArticulos, VistaClientes vistaClientes, VistaPedidos vistaPedidos) {
        this.controlador = controlador;
        this.vistaArticulos = vistaArticulos;
        this.vistaClientes = vistaClientes;
        this.vistaPedidos = vistaPedidos;
    }

    /* Getters & Setters */
    public Controlador getControlador() {
        return controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public VistaArticulos getVistaArticulos() {
        return vistaArticulos;
    }

    public void setVistaArticulos(VistaArticulos vistaArticulos) {
        this.vistaArticulos = vistaArticulos;
    }

    public VistaClientes getVistaClientes() {
        return vistaClientes;
    }

    public void setVistaClientes(VistaClientes vistaClientes) {
        this.vistaClientes = vistaClientes;
    }

    public VistaPedidos getVistaPedidos() {
        return vistaPedidos;
    }

    public void setVistaPedidos(VistaPedidos vistaPedidos) {
        this.vistaPedidos = vistaPedidos;
    }

    @Override
    public String toString() {
        return "MenuPrincipal{" +
                "controlador=" + controlador +
                ", vistaArticulos=" + vistaArticulos +
                ", vistaClientes=" + vistaClientes +
                ", vistaPedidos=" + vistaPedidos +
                '}';
    }

    // Método que inicia la aplicación y muestra el menú principal por terminal.
    public void inicio() {
        boolean salir = false;
        char opcio;

        // Mostramos un menú inicial si no existen datos en la BD para que el usuario cargar datos de test.
        if (!controlador.checkData()) {
            loadTestData();
        }

        // Mostramos el menú principal.
        System.out.println("==================================");
        System.out.println("Bienvenido a la aplicación de gestión de pedidos de la empresa OnlineStore.");
        System.out.println("==================================");

        do {
            System.out.println("1. Gestión Articulos");
            System.out.println("2. Gestión Clientes");
            System.out.println("3. Gestión Pedidos");
            System.out.println("0. Salir");
            opcio = pedirOpcion(4);

            // Mostramos el menú correspondiente según la opción elegida.
            switch (opcio) {
                case '1' -> vistaArticulos();
                case '2' -> vistaClientes();
                case '3' -> vistaPedidos();
                case '0' -> salir = true;
                // Manejamos el error de opción incorrecta.
                default -> System.out.println("Opción incorrecta.");
            }
        } while (!salir);
    }

    // Método que pide una opción al usuario y la devuelve.
    public char pedirOpcion(int numOpts) {
        String resp;

        StringBuilder sb = new StringBuilder("Elige una opción (");

        // Añadimos dinámicamente las posibles opciones.
        for (int i = 0; i < numOpts; i++) {
            sb.append(i);
            if (i < numOpts - 1) {
                sb.append(",");
            }
        }
        sb.append("): ");

        System.out.println(sb);
        resp = teclado.nextLine();
        if (resp.isEmpty()) {
            resp = " ";
        }
        return resp.charAt(0);
    }

    public String pedirDatos(String mensaje) {
        String resp;

        System.out.println(mensaje);

        // Nos aseguramos de que el usuario no deje el campo vacío.
        do {
            resp = teclado.nextLine();
            if (resp.isEmpty()) {
                System.out.println("La respuesta no puede estar vacía.");
            }
        } while (resp.isEmpty());

        return resp;
    }

    // Métodos para mostrar las vistas de los menús.
    private void vistaArticulos() {
        vistaArticulos.inicio();
    }

    private void vistaClientes() {
        vistaClientes.inicio();
    }

    private void vistaPedidos() {
        vistaPedidos.inicio();
    }

    // Métodos que manejan las acciones implementadas en el controlador.
    // Los métodos llaman a la lógica implementada en el controlador y devuelven el resultado.

    /* Articulos */
    // Añadimos un nuevo artículo recibiendo los datos por teclado.
    public boolean createArticulo(String descripcion,
                                   double pvp,
                                   double gastosEnvio,
                                   int tiempoPreparacion) {

        // Creamos un objeto Articulo con los datos recibidos para manejar el posible retorno de un objeto null.
        try {
            Articulo newArticulo = controlador.createArticulo(descripcion, pvp, gastosEnvio, tiempoPreparacion);

            // Comprobamos que no sea null.
            if (newArticulo != null) {
                System.out.println("Artículo creado correctamente.");
                System.out.println(newArticulo);
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // En caso de error retornamos false
        System.out.println("Error al crear el artículo.");
        return false;
    }

    // Mostramos por pantalla una lista de artículos
    public void listArticulos() {

        System.out.println("Listado de artículos:");

        // Primero comprobamos que la lista no esté vacía.
        if (controlador.listArticulos().isEmpty()) {
            System.out.println("No hay artículos en la lista.");
            return;
        }

        // Iteramos todos los elementos de la lista esperando una pulsación de tecla para mostrar el siguiente.
        for (Articulo articulo : controlador.listArticulos()) {
            System.out.println(articulo);
            System.out.println("==================================");
            System.out.println("Pulsa ENTER para continuar.");
            // Esperamos una pulsación de tecla para mostrar el siguiente artículo.
            teclado.nextLine();
        }
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
    public void clearArticulos() {

        // Primero comprobamos que la lista no esté vacía.
        if (!controlador.listArticulos().isEmpty()) {

            // Si existen artículos en la lista, los vaciamos y mostramos por pantalla.
            try {
                ArrayList<Articulo> articulos = controlador.clearArticulos();
                System.out.println("Lista de artículos vaciada.");

                for (Articulo articulo : articulos) {
                    System.out.println(articulo);
                    System.out.println("==================================");
                    System.out.println("Pulsa ENTER para continuar.");
                    // Esperamos una pulsación de tecla para mostrar el siguiente artículo.
                    teclado.nextLine();

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("No hay artículos en la lista.");

    }

    /* Clientes */

    // Añadimos un nuevo cliente recibiendo los datos por teclado.
    public boolean createCliente(String nombre,
                                 String domicilio,
                                 String poblacion,
                                 String provincia,
                                 String cp,
                                 String pais,
                                 String nif,
                                 String email,
                                 String tipo) {
        // Creamos el nuevo objeto Cliente con los datos recibidos para manejar el posible retorno de un objeto null.
        try {
            Cliente newCliente = controlador.createCliente(nombre, domicilio, poblacion, provincia, cp, pais, nif, email, tipo);

            // Comprobamos que no sea null.
            if (newCliente != null) {
                System.out.println("Cliente creado correctamente.");
                System.out.println(newCliente);
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // En caso de error retornamos false
        System.out.println("Error al crear el cliente.");
        return false;
    }

    // Mostramos por pantalla una lista de clientes
    public void listClientes() {

        System.out.println("Lista de clientes:");

        // Primero comprobamos que la lista no esté vacía.
        if (controlador.listClientes().isEmpty()) {
            System.out.println("No hay clientes en la lista.");
            return;
        }

        // Iteramos todos los elementos de la lista esperando una pulsación de tecla para mostrar el siguiente.
        for (Cliente cliente : controlador.listClientes()) {
            System.out.println(cliente);
            System.out.println("==================================");
            System.out.println("Pulsa ENTER para continuar.");
            // Esperamos una pulsación de tecla para mostrar el siguiente artículo.
            teclado.nextLine();
        }
    }

    // Buscamos un cliente por su NIF
    public boolean searchCliente(@NotNull String nif) {

        // Primero comprobamos que la lista no esté vacía.
        if (!controlador.listClientes().isEmpty()) {
            // Buscamos si existe el cliente
            try{
                Cliente cliente = controlador.searchCliente(nif);

                // Si existe, lo mostramos por pantalla
                if (cliente != null) {
                    System.out.println("Cliente encontrado:");
                    System.out.println(cliente);
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("El cliente no existe o no hay clientes en la lista.");
        return false;
    }

    // Filtramos los clientes por tipo y mostramos la lista obtenida por pantalla
    public ArrayList<Cliente> filterClientesByType(String tipo) {

        // Primero comprobamos que la lista no esté vacía.
        if (!controlador.listClientes().isEmpty()) {
            // Obtenemos una lista filtrada por el tipo de cliente
            try {
                return controlador.filterClientesByType(tipo);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return new ArrayList<>();
    }

    /* Pedidos */
    public void createPedido(String nifCliente, String codArticulo, int cantidad) {

        // Asignamos el cliente al pedido
        Cliente cliente = controlador.searchCliente(nifCliente);

        // Comprobamos si existe el artículo
        Articulo articulo = controlador.searchArticulo(codArticulo);
        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }

        // Creamos el pedido
        if (cliente != null && cantidad > 0) {
            try {
                Pedido pedido = controlador.createPedido(cliente, articulo, cantidad);
                System.out.println("Pedido creado correctamente.");
                controlador.printTicket(pedido);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
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
    public void listPedidos() {

        System.out.println("Lista de pedidos:");

        // Primero comprobamos que la lista no esté vacía.
        if (controlador.listPedidos().isEmpty()) {
            System.out.println("No hay pedidos en la lista.");
            return;
        }

        // Iteramos todos los elementos de la lista esperando una pulsación de tecla para mostrar el siguiente.
        for (Pedido pedido : controlador.listPedidos()) {
            controlador.printTicket(pedido);
            System.out.println("==================================");
            System.out.println("Pulsa ENTER para continuar.");
            // Esperamos una pulsación de tecla para mostrar el siguiente artículo.
            teclado.nextLine();
        }
    }

    // Actualizamos el estado de los pedidos de la lista si cumplen con las condiciones de fecha de envío.
    public void actualizarEstadoPedidos() {

        // Primero comprobamos que la lista no esté vacía.
        if (controlador.listPedidos().isEmpty()) {
            System.out.println("No hay pedidos en la lista.");
            return;
        }

        // Actualizamos el estado de los pedidos
        try {
            int contadorActualizados = controlador.actualizarEstadoPedidos();

            // Mostramos el número de pedidos actualizados
            System.out.println("Se han actualizado " + contadorActualizados + " pedidos.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Esta función es algo más compleja.
     * 1. Primero comprobamos que la lista no esté vacía.
     * 2. Actualizamos el estado de los pedidos para que no haya errores.
     * 3. Obtenemos la lista de pedidos e iteramos por la lista de pedidos hasta que el usuario escoja elimar o salir.
     */
    public void deletePedido() {

        boolean isSalir = false;

        // Primero comprobamos que la lista no esté vacía.
        if (controlador.listPedidos().isEmpty()) {
            System.out.println("No hay pedidos en la lista.");
            return;
        }

        // Actualilzamos el estaddo de los pedidos para que no haya errores
        controlador.actualizarEstadoPedidos();

        // Iteramos por la lista de pedidos hasta que el usuario escoja elimar o salir
        do {
            for (Pedido pedido : controlador.listPedidos()) {
                controlador.printTicket(pedido);
                System.out.println("==================================");
                System.out.println("1. Eliminar pedido");
                System.out.println("2. Continuar");
                System.out.println("0. Salir");
                char opt = pedirOpcion(3);

                switch (opt) {
                    case '1'-> {
                        try {
                            Pedido pedidoEliminado = controlador.deletePedido(pedido);
                            System.out.println("Pedido eliminado correctamente.");
                            // Mostramos el pedido eliminado y esperamos a que el usuario pulse una tecla para continuar.
                            controlador.printTicket(pedidoEliminado);
                            System.out.println("Pulsa ENTER para continuar");
                            // Esperamos una pulsación de tecla para mostrar el siguiente artículo.
                            teclado.nextLine();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case '2' -> {
                        if (controlador.listPedidos().isEmpty()) {
                            System.out.println("No hay más pedidos en la lista.");
                            isSalir = true;
                        }
                    }
                    case '0'-> isSalir = true;
                }
            }
        } while (!isSalir && !controlador.listPedidos().isEmpty());
    }

    // Obtenemos una lista de pedidos filtrados por el estado
    public void filterPedidosByEstado(String opt) {

        // Primero comprobamos que la lista no esté vacía.
        if (controlador.listPedidos().isEmpty()) {
            System.out.println("No hay pedidos en la lista.");
            return;
        }

        // Actualizamos el estado de los pedidos para evitar errores
        controlador.actualizarEstadoPedidos();


        // Obtenemos la lista de pedidos filtrados por el estado
        ArrayList<Pedido> pedidos = controlador.filterPedidosByEstado(opt);

        // Mostramos la lista por pantalla
        System.out.println("Lista de pedidos " + opt + ":");
        for (Pedido pedido : pedidos) {
            controlador.printTicket(pedido);
            System.out.println("==================================");
            System.out.println("Pulsa ENTER para continuar.");
            // Esperamos una pulsación de tecla para mostrar el siguiente artículo.
            teclado.nextLine();
        }
    }

    // Obtenemos una lista de los pedidos de un cliente filtrándolos por su nif.
    public void filterPedidosByCliente(@NotNull String nif) {

        // Primero comprobamos que la lista no esté vacía.
        if (controlador.listPedidos().isEmpty()) {
            System.out.println("No hay pedidos en la lista.");
            return;
        }

        // Actualizamos el estado de los pedidos para evitar errores
        controlador.actualizarEstadoPedidos();

        // Obtenemos la lista de pedidos filtrados por el cliente
        try {
            ArrayList<Pedido> pedidos = controlador.filterPedidosByCliente(nif);

            // Mostramos la lista por pantalla
            System.out.println("Lista de pedidos del cliente " + nif + ":");
            for (Pedido pedido : pedidos) {
                controlador.printTicket(pedido);
                System.out.println("==================================");
                System.out.println("Pulsa ENTER para continuar.");
                // Esperamos una pulsación de tecla para mostrar el siguiente artículo.
                teclado.nextLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Preguntamos al usuario si desea cargar los datos de test de la BD y los intentanos cargar.
    // Si la BD tiene datos no se realiza la consulta.
    public void loadTestData() {
        // Preguntamos al usuario si desea cargar los datos de test de la BD.
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
        }
    }

    // TODO -> Validación de datos
}
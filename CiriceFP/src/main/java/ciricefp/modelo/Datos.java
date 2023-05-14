package ciricefp.modelo;

import ciricefp.controlador.Controlador;
import ciricefp.modelo.interfaces.ICliente;
import ciricefp.modelo.interfaces.factory.IClienteFactory;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.repositorio.*;
import ciricefp.modelo.repositorio.testdataloader.LoadDataImpl;
import ciricefp.modelo.repositorio.testdataloader.LoadDataRepositorio;
import ciricefp.modelo.repositorio.testdataloader.LoadDataService;
import ciricefp.modelo.repositorio.testdataloader.LoadDataServiceImpl;
import ciricefp.modelo.services.ArticuloServiceImpl;
import ciricefp.modelo.services.ClienteServiceImpl;
import ciricefp.modelo.services.DireccionServiceImpl;
import ciricefp.modelo.services.PedidoServiceImpl;
import ciricefp.modelo.services.interfaces.ArticuloService;
import ciricefp.modelo.services.interfaces.ClienteService;
import ciricefp.modelo.services.interfaces.DireccionService;
import ciricefp.modelo.services.interfaces.PedidoService;
import ciricefp.modelo.utils.Conexion;
import ciricefp.modelo.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Esta clase funciona como un controlador interno para el módulo Modelo siguiendo el patrón MVC.
 * Implementa todos los métodos CRUD para los datos de la tienda.
 * También implementa métodos para la gestión de los pedidos.
 * Producto 3: Implementación de la capa de acceso a datos en BBDD.
 *
 * @author Cirice
 */
public class Datos {
    // Atributos de clase
    private Controlador controlador;

    // Producto 3 -> Eliminamos las listas de clientes, artículos y pedidos al implementar la
    // interacción con la BD.
    // private Listas<Cliente> clientes;
    // private Listas<Articulo> articulos;
    // private Listas<Pedido> pedidos;

    /* Producto 4 -> Refactorizamos la clase para usar Entity Manager. */
    private EntityManager em;

    // Constructor por defecto --> Inicializa las listas de clientes, artículos y pedidos.
    public Datos() {
    }

    // Constructor con parámetros --> Inicializa las listas de clientes, artículos y pedidos.
    public Datos(Controlador controlador, EntityManager em) {
        this.controlador = controlador;
        this.em = em;
    }

    // Producto 3 -/> Al implementar el acceso a la BD ya no necesitamos este constructor.
    // Constructor con parámetros --> Inicializa las listas de clientes, artículos y pedidos.

    /* Getters & Setters */
    public Controlador getControlador() {
        return controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    // StringBuilder nos permite implementar un patrón de diseño de string para el método toString() de una forma visual muy clara.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Controlador interno del Modelo:\n");
        sb.append(controlador).append("||").append(em);

        return sb.toString();
    }

    /* Métodos de la clase
     * Producto 4 -> Refactorizamos los métodos para usar Entity Manager.
     * */
    /* Articulos */
    // Añadimos un artículo a la bd instanciándolo con los parámetros recibidos.
    public Optional<Articulo> createArticulo(String descripcion, double precio, double gastosEnvio, int preparacion) {
        // Creamos el objeto artículo.
        Articulo articulo = new Articulo(descripcion, precio, gastosEnvio, preparacion);

        // Producto 4 ≥ Usamos los servicios
        ArticuloService service = new ArticuloServiceImpl(this.em);

        // Intentamos añadir el artículo a la BD.
        // Comprobamos si el artículo ya existe
        if (!checkArticulo(articulo)) {
            // Añadimos el artículo a la BD
            // El manejo de excepciones se realiza desde la clase Service.
            boolean res = service.save(articulo);

            if (res) {
                // Aumentamos el número de artículos
                Articulo.avanzarContador();
            }

            // Devolvemos el último artículo creado o null en caso de error.
            return res ? service.getLast() : Optional.empty();

        } else {
            System.out.println(MessageFormat.format("El artículo {0} ya existe", articulo.getDescripcion()));
            return Optional.empty();
        }
    }

    // Añadimos un artículo a la bd recibiéndolo por parámetro
    public Optional<Articulo> createArticulo(@NotNull Articulo articulo) {
        // Producto 4 ≥ Usamos los servicios
        ArticuloService service = new ArticuloServiceImpl(this.em);

        // Intentamos añadir el artículo a la BD.
        // Comprobamos si el artículo ya existe
        if (!checkArticulo(articulo)) {
            // Añadimos el artículo a la BD
            // El manejo de excepciones se realiza desde la clase Service.
            boolean res = service.save(articulo);

            if (res) {
                // Aumentamos el número de artículos
                Articulo.avanzarContador();
            }

            // Devolvemos el último artículo creado o null en caso de error.
            return res ? service.getLast() : Optional.empty();

        } else {
            System.out.println(MessageFormat.format("El artículo {0} ya existe", articulo.getDescripcion()));
            return Optional.empty();
        }
    }

    // Devolvemos una lista con todos los elementos de la lista de artículos, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Articulo> listArticulos() {
        // Producto 4 ≥ Usamos los servicios
        ArticuloService service = new ArticuloServiceImpl(this.em);

        // Ejecutamos el método para listar la entidad. El método nos devuelve una Lista, con lo que
        // podemos asignarla directamente a la lista de artículos.
        return !service.isEmpty()? service.findAll().cloneOf() : new Listas<>();
    }

    // Producto 3 --> Método para obtener un artículo de la BD a través de su id.
    public Optional<Articulo> getArticuloById(Long id) {
        // Producto 4 ≥ Usamos los servicios
        ArticuloService service = new ArticuloServiceImpl(this.em);

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un artículo.
        return !service.isEmpty()? service.findById(id) : Optional.empty();
    }

    // Producto 3 --> Cambiamos el método para obtener un artículo de la BD a través de su código.
    public Optional<Articulo> searchArticulo(@NotNull String codigo) {
        // Producto 4 ≥ Usamos los servicios
        ArticuloService service = new ArticuloServiceImpl(this.em);

        // Comprobamos que haya artículos en la BD y lo devolvemos.
        return !service.isEmpty() ? service.findOne(codigo) : Optional.empty();
    }

    // TODO -> Como extra podemos crear métodos para eliminar y modificar artículos

    // Eliminamos todos los elementos de la lista de artículos y devolvemos una lista con los elementos eliminados
    public ArrayList<Articulo> clearArticulos() {
        // Producto 4 ≥ Usamos los servicios
        ArticuloService service = new ArticuloServiceImpl(this.em);

        // Creamos una lista temporal quer devolveremos en caso de éxito
        ArrayList<Articulo> articulosTemp = listArticulos().getLista();

        // Flag de control
        AtomicBoolean flag = new AtomicBoolean(false);

        // Ejecutamos el método para eliminar todos los elementos de la entidad.
        // Vamos a usar una función lambda para simplificar el código.
        try {
            listArticulos().forEach(articulo -> flag.set(service.delete(articulo.getId())));
            if (flag.get()) {
                // Reseteamos el contador de artículos
                Articulo.resetContador();

                // Reseteamos el contador id en la DB.
                service.resetId();
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error al limpiar la lista de artículos.");
            e.printStackTrace();
        }

        // En caso de producirse un error o no haber podido ejecutar la limpieza devolvemos una lista vacía
        return flag.get() ? articulosTemp : new ArrayList<>();
    }

    /* Clientes */

    // Añadimos un cliente generando la instancia en el método
    public Optional<Cliente> createCliente(String nombre, Direccion domicilio, String nif, String email, String tipo) {
        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);

        // Añadimos el cliente a la BD, si se añade correctamente, devolvemos el Cliente.
        // Aplicamos la lógica de negocio para crear el cliente, no puede haber dos clientes con el mismo
        // nif.
        Cliente cliente = (Cliente) IClienteFactory.createCliente(nombre, domicilio, nif, email, tipo);

        // Comprobamos que el cliente no exista
        if (checkCliente(cliente)) {
            System.out.println(MessageFormat.format("El cliente con NIF {0} ya existe", cliente.getNif()));
        }

        if (service.save(cliente)) {
            // Avanzamos el contador de clientes
            Cliente.advanceTotalClientes();
            // Devolvemos el cliente
            return service.getLast();
        }
        // Si ha habido algún error devolvemos un Optional vacío.
        return Optional.empty();
    }

    // Añadimos un cliente recibiendo la instancia del mismo
    public Optional<Cliente> createCliente(@NotNull Cliente cliente) {

        /*// Producto 3 --> Manejamos la creación de un cliente a través de la BD.
        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Añadimos el cliente a la BD, si se añade correctamente, devolvemos el Cliente.
        try {

            // Comprobamos que el cliente no exista
            if (checkCliente(cliente)) {
                System.out.println(MessageFormat.format("El cliente {0} ya existe", cliente.getNombre()));
                return null;
            }

            if (repositorio.save(cliente)) {
                // Actualizamos el contador de clientes
                Cliente.advanceTotalClientes();

                // Devolvemos el cliente
                return repositorio.getLast();
            }
        } catch (NullPointerException e) {
            System.out.println("Error al crear el cliente.");
            e.printStackTrace();
        }

        return null;*/

        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);

        // Añadimos el cliente a la BD, si se añade correctamente, devolvemos el Cliente.
        // Aplicamos la lógica de negocio para crear el cliente, no puede haber dos clientes con el mismo
        // nif.
        // Comprobamos que el cliente no exista
        if (checkCliente(cliente)) {
            System.out.println(MessageFormat.format("El cliente con NIF {0} ya existe", cliente.getNif()));
        }

        if (service.save(cliente)) {
            // Avanzamos el contador de clientes
            Cliente.advanceTotalClientes();
            // Devolvemos el cliente
            return service.getLast();
        }
        // Si ha habido algún error devolvemos un Optional vacío.
        return Optional.empty();
    }

    // Devolvemos una lista con todos los elementos de la lista de clientes, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Cliente> listClientes() {
        // Prodcuto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);

        // Comprobamos que la lista no esté vacía, si hay elementos devolvemos la lista de clientes, si no, devolvemos
        // una lista vacía.
        return !service.isEmpty() ? service.findAll().cloneOf() : new Listas<>();


        /*// Producto 3 -> Obtenemos la lista de clientes de la BD a través del Repositorio.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para listar la entidad. El método nos devuelve una Lista, con lo que
        // podemos asignarla directamente a la lista de clientes.
        try {
            // Comprobamos que la lista no esté vacía
            if (!repositorio.isEmpty()) {
                return repositorio.findAll().cloneOf();
            }
        } catch (NullPointerException e) {
            System.out.println("Error al listar los clientes.");
            e.printStackTrace();
        }

        // Si falla, devolvemos una lista vacía
        return new Listas<>();*/
    }

    // Producto 3 --> Método para obtener un cliente de la BD a través de su id.
    public Optional<Cliente> getClienteById(Long id) {
        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        return !service.isEmpty() ? service.findById(id) : Optional.empty();

        /*// Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un cliente.
        try {
            return repositorio.findById(id);
        } catch (NullPointerException e) {
            System.out.println("Error al buscar el cliente por ID.");
            e.printStackTrace();
        }

        return null;*/
    }

    public ArrayList<Cliente> filterClientesByType(@NotNull String tipo) {
        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);

        // Creamos el tipo de cliente según el tipo recibido por parámetro.
        switch (tipo) {
            case "Estandard" -> tipo = "ClienteEstandard";
            case "Premium" -> tipo = "ClientePremium";
        }

        // Creamos una lista temporal
        ArrayList<Cliente> clientesTemp = new ArrayList<>();

        // Comprobamos que la lista contenga elementos
        if (!service.isEmpty()) {

            // Recorremos cada elemento de la lista y, si es del tipo que buscamos, lo
            // añadimos a la lista temporal.
            String finalTipo = tipo;
            // Obtenemos y recorremos la lista de clientes
            service.findAll().getLista().stream()
                    // Filtramos los clientes por tipo.
                    .filter(cliente -> IClienteFactory.tipoCliente(cliente).equals(finalTipo))
                    // Añadimos los clientes filtrados a la lista temporal
                    .forEach(clientesTemp::add);
        }

        // Devolvemos la lista temporal, contendrá los clientes filtrados por tipo o estará vacía si no ha encontrado ninguno.
        return clientesTemp;

        /*// Producto 3 --> Obtenemos una lista de clientes de la BD filtrada por tipo.
        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Creamos el tipo de cliente según el tipo recibido por parámetro.
        switch (tipo) {
            case "Estandard" -> tipo = "ClienteEstandard";
            case "Premium" -> tipo = "ClientePremium";
        }

        // Creamos una lista temporal
        ArrayList<Cliente> clientesTemp = new ArrayList<>();

        // Comprobamos que la lista contenga elementos
        if (!repositorio.isEmpty()) {

            // Recorremos cada elemento de la lista y, si es del tipo que buscamos, lo
            // añadimos a la lista temporal.
            try {
                String finalTipo = tipo;
                repositorio.findAll().getLista().stream()
                            .filter(cliente -> IClienteFactory.tipoCliente(cliente).equals(finalTipo))
                            .forEach(clientesTemp::add);

            } catch (IndexOutOfBoundsException e) {
                // Si se produce una excepción, la lista temporal quedará vacía
                System.out.println("Error al filtrar los clientes por tipo");
                e.printStackTrace();
            }
        }

        // Devolvemos la lista temporal con los clientes del tipo buscado, si lo hay.
        return clientesTemp;*/
    }

    // Buscamos un cliente por su DNI
    // Producto 3 --> Refactorizamos el método para realizar la búsqueda en la BD.
    public Optional<Cliente> searchCliente(@NotNull String nif) {
        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);

        // Comprobamos que la lista contenga elementos y ejecutamos el método para buscar el cliente por NIF.
        return !service.isEmpty() ? service.findOne(nif) : Optional.empty();

        /*// Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // Comprobamos que la tabla contenga elementos.
        if (!repositorio.isEmpty()) {
            // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un cliente.
            try {
                return repositorio.findOne(nif);
            } catch (NullPointerException e) {
                System.out.println(MessageFormat.format("Error al buscar el cliente con NIF {0}", nif));
                e.printStackTrace();
            }
        }

        // Si el proceso falla o el cliente no existe, devolvemos null.
        return null;*/
    }

    // Limpiamos la lista de clientes
    public ArrayList<Cliente> clearClientes() {
        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);
        DireccionService direccionService = new DireccionServiceImpl(this.em);

        // Creamos una lista temporal que devolveremos en caso de éxito.
        ArrayList<Cliente> clientesTemp = listClientes().getLista();

        // Creamos un flag de control.
        AtomicBoolean flag = new AtomicBoolean(false);

        // Ejecutamos el método para eliminar todos los elementos de la tabla.
        if (!service.isEmpty()) {
            try {
                // Obtenemos y recorremos la lista de clientes eliminando cada uno de ellos, Hibernate se
                // encargará de eliminar también la dirección.
                listClientes().forEach(cliente -> flag.set(service.delete(cliente.getId())));
                // Comprobamos que se haya ejecutado la limpieza
                if (flag.get()) {
                    // Reseteamos el contador de clientes
                    Cliente.resetTotalClientes();

                    // Reseteamos el contador de la clave primaria en la BD.
                    service.resetId();
                    direccionService.resetId();
                }
            } catch (NullPointerException e) {
                System.out.println("Error al limpiar la lista de clientes.");
                e.printStackTrace();
            }
        }

        // En caso de producirse un error o no haber podido ejecutar la limpieza devolvemos una lista vacía
        return flag.get() ? clientesTemp : new ArrayList<>();

        /*// Creamos una lista temporal que devolveremos en caso de éxito.
        // Producto 3 --> Obtenemos los clientes directamente de la BD y la limpiamos en la BD.
        ArrayList<Cliente> clientesTemp = listClientes().getLista();

        // Creamos un flag de control.
        AtomicBoolean flag = new AtomicBoolean(false);

        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para eliminar todos los elementos de la tabla.
        if (!repositorio.isEmpty()) {
            try {
                listClientes().forEach(cliente -> flag.set(repositorio.delete(cliente.getId())));
                // Comprobamos que se haya ejecutado la limpieza
                if (flag.get()) {
                    // Reseteamos el contador de clientes
                    Cliente.resetTotalClientes();

                    // Reseteamos el contador de la clave primaria en la BD.
                    repositorio.resetId();

                    // Limpiamos también la tabla direcciones.
                    // La acción devuelve un boolean, así que podemos usar el flag para controlar el resultado.
                    DireccionRepositorioImpl repositorioDireccion = new DireccionRepositorioImpl();
                    if (!repositorioDireccion.deleteAll()) {
                        System.out.println("Error al limpiar la tabla de direcciones");
                        flag.set(false);
                    }

                    // Reseteamo el contador de la clave primaria en la tabla direcciones
                    repositorioDireccion.resetId();
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error al limpiar la lista de clientes");
                e.printStackTrace();
            }
        }

        // En caso de producirse un error o no haber podido ejecutar la limpieza devolvemos una lista vacía
        return flag.get() ? clientesTemp : new ArrayList<>();*/
    }

    // Refactorizado --> Eliminamos un método que no usamos
    /*public Cliente getCliente(@NotNull Cliente cliente) {

        // Producto 3 --> Obtenemos el cliente de la BD a través de su id.

        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);


        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un cliente.
        try {
            return repositorio.findById(cliente.getId());
        } catch (NullPointerException e) {
            System.out.println("Error al obtener el cliente");
            e.printStackTrace();
        }

        // Si falla, devolvemos null
        return null;
    }*/

    public Cliente updateCliente(@NotNull Cliente cliente) {
        // Producto 3 --> Actualizamos el cliente en la BD.
        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);

        // Comprobamos que existan clientes en la BD.
        if (!service.isEmpty()) {
            return service.save(cliente)? cliente : null;
        }

        /*// Producto 3 --> Actualizamos el cliente en la BD.
        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para actualizar el cliente en la BD.
        if (!repositorio.isEmpty()) {
            try {
                return repositorio.save(cliente)? cliente : null;
            } catch (NullPointerException e) {
                System.out.println("Error al actualizar el cliente");
                e.printStackTrace();
            }
        }*/

        // Si falla, devolvemos null
        return null;
    }

    public Cliente deleteCliente(@NotNull Cliente cliente) {

        // Producto 3 --> Eliminamos el cliente de la BD.
        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);

        // Comprobamos que existan clientes en la BD y ejecutamos el método para eliminar el cliente de la BD.
        if (service.isEmpty()) {
            if (service.delete(cliente.getId())) {
                // Decrementamos el contador de clientes
                Cliente.decreaseTotalClientes();

                // Devolvemos el cliente eliminado
                return cliente;
            }
        }

        // Si falla, devolvemos null
        return null;

        /*// Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para eliminar el cliente de la BD.
        if (!repositorio.isEmpty()) {
            try {
                if (repositorio.delete(cliente.getId())) {
                    // Decrementamos el contador de clientes
                    Cliente.decreaseTotalClientes();

                    // Devolvemos el cliente eliminado
                    return cliente;
                }
            } catch (NullPointerException e) {
                System.out.println("Error al eliminar el cliente");
                e.printStackTrace();
            }
        }

        // Si falla, devolvemos null
        return null;*/
    }

    /* Pedidos */
    // Creamos un pedido recibiendo los parámetros necesarios
    // Producto 3 -> Refactorizando para actuar sobre la BD.
    public Pedido createPedido(@NotNull Cliente cliente, @NotNull Articulo articulo, int cantidad) {
        // Producto 4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);
        ClienteService clienteService = new ClienteServiceImpl(this.em);

        // Creamos el pedido
        Pedido pedido = new Pedido(cliente, articulo, cantidad);

        // Asignamos un número de pedido a través del último número de pedido + 1.
        // Obtenemos el último pedido de la BD que es un Optional, con lo que podemos usar el método ifPresentOrElse.
        service.getLast().ifPresentOrElse(p -> pedido.setNumeroPedido(p.getNumeroPedido() + 1),
                // Tenemos que crear una expresión lambda vacía para que funcione el método ifPresentOrElse.
                () -> pedido.setNumeroPedido(1));

        // Nos aseguramos de que enviado sea false:
        pedido.setEsEnviado(false);

        // Ejecutamos el método para crear el pedido en la BD, si se añade correctamente devolvemos el pedido.
        // Comprobamos que el pedido no exista en la BD, si no existe lo añadimos.
        if (service.findOne(String.valueOf(pedido.getNumeroPedido())).isEmpty()) {
            System.out.println("El pedido no existe, se va a crear." + pedido.getCliente());
            if (service.save(pedido)) {
                // Avalamos el contador de pedidos
                Pedido.avanzarTotalPedidos();

                // Devolvemos el pedido con Id y número de pedido
                return service.getLast().orElse(null);
            }
        }

        // Si ha habido un error, devolvemos un mensaje de error y null
        System.out.println(MessageFormat.format("El pedido con número {0} ya existe o ha habido un error al crearlo.",
                pedido.getNumeroPedido()));
        return null;

        /*// Producto 3 -> Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Asignamos un número de pedido a través del último número de pedido + 1.
        pedido.setNumeroPedido(repositorio.isEmpty()? 1 : repositorio.getLast().getNumeroPedido() + 1);

        if (repositorio.isEmpty()) {
            pedido.setNumeroPedido(1);
        } else {
            pedido.setNumeroPedido(repositorio.getLast().getNumeroPedido() + 1);
        }

        // Ejecutamos el método para crear el pedido en la BD, si se añade correctamente devolvemos el pedido.
        try {
            // Comprobamos que el pedido sea nuevo, si no lo es devolvemos null.
            if (repositorio.findOne(String.valueOf(pedido.getNumeroPedido())) != null) {
                System.out.println(MessageFormat.format("El pedido con número {0} ya existe.",
                        pedido.getNumeroPedido()));
                return null;
            }

            if (repositorio.save(pedido)) {
                // Incrementamos el contador de pedidos
                Pedido.avanzarTotalPedidos();

                // Devolvemos el pedido con ID
                return repositorio.getLast();
            }
        } catch (NullPointerException e) {
            System.out.println("Error al crear el pedido");
            e.printStackTrace();
        }

        // Si el proceso falla o el pedido no se añade correctamente, devolvemos null.
        return null;*/
    }

    // Añadimos un pedido recibiendo la instancia del mismo
    // Producto 3 -> Refactorizando para actuar sobre la BD.
    public Pedido createPedido(@NotNull Pedido pedido) {
        // Producto 4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Asignamos un número de pedido a través del último número de pedido + 1.
        // Obtenemos el último pedido de la BD que es un Optional, con lo que podemos usar el método ifPresentOrElse.
        service.getLast().ifPresentOrElse(p -> pedido.setNumeroPedido(p.getNumeroPedido() + 1),
                // Tenemos que crear una expresión lambda vacía para que funcione el método ifPresentOrElse.
                () -> pedido.setNumeroPedido(1));

        // Nos aseguramos de que enviado sea false:
        pedido.setEsEnviado(false);

        // Ejecutamos el método para crear el pedido en la BD, si se añade correctamente devolvemos el pedido.
        // Comprobamos que el pedido no exista en la BD, si no existe lo añadimos.
        if (service.findOne(String.valueOf(pedido.getNumeroPedido())).isEmpty()) {
            if (service.save(pedido)) {
                // Avalamos el contador de pedidos
                Pedido.avanzarTotalPedidos();

                // Devolvemos el pedido con Id y número de pedido
                return service.getLast().orElse(null);
            }
        }

        // Si ha habido un error, devolvemos un mensaje de error y null
        System.out.println(MessageFormat.format("El pedido con número {0} ya existe o ha habido un error al crearlo.",
                pedido.getNumeroPedido()));
        return null;


       /* // Creamos el repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Asignamos un número de pedido a través del último número de pedido + 1.
        pedido.setNumeroPedido(repositorio.isEmpty()? 1 : repositorio.getLast().getNumeroPedido() + 1);

        // Nos aseguramos de que enviado sea false:
        pedido.setEsEnviado(false);

        // Ejecutamos el método para crear el pedido en la BD, si se añade correctamente devolvemos el pedido.
        try {
            // Comprobamos que el pedido sea nuevo, si no lo es devolvemos null.
            if (searchPedido(pedido.getNumeroPedido()) != null) {
                System.out.println(MessageFormat.format("El pedido con número {0} ya existe.",
                        pedido.getNumeroPedido()));
                return null;
            }

            if (repositorio.save(pedido)) {

                // Incrementamos el contador de pedidos
                Pedido.avanzarTotalPedidos();

                // Devolvemos el pedido con ID
                return repositorio.getLast();
            }
        } catch (NullPointerException e) {
            System.out.println("Error al crear el pedido.");
            e.printStackTrace();
        }

        return null;*/
    }

    // Devolvemos una lista con todos los elementos de la lista de pedidos, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Pedido> listPedidos() {

        // Producto 3 -> Obtenemos la lista de pedidos a través de la BD.
        // Producto 4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Comprobamos que la lista no esté vacía, si hay elementos devolvemos la lista de pedidos, si no, devolvemos
        // una lista vacía.
        return !service.isEmpty() ? service.findAll().cloneOf() : new Listas<>();


        /*// Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Ejecutamos el método para listar la entidad. El método nos devuelve una Lista, con lo que
        // podemos asignarla directamente a la lista de clientes.
        if (!repositorio.isEmpty()) {
            try {
                return repositorio.findAll().cloneOf();
            } catch (NullPointerException e) {
                System.out.println("Error al listar los pedidos.");
                e.printStackTrace();
            }
        }

        // Si falla, devolvemos una lista vacía
        return new Listas<>();*/
    }

    // Producto 3 --> Creamos un método para buscar un pedido por su ID.
    public Optional<Pedido> getPedidoById(Long id) {
        // Producto 3 --> Obtenemos el pedido a través de la BD.
        // Producto 4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // El método nos devuelve un objeto Optional, con lo que podemos asignarlo directamente a un cliente.
        return !service.isEmpty() ? service.findById(id) : Optional.empty();


//        // Creamos un objeto Repositorio para la entidad Pedido.
//        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();
//
//        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
//        // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un cliente.
//        try {
//            return repositorio.findById(id);
//        } catch (NullPointerException e) {
//            System.out.println("Error al buscar el pedido por ID.");
//            e.printStackTrace();
//        }
//
//        // Si falla, devolvemos null
//        return null;
    }

    // Eliminamos un pedido
    public Pedido deletePedido(@NotNull Pedido pedido) {

        // Producto 3 --> Eliminamos el pedido de la BD.
        // Producto 4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Por seguridad, actualizamos y comprobamos que el pedido no está enviado.
        actualizarEstadoPedido(pedido);

        // Guardamos los cambios en la BD
        service.save(pedido);

        // Comprobamos si el pedido está enviado
        if (!pedido.pedidoEnviado() && !service.isEmpty()) {
            // Ejecutamos el método para eliminar el pedido de la BD.
            if (service.delete(pedido.getId())) {

                // Decrementamos el contador de pedidos
                Pedido.decrementarTotalPedidos();

                // Devolvemos el pedido eliminado.
                return pedido;
            }
        }
        // Si el pedido está enviado, mostramos un mensaje de error.
        System.out.println("=== ERROR ===");
        System.out.println("El pedido ya está enviado, no se puede eliminar.");
        System.out.println("==============");

        // Si ha habido un error, devolvemos null
        return null;

        /*// Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Por seguridad, actualizamos y comprobamos que el pedido no está enviado.
        actualizarEstadoPedido(pedido);

        // Comprobamos si el pedido está enviado
        if (!pedido.pedidoEnviado() && !repositorio.isEmpty()) {
            try {
                Pedido deletedPedido = pedido;
                // Ejecutamos el método para eliminar el pedido de la BD.
                if (repositorio.delete(pedido.getId())) {

                    // Decrementamos el contador de pedidos
                    Pedido.decrementarTotalPedidos();

                    // Devolvemos el pedido eliminado.
                    return pedido;
                }
                System.out.println("El pedido no puede ser eliminado porque ya está enviado!");
            } catch (NullPointerException e) {
                // Si se produce un error, devolvemos null
                System.out.println("Error al eliminar el pedido.");
                e.printStackTrace();
            }
        } else {
            // Si el pedido está enviado, mostramos un mensaje de error.
            System.out.println("=== ERROR ===");
            System.out.println("El pedido ya está enviado, no se puede eliminar.");
            System.out.println("==============");
        }

        // Si el proceso falla o el pedido no existe o ya está enviado, devolvemos null.
        return null;*/
    }

    // Buscamos un pedido por su número de pedido
    public Optional<Pedido> searchPedido(int numeroPedido) {

        // Producto 3 --> Buscamos el pedido en la BD.
        // Producto 4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        return !service.isEmpty() ? service.findOne(String.valueOf(numeroPedido)) : Optional.empty();


       /* // Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un cliente.
        if (!repositorio.isEmpty()) {
            try {
                return repositorio.findOne(String.valueOf(numeroPedido));
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                System.out.println("Error al buscar el pedido.");
                e.printStackTrace();
            }
        }

        // Si el proceso falla o el pedido no existe, devolvemos null.
        return null;*/
    }

    // Filtramos los pedidos según el cliente a través de su nif
    public ArrayList<Pedido> filterPedidosByCliente(@NotNull String nif) {
        // Producto 3 --> Filtramos los pedidos de la BD.
        // Producto 4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Creamos una lista temporal donde almacenar los pedidos filtrados.
        ArrayList<Pedido> pedidosTemp = new ArrayList<>();

        // Comprobamos que la lista contenga elementos
        if (!service.isEmpty()) {
            try {
                // Obtenemos la lista de pedidos de la BD y la iteramos, usamos programación funcional
                // así que explicaremos el método paso a paso.
                // Primero obtenemos la lista de la base de datos y la convertimos en un stream.
                service.findAll().getLista().stream()
                        // Filtramos los pedidos que coincidan con el nif recibido, mapeando cada uno de los
                        // pedidos a su cliente.
                        .filter(pedido -> pedido.getCliente().getNif().equals(nif))
                        // Finalizamos iterando sobre la lista y añadiendo los pedidos a la lista temporal.
                        .forEach(pedidosTemp::add);
            } catch (NullPointerException e) {
                System.out.println("Error al filtrar los pedidos por cliente.");
                e.printStackTrace();
            }
        }

        /*// Producto 3 --> Filtramos los pedidos de la BD.
        // Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Comprobamos que la lista contenga elementos
        if (!repositorio.isEmpty()) {
            try {

                // Obtenemos la lista de pedidos de la BD y la iteramos, usamos programación funcional
                // así que explicaremos el método paso a paso.
                // Primero obtenemos la lista de la base de datos y la convertimos en un stream.
                repositorio.findAll().getLista().stream()
                        // Filtramos los pedidos que coincidan con el nif recibido, mapeando cada uno de los
                        // pedidos a su cliente.
                        .filter(pedido -> pedido.getCliente().getNif().equals(nif))
                        // Finalizamos iterando sobre la lista y añadiendo los pedidos a la lista temporal.
                        .forEach(pedidosTemp::add);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error al filtrar los pedidos por cliente.");
                e.printStackTrace();
            }
        }*/

        // Devolvemos la lista temporal, si se ha producido un error o no existen pedidos para el nif devolvemos una lista vacía
        return pedidosTemp;
    }

    // Filtramos los pedidos según el cliente recibiendo una instancia de cliente
    public ArrayList<Pedido> filterPedidosByCliente(@NotNull Cliente cliente) {
        return this.filterPedidosByCliente(cliente.getNif());
    }

    // Filtramos los pedidos enviados y los ordenamos por fecha de envío
    public ArrayList<Pedido> filterPedidosByEstado(String opt) {

        // Por seguridad, pasamos la opción a minúsculas.
        opt = opt.toLowerCase();

        // Creamos una lista temporal
        ArrayList<Pedido> pedidosTemp = new ArrayList<>();

        // Producto 3 --> Filtramos los pedidos de la BD.
        // Producto 4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Comprobamos que haya elementos en la lista
        if (!service.isEmpty()) {
            try {
                // Actualizamos el estado de los pedidos
                actualizarEstadoPedidos();

                /*
                 * Usamos un switch para filtrar los pedidos según el estado recibido.
                 * En el caso de que el estado sea "enviado" filtramos los pedidos que estén enviados y los ordenamos
                 * por fecha de envío.
                 * En el caso de que el estado sea "pendiente" filtramos los pedidos que no estén enviados y los ordenamos
                 * por fecha de envío.
                 */
                switch (opt) {
                    // Obtenemos la lista de pedidos de la BD y la iteramos, usamos programación funcional
                    // así que explicaremos el método paso a paso.
                    // Primero obtenemos la lista de la base de datos y la convertimos en un stream.
                    case "enviado" -> service.findAll().getLista().stream()
                            // Filtramos los pedidos que estén enviados
                            .filter(Pedido::getEsEnviado)
                            // Ordenamos los pedidos por fecha de envío
                            .sorted(Comparator.comparing(Pedido::getFechaEnvio))
                            // Finalizamos iterando sobre la lista y añadiendo los pedidos a la lista temporal.
                            .forEach(pedidosTemp::add);
                    case "pendiente" -> service.findAll().getLista().stream()
                            .filter(pedido -> !pedido.getEsEnviado())
                            .sorted(Comparator.comparing(Pedido::getFechaEnvio))
                            .forEach(pedidosTemp::add);
                    default -> pedidosTemp.addAll(service.findAll().getLista());
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error al filtrar los pedidos.");
                e.printStackTrace();
            }
        }

        // Devolvemos la lista temporal, si se ha producido un error o no existen pedidos enviados devolvemos una lista vacía
        return pedidosTemp;
    }

    // Filtramos los pedidos enviados por su fecha de pedido
    public ArrayList<Pedido> filterPedidosEnviadosByFecha(@NotNull LocalDate fecha) {

        // Creamos una lista temporal
        ArrayList<Pedido> pedidosTemp = new ArrayList<>();

        // Producto 3 --> Filtramos los pedidos de la BD.
        // Producto 4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Comprobamos que haya elementos en la lista
        if (!service.isEmpty()) {
            try {
                // Actualizamos el estado de los pedidos
                actualizarEstadoPedidos();

                // Obtenemos la lista de pedidos de la BD y la iteramos, usamos programación funcional
                // así que explicaremos el método paso a paso.
                // Primero obtenemos la lista de la base de datos y la convertimos en un stream.
                service.findAll().getLista().stream()
                        // Filtramos los pedidos que estén enviados y que su fecha de pedido
                        // coincida con la fecha recibida
                        .filter(pedido -> pedido.getEsEnviado() && pedido.getFechaPedido().equals(fecha))
                        // Finalizamos iterando sobre la lista y añadiendo los pedidos a la lista temporal.
                        .forEach(pedidosTemp::add);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error al filtrar los pedidos por fecha");
                e.printStackTrace();
            }
        }

        // Devolvemos la lista temporal, si se ha producido un error o no existen pedidos enviados devolvemos una lista vacía
        return pedidosTemp;
    }

    // Limpiamos la lista de pedidos
    public ArrayList<Pedido> clearPedidos() {

        // Producto3 -> Implementamos el método para ejecutarse en la BD.
        // Producto4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Creamos una lista temporal
        ArrayList<Pedido> pedidosTemp = listPedidos().getLista();

        // Creamos un flag de control
        AtomicBoolean flag = new AtomicBoolean(false);

        // Iteramos por toda la tabla de la BD eliminando los pedidos.
        if (!service.isEmpty()) {
            try {
                listPedidos().forEach(pedido -> flag.set(service.delete(pedido.getId())));
                if (flag.get()) {
                    // Actualizamos el contador
                    Pedido.resetTotalPedidos();

                    // Actualizamos el contador de la BD
                    service.resetId();
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error al eliminar los pedidos de la BD");
                e.printStackTrace();
            }
        }

        // Comprobamos el flag, si se ha eliminado la lista de forma correcta devolvemos la lista temporal,
        // si no devolvemos una lista vacía.
        return flag.get() ? pedidosTemp : new ArrayList<>();
    }

    // Actualizamos el estado de los pedidos de la lista
    public int actualizarEstadoPedidos() {
        // Producto3 --> Implementamos el método para ejecutarse en la BD.
        // Producto4 ≥ Usamos los servicios
        PedidoService service = new PedidoServiceImpl(this.em);

        // Declaramos un contador que podamos usar en la función lambda
        AtomicInteger contPedidos = new AtomicInteger();
        // Inicializamos el contador
        contPedidos.set(0);

        // Obtendremos la lista de pedidos de forma asíncrona en la BD. Por cada pedido
        // obtenido comprobaremos si está enviado y, de ser así, lo actualizaremos en la BD.

        // Comprobamos que existan elementos en la lista.
        if (!service.isEmpty()) {
            try {
                // Obtenemos la lista de pedidos de la BD y usamos progración funcional
                // para filtrarla e iterarla.
                listPedidos().getLista().stream()
                        // Descartamos los pedidos que ya estén marcados como enviados
                        .filter(pedido -> !pedido.getEsEnviado())
                        // Filtramos los pedidos que estén enviados
                        .filter(Pedido::pedidoEnviado)
                        .forEach(pedido -> {
                            // Actualizamos el estado del pedido
                            pedido.setEsEnviado(true);

                            // Actualizamos el pedido en la BD.
                            service.save(pedido);

                            // Incrementamos el contador
                            contPedidos.getAndIncrement();
                        });
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error al actualizar el estado de los pedidos");
                e.printStackTrace();
            }
        }

        // Devolvemos el número de pedidos actualizados
        return contPedidos.get();
    }

    // Actualizamos el estado de un pedido
    public int actualizarEstadoPedido(@NotNull Pedido pedido) {

        // Comprobamos si el pedido está enviado
        if (!pedido.getEsEnviado()) {
            if (pedido.pedidoEnviado()) {
                pedido.setEsEnviado(true);

                // Devolvemos 1 si el pedido se ha actualizado
                return 1;
            }
        }

        // Devolvemos 0 si el pedido no se ha actualizado
        return 0;
    }

    // TODO -> Implementar resto de métodos CRUD para el trabajo con la bbdd

    // Prototipo de ticket de pedido para el test por consola
    public void printTicket(Pedido pedido) {
        System.out.println("Ticket de pedido");
        System.out.println("========================================");
        System.out.println(MessageFormat.format("Cliente tipo: {0}\tNombre: {1} Nif: {2}\nEmail: {3}\n",
                IClienteFactory.tipoCliente(pedido.getCliente()),
                pedido.getCliente().getNombre(),
                pedido.getCliente().getNif(),
                pedido.getCliente().getEmail()));
        System.out.println("========================================");
        System.out.println(MessageFormat.format("Artículo: {0}\tPrecio: {1}\tCantidad: {2}\tSubtotal: {3}\n",
                pedido.getArticulo().getCodArticulo(),
                pedido.getArticulo().getPvp(),
                pedido.getUnidades(),
                pedido.calcularSubtotal()));
        System.out.println("========================================");
        System.out.println("Gastos de envío: " + pedido.precioEnvio());
        System.out.println("========================================");
        System.out.println(MessageFormat.format("Total: {0}\tFecha de pedido: {1}\tFecha de envío: {2}\tEstado: {3}",
                pedido.precioTotal(),
                pedido.getFechaPedido(),
                pedido.calcularFechaEnvio(),
                pedido.getEsEnviado() ? "Enviado" : "Pendiente"));

    }

    // Actualizamos las listas.
    public int actualizarContadores() {
        // Producto3 --> Implementamos el método para ejecutarse en la BD.
        // Producto4 ≥ Usamos los servicios
        ArticuloService articuloService = new ArticuloServiceImpl(this.em);
        ClienteService clienteService = new ClienteServiceImpl(this.em);
        PedidoService pedidoService = new PedidoServiceImpl(this.em);

        try {
            Articulo.setTotalArticulos(articuloService.count());
            Cliente.setTotalClientes(clienteService.count());
            Pedido.setTotalPedidos(pedidoService.count());

            // Devolvemos 1 si se han actualizado los contadores
            return 1;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error al actualizar los contadores");
            e.printStackTrace();
        }

        // Si hay algún error devolvemos 0.
        return 0;
    }

    public int actualizarCodigos() {
        // Producto3 --> Implementamos el método para ejecutarse en la BD.
        // Producto4 ≥ Usamos los servicios
        ArticuloService articuloService = new ArticuloServiceImpl(this.em);
        ClienteService clienteService = new ClienteServiceImpl(this.em);

        // Usaremos una única variable temporal para almacenar los códigos.
        ArrayList<String> codigosTemp = new ArrayList<>();

        // Obtenemos los codigos de articulos.
        try {
            // Primero obtenemos la lista de todos los artículos de la BD.
            articuloService.findAll()
                    // Iteramos por la lista y añadimos los códigos a la lista temporal.
                    .forEach(articulo -> codigosTemp.add(articulo.getCodArticulo()));

            // Seteamos la nueva lista de códigos en la clase Articulo.
            Articulo.setCodigos(codigosTemp);

            // Limpiamos la lista temporal.
            codigosTemp.clear();

            // Repetimos el proceso para los clientes, pero en este caso hemos de filtrar
            // los clientes del tipo Premium.
            // Obtenemos la lista de clientes y la convertimos y la mapeamos.
            clienteService.findAll().getLista().stream()
                    // Filtramos los clientes del tipo Premium mediante el método instanceof.
                    .filter(cliente -> cliente instanceof ClientePremium)
                    // Iteramos por la lista y añadimos los códigos a la lista temporal.
                    .forEach(cliente -> codigosTemp.add(((ClientePremium) cliente).getCodSocio()));

            // Seteamos la nueva lista de códigos en la clase ClientePremium.
            ClientePremium.setCodigos(codigosTemp);

            // Limpiamos la lista temporal.
            codigosTemp.clear();

            // Devolvemos una sálida de éxito.
            return 1;

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error al actualizar los códigos de los artículos y clientes.");
            e.printStackTrace();

            // Devolvemos una salida de error.
            return 0;
        }
    }

    /* Métodos de comprobación de existencia de artículos y clientes en la BD. */
    // Comprobamos si ya existe un artículo en la BD.
    public boolean checkArticulo(Articulo src) {
        // Producto 4 ≥ Usamos los servicios
        ArticuloService service = new ArticuloServiceImpl(this.em);

        assert Objects.requireNonNull(src).getDescripcion() != null;
        // Comprobamos si existe el artículo en la BD.
        try {
            return service.findAll().getLista().stream()
                    .anyMatch(articulo -> articulo.getDescripcion().equals(src.getDescripcion()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error al comprobar el artículo en la BD.");
            e.printStackTrace();
            return false;
        }
    }

    // Comprobamos si ya existe un cliente en la BD.
    public boolean checkCliente(Cliente src) {
        // Producto 4 ≥ Usamos los servicios
        ClienteService service = new ClienteServiceImpl(this.em);

        assert Objects.requireNonNull(src).getNif() != null;
        // Comprobamos si existe el cliente en la BD.
        try {
            return service.findAll().getLista().stream()
                    .anyMatch(cliente -> cliente.getNif().equals(src.getNif()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error al comprobar el cliente en la BD.");
            e.printStackTrace();
            return false;
        }
    }

    // Creamos un proceso para cargar los datos de test en la BD.
    public int loadTestData() {
        // Producto 4 ≥ Usamos los servicios
        LoadDataService service = new LoadDataServiceImpl(this.em);

        // Por seguridad, volvemos a comprobar si la BD está vacía.
        if (service.checkData()) {
            return 0;
        }

        // Ejecutamos el método para cargar los datos de test.
        return service.loadData();

        /*// Creamos el repositorio para ejecutar acciones sobre la BD.
        LoadDataRepositorio repositorio = new LoadDataImpl();

        // Por seguridad, volvemos a comprobar si la BD está vacía.
        if (repositorio.checkData()) {
            return 0;
        }

        // Ejecutamos el método para cargar los datos de test.
        try {
            return repositorio.loadData();
        } catch (Exception e) {
            System.out.println("Error al cargar los datos de test en la BD.");
            e.printStackTrace();
            return -1;
        }*/
    }

    // Comprobamos de forma independiente si la BD está vacía.
    public boolean checkData() {
        // Producto 4 ≥ Usamos los servicios
        LoadDataService service = new LoadDataServiceImpl(this.em);

        // Compobamos si la BD está vacía.
        return service.checkData();
    }
}
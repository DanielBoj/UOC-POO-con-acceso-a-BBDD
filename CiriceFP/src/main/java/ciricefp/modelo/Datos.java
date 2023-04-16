package ciricefp.modelo;

import ciricefp.controlador.Controlador;
import ciricefp.modelo.interfaces.ICliente;
import ciricefp.modelo.interfaces.factory.IClienteFactory;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.repositorio.*;
import ciricefp.modelo.utils.Conexion;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Objects;
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
    private Conexion baseDatos;

    // Constructor por defecto --> Inicializa las listas de clientes, artículos y pedidos.
    public Datos() {
    }

    // Constructor con parámetros --> Inicializa las listas de clientes, artículos y pedidos.
    public Datos(Controlador controlador, Conexion baseDatos) {
        this.controlador = controlador;
        this.baseDatos = baseDatos;
    }

    // Producto 3 -> Al implementar el acceso a la BD ya no necesitamos este constructor.
    // Constructor con parámetros --> Inicializa las listas de clientes, artículos y pedidos.

    /* Getters & Setters */
    public Controlador getControlador() {
        return controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public Conexion getBaseDatos() {
        return baseDatos;
    }

    public void setBaseDatos(Conexion baseDatos) {
        this.baseDatos = baseDatos;
    }

    // StringBuilder nos permite implementar un patrón de diseño de string para el método toString() de una forma visual muy clara.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Controlador interno del Modelo:\n");
        sb.append(controlador).append("||").append(baseDatos);

        return sb.toString();
    }

    /* Métodos de la clase */
    /* Articulos */
    // Añadimos un artículo a la bd instanciándolo con los parámetros recibidos.
    public Articulo createArticulo(String descripcion, double precio, double gastosEnvio, int preparacion) {

        // Creamos el objeto artículo.
        Articulo articulo = new Articulo(descripcion, precio, gastosEnvio, preparacion);

        // Producto 3 -> Manejamos la creación de un artículo a través del Repositorio.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Intentamos añadir el artículo a la BD.
        // Comprobamos si el artículo ya existe
        if (!checkArticulo(articulo)) {
            // Añadimos el artículo a la BD
            try {
                if (repositorio.save(articulo)) {
                    // Aumentamos el número de artículos
                    Articulo.avanzarContador();

                    // Recuperamos el último artículo creado.
                    return repositorio.getLast();
                }
            } catch (NullPointerException e) {
                System.out.println("Error al crear el artículo.");
                e.printStackTrace();
            }
        } else {
            System.out.println(MessageFormat.format("El artículo {0} ya existe", articulo.getDescripcion()));
        }

        // En caso de error, devolvemos null
        return null;
    }

    // Añadimos un artículo a la bd recibiéndolo por parámetro
    public Articulo createArticulo(@NotNull Articulo articulo) {

        // Producto 3 -> Manejamos la creación de un artículo a través del Repositorio.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Intentamos añadir el artículo a la BD.
        // Comprobamos si el artículo ya existe
        if (!checkArticulo(articulo)) {
            // Añadimos el artículo a la BD
            try {
                if (repositorio.save(articulo)) {
                    // Aumentamos el número de artículos
                    Articulo.avanzarContador();

                    // Devolvemos el último artículo creado.
                    return repositorio.getLast();
                }
            } catch (NullPointerException e) {
                System.out.println("Error al crear el artículo.");
                e.printStackTrace();
            }
        } else {
            System.out.println(MessageFormat.format("El artículo {0} ya existe", articulo.getDescripcion()));
        }

        // En caso de error, devolvemos null
        return null;
    }

    // Devolvemos una lista con todos los elementos de la lista de artículos, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Articulo> listArticulos() {

        // Producto 3 -> Obtenemos la lista de productos de la BD a través del Repositorio.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Ejecutamos el método para listar la entidad. El método nos devuelve una Lista, con lo que
        // podemos asignarla directamente a la lista de artículos.
        try {
            // Comprobamos que la lista contenga elementos
            if (!repositorio.isEmpty()) {
                // Intentamos devolver una copia de la lista
                return repositorio.findAll().cloneOf();
            }
        } catch (NullPointerException e) {
            System.out.println("Error al listar los artículos.");
            e.printStackTrace();
        }

        // Si falla, devolvemos una lista vacía
        return new Listas<>();
    }

    // Producto 3 --> Método para obtener un artículo de la BD a través de su id.
    public Articulo getArticuloById(Long id) {

        // Creamos un objeto Repositorio para la entidad Articulo.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un artículo.
        try {
            return repositorio.findById(id);
        } catch (NullPointerException e) {
            System.out.println("Error al buscar el artículo por ID.");
            e.printStackTrace();
        }

        // En caso de error, devolvemos null
        return null;
    }

    // Producto 3 --> Cambiamos el método para obtener un artículo de la BD a través de su código.
    public Articulo searchArticulo(@NotNull String codigo) {

        // Creamos un objeto para recibir la instancia.
        Articulo articulo = null;

        // Creamos un objeto Repositorio para la entidad Articulo.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Comprobamos que haya artículos en la BD.
        if (!repositorio.isEmpty()) {
            // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
            // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un artículo.
            try {
                return repositorio.findOne(codigo);
            } catch (NullPointerException e) {
                System.out.println("Error al buscar el artículo.");
                e.printStackTrace();
            }
        }

        return null;
    }

    // TODO -> Como extra podemos crear métodos para eliminar y modificar artículos

    // Eliminamos todos los elementos de la lista de artículos y devolvemos una lista con los elementos eliminados
    public ArrayList<Articulo> clearArticulos() {

        // Creamos una lista temporal quer devolveremos en caso de éxito
        ArrayList<Articulo> articulosTemp = listArticulos().getLista();

        // Flag de control
        AtomicBoolean flag = new AtomicBoolean(false);

        // Producto 3 --> Limpiamos la lista de artículos de la BD.
        // Creamos un objeto Repositorio para la entidad Articulo.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Ejecutamos el método para eliminar todos los elementos de la entidad.
        // Vamos a usar una función lambda para simplificar el código.
        try {
            listArticulos().forEach(articulo -> flag.set(repositorio.delete(articulo.getId())));
            if (flag.get()) {
                // Reseteamos el contador de artículos
                Articulo.resetContador();

                // Reseteamis el contador id en la DB.
                repositorio.resetId();
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
    public Cliente createCliente(String nombre, Direccion domicilio, String nif, String email, String tipo) {

        // Producto 3 -> Manejamos la creación de un cliente a través de la BD.

        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Añadimos el cliente a la BD, si se añade correctamente, devolvemos el Cliente.
        // Aplicamos la lógica de negocio para crear el cliente, no pueden haber dos clientes con el mismo
        // nif.
        try {
            // Producto 3 -> Mediante Factory podemos crear el cliente directamente.
            Cliente cliente = (Cliente) IClienteFactory.createCliente(nombre, domicilio, nif, email, tipo);

            // Comprobamos que el cliente no exista

            if (checkCliente(cliente)) {
                System.out.println(MessageFormat.format("El cliente {0} ya existe", cliente.getNombre()));
                return null;
            }

            if (repositorio.save(cliente)) {
                // Avanzamos el contador de clientes
                Cliente.advanceTotalClientes();
                // Devolvemos el cliente
                return repositorio.getLast();
            }
        } catch (NullPointerException e) {
            System.out.println("Error al crear el cliente");
            e.printStackTrace();
        }

        return null;
    }

    // Añadimos un cliente recibiendo la instancia del mismo
    public Cliente createCliente(@NotNull Cliente cliente) {

        // Producto 3 --> Manejamos la creación de un cliente a través de la BD.
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

        return null;
    }

    // Devolvemos una lista con todos los elementos de la lista de clientes, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Cliente> listClientes() {

        // Producto 3 -> Obtenemos la lista de clientes de la BD a través del Repositorio.
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
        return new Listas<>();
    }

    // Producto 3 --> Método para obtener un cliente de la BD a través de su id.
    public Cliente getClienteById(Long id) {

        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un cliente.
        try {
            return repositorio.findById(id);
        } catch (NullPointerException e) {
            System.out.println("Error al buscar el cliente por ID.");
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Cliente> filterClientesByType(@NotNull String tipo) {

        // Producto 3 --> Obtenemos una lista de clientes de la BD filtrada por tipo.
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
        return clientesTemp;
    }

    // Buscamos un cliente por su DNI
    // Producto 3 --> Refactorizamos el método para realizar la búsqueda en la BD.
    public Cliente searchCliente(@NotNull String nif) {

        // Creamos un objeto Repositorio para la entidad Cliente.
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
        return null;
    }

    // Limpiamos la lista de clientes
    public ArrayList<Cliente> clearClientes() {

        // Creamos una lista temporal que devolveremos en caso de éxito.
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
        return flag.get() ? clientesTemp : new ArrayList<>();
    }

    // TODO -> Como extra podemos crear métodos para eliminar y modificar clientes, el método getCliente será útil para la futura interacción con la BBDD
    public Cliente getCliente(@NotNull Cliente cliente) {

        // Producto 3 --> Obtenemos el cliente de la BD a través de su id.
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
    }

    public Cliente updateCliente(Cliente cliente) {

        // Producto 3 --> Actualizamos el cliente en la BD.
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
        }

        // Si falla, devolvemos null
        return null;
    }

    public Cliente deleteCliente(@NotNull Cliente cliente) {

        // Producto 3 --> Eliminamos el cliente de la BD.
        // Creamos un objeto Repositorio para la entidad Cliente.
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
        return null;
    }

    /* Pedidos */
    // Creamos un pedido recibiendo los parámetros necesarios
    // Producto 3 -> Refactorizando para actuar sobre la BD.
    public Pedido createPedido(@NotNull Cliente cliente, @NotNull Articulo articulo, int cantidad) {

        // Creamos el pedido
        Pedido pedido = new Pedido(cliente, articulo, cantidad);

        // Producto 3 -> Creamos un objeto Repositorio para la entidad Pedido.
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
        return null;
    }

    // Añadimos un pedido recibiendo la instancia del mismo
    // Producto 3 -> Refactorizando para actuar sobre la BD.
    public Pedido createPedido(@NotNull Pedido pedido) {

        // Creamos el repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Asignamos un número de pedido a través del último número de pedido + 1.
        pedido.setNumeroPedido(repositorio.isEmpty()? 1 : repositorio.getLast().getNumeroPedido() + 1);

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

        return null;
    }

    // Devolvemos una lista con todos los elementos de la lista de pedidos, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Pedido> listPedidos() {

        // Producto 3 -> Obtenemos la lista de pedidos a través de la BD.
        // Creamos un objeto Repositorio para la entidad Pedido.
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
        return new Listas<>();
    }

    // Producto 3 --> Creamos un método para buscar un pedido por su ID.
    public Pedido getPedidoById(Long id) {

        // Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un cliente.
        try {
            return repositorio.findById(id);
        } catch (NullPointerException e) {
            System.out.println("Error al buscar el pedido por ID.");
            e.printStackTrace();
        }

        // Si falla, devolvemos null
        return null;
    }

    // Eliminamos un pedido
    public Pedido deletePedido(@NotNull Pedido pedido) {

        // Producto 3 --> Eliminamos el pedido de la BD.
        // Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Por seguridad, actualizamos y comprobamos que el pedido no está enviado.
        actualizarEstadoPedido(pedido);

        // Comprobamos si el pedido está enviado
        if (!pedido.pedidoEnviado() && !repositorio.isEmpty()) {
            try {
                // Ejecutamos el método para eliminar el pedido de la BD.
                if (repositorio.delete(pedido.getId())) {

                    // Decrementamos el contador de pedidos
                    Pedido.decrementarTotalPedidos();

                    // Devolvemos el pedido eliminado.
                    return pedido;
                }
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
        return null;
    }

    // Buscamos un pedido por su número de pedido
    public Pedido searchPedido(int numeroPedido) {

        // Producto 3 --> Buscamos el pedido en la BD.
        // Creamos un objeto Repositorio para la entidad Pedido.
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
        return null;
    }

    // Filtramos los pedidos según el cliente a través de su nif
    public ArrayList<Pedido> filterPedidosByCliente(@NotNull String nif) {

        // Creamos una lista temporal donde almacenar los pedidos filtrados.
        ArrayList<Pedido> pedidosTemp = new ArrayList<>();

        // Producto 3 --> Filtramos los pedidos de la BD.
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
        }

        // Devolvemos la lista temporal, si se ha producido un error o no existen pedidos para el nif devolvemos una lista vacía
        return pedidosTemp;
    }

    // Filtramos los pedidos según el cliente recibiendo una instancia de cliente
    public ArrayList<Pedido> filterPedidosByCliente(@NotNull Cliente cliente) {
        return this.filterPedidosByCliente(cliente.getNif());
    }

    // Filtramos los pedidos enviados y los ordenamos por fecha de envío
    public ArrayList<Pedido> filterPedidosByEstado(String opt) {

        // Creamos una lista temporal
        ArrayList<Pedido> pedidosTemp = new ArrayList<>();

        // Producto 3 --> Filtramos los pedidos de la BD.
        // Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Comprobamos que haya elementos en la lista
        if (!repositorio.isEmpty()) {
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
                    case "enviado" -> repositorio.findAll().getLista().stream()
                            // Filtramos los pedidos que estén enviados
                            .filter(Pedido::getEsEnviado)
                            // Ordenamos los pedidos por fecha de envío
                            .sorted(Comparator.comparing(Pedido::getFechaEnvio))
                            // Finalizamos iterando sobre la lista y añadiendo los pedidos a la lista temporal.
                            .forEach(pedidosTemp::add);
                    case "pendiente" -> repositorio.findAll().getLista().stream()
                            .filter(pedido -> !pedido.getEsEnviado())
                            .sorted(Comparator.comparing(Pedido::getFechaEnvio))
                            .forEach(pedidosTemp::add);
                    default -> pedidosTemp.addAll(repositorio.findAll().getLista());
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
        // Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Comprobamos que haya elementos en la lista
        if (!repositorio.isEmpty()) {
            try {
                // Actualizamos el estado de los pedidos
                actualizarEstadoPedidos();

                // Obtenemos la lista de pedidos de la BD y la iteramos, usamos programación funcional
                // así que explicaremos el método paso a paso.
                // Primero obtenemos la lista de la base de datos y la convertimos en un stream.
                repositorio.findAll().getLista().stream()
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
        // Creamos una lista temporal
        ArrayList<Pedido> pedidosTemp = listPedidos().getLista();

        // Creamos un flag de control
        AtomicBoolean flag = new AtomicBoolean(false);

        // Creamos un Repositorio de Pedidos
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Iteramos por toda la tabla de la BD eliminando los pedidos.
        if (!repositorio.isEmpty()) {
            try {
                listPedidos().forEach(pedido -> flag.set(repositorio.delete(pedido.getId())));
                if (flag.get()) {
                    // Actualizamos el contador
                    Pedido.resetTotalPedidos();

                    // Actualizamos el contador de la BD
                    repositorio.resetId();
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

        // Creamos un repositorio para ejecutar acciones sobre la lista de pedidos de la BD.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Declaramos un contador que podamos usar en la función lambda
        AtomicInteger contPedidos = new AtomicInteger();

        // Obtendremos la lista de pedidos de forma asíncrona en la BD. Por cada pedido
        // obtenido comprobaremos si está enviado y, de ser así, lo actualizaremos en la BD.

        // Comprobamos que existan elementos en la lista.
        if (!repositorio.isEmpty()) {
            try {
                // Obtenemos la lista de pedidos de la BD y usamos progración funcional
                // para filtrarla e iterarla.
                listPedidos().getLista().stream()
                            // Filtramos los pedidos que estén enviados
                            .filter(Pedido::pedidoEnviado)
                            .forEach(pedido -> {
                                // Actualizamos el estado del pedido
                                pedido.setEsEnviado(true);

                                // Actualizamos el pedido en la BD.
                                repositorio.save(pedido);

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
        // Creamos los repositorios para consultar la BD.
        Repositorio<Articulo> repositorioArticulo = new ArticuloRepositorioImpl();
        Repositorio<Cliente> repositorioCliente = new ClienteRepositorioImpl();
        Repositorio<Pedido> repositorioPedido = new PedidoRepositorioImpl();

        try {
            Articulo.setTotalArticulos(repositorioArticulo.count());
            Cliente.setTotalClientes(repositorioCliente.count());
            Pedido.setTotalPedidos(repositorioPedido.count());

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
        // Usaremos una única variable temporal para almacenar los códigos.
        ArrayList<String> codigosTemp = new ArrayList<>();

        // Creamos los repositorios para consultar la BD.
        Repositorio<Articulo> repositorioArticulo = new ArticuloRepositorioImpl();
        Repositorio<Cliente> repositorioCliente = new ClienteRepositorioImpl();

        // Obtenemos los codigos de articulos.
        try {
            // Primero obtenemos la lista de todos los artículos de la BD.
            repositorioArticulo.findAll()
                    // Iteramos por la lista y añadimos los códigos a la lista temporal.
                    .forEach(articulo -> codigosTemp.add(articulo.getCodArticulo()));

            // Seteamos la nueva lista de códigos en la clase Articulo.
            Articulo.setCodigos(codigosTemp);

            // Repetimos el proceso para los clientes, pero en este caso hemso de filtrar
            // los clientes del tipo Premium.
            // Obtenemos la lista de clientes y la convertimos y la mapeamos.
            repositorioCliente.findAll().getLista().stream()
                    // Filtramos los clientes del tipo Premium mediante el método instanceof.
                    .filter(cliente -> cliente instanceof ClientePremium)
                    // Iteramos por la lista y añadimos los códigos a la lista temporal.
                    .forEach(cliente -> codigosTemp.add(((ClientePremium) cliente).getCodSocio()));

            // Seteamos la nueva lista de códigos en la clase ClientePremium.
            ClientePremium.setCodigos(codigosTemp);

            // Devolvemos una sálida de éxito.
            return 1;

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error al actualizar los códigos de los artículos y clientes.");
            e.printStackTrace();
        }

        // Devolvemos una salida de error.
        return 0;
    }

    /* Métodos de comprobación de existencia de artículos y clientes en la BD. */
    // Comprobamos si ya existe un artículo en la BD.
    public boolean checkArticulo(Articulo src) {
        // Creamos un repositorio para ejecutar acciones sobre la lista de artículos de la BD.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        assert Objects.requireNonNull(src).getDescripcion() != null;
        // Comprobamos si existe el artículo en la BD.
        try {
            return repositorio.findAll().getLista().stream()
                    .anyMatch(articulo -> articulo.getDescripcion().equals(src.getDescripcion()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error al comprobar el artículo en la BD.");
            e.printStackTrace();
        }

        return false;
    }

    // Comprobamos si ya existe un cliente en la BD.
    public boolean checkCliente(Cliente src) {
        // Creamos un repositorio para ejecutar acciones sobre la lista de clientes de la BD.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        assert Objects.requireNonNull(src).getNif() != null;
        // Comprobamos si existe el cliente en la BD.
        try {
            return repositorio.findAll().getLista().stream()
                    .anyMatch(cliente -> cliente.getNif().equals(src.getNif()));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error al comprobar el cliente en la BD.");
            e.printStackTrace();
        }

        return false;
    }
}
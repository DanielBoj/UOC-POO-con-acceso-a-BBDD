package ciricefp.modelo;

import ciricefp.controlador.Controlador;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.repositorio.ArticuloRepositorioImpl;
import ciricefp.modelo.repositorio.ClienteRepositorioImpl;
import ciricefp.modelo.repositorio.PedidoRepositorioImpl;
import ciricefp.modelo.repositorio.Repositorio;
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
    private Listas<Cliente> clientes;
    private Listas<Articulo> articulos;
    private Listas<Pedido> pedidos;
    private Conexion baseDatos;

    // Constructor por defecto --> Inicializa las listas de clientes, artículos y pedidos.
    public Datos() {
        this.clientes = new Listas<>();
        this.articulos = new Listas<>();
        this.pedidos = new Listas<>();
    }

    // Constructor con parámetros --> Inicializa las listas de clientes, artículos y pedidos.
    public Datos(Controlador controlador, Conexion baseDatos) {
        this.controlador = controlador;
        this.baseDatos = baseDatos;
        this.clientes = new Listas<>();
        this.articulos = new Listas<>();
        this.pedidos = new Listas<>();
    }

    // Constructor con parámetros --> Inicializa las listas de clientes, artículos y pedidos.
    public Datos(Controlador controlador,
                 Listas<Cliente> clientes,
                 Listas<Articulo> articulos,
                 Conexion baseDatos) {
        this.controlador = controlador;
        this.baseDatos = baseDatos;
        this.clientes = clientes;
        this.articulos = articulos;
        this.pedidos = new Listas<>();
    }

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

    public Listas<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Listas<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Listas<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(Listas<Articulo> articulos) {
        this.articulos = articulos;
    }

    public Listas<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Listas<Pedido> pedido) {
        this.pedidos = pedido;
    }

    // StringBuilder nos permite implementar un patrón de diseño de string para el método toString() de una forma visual muy clara.
    @Override
    public String toString() {
        return "Datos{" +
                "controlador=" + controlador +
                ", clientes=" + clientes +
                ", articulos=" + articulos +
                ", pedido=" + pedidos +
                ", baseDatos=" + baseDatos +
                '}';
    }

    /* Métodos de la clase */
    /* Articulos */

    // Añadimos un artículo a la lista instanciándolo con los parámetros recibidos.
    public Articulo createArticulo(String descripcion, double precio, double gastosEnvio, int preparacion) {

        // Creamos el objeto artículo.
        Articulo articulo = new Articulo(descripcion, precio, gastosEnvio, preparacion);

        // Producto 3 -> Manejamos la creación de un artículo a través del Repositorio.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Intentamos añadir el artículo a la BD.
        // Comprobamos si el artículo ya existe
        if (repositorio.findOne(articulo.getCodArticulo()) == null) {
            // Añadimos el artículo a la BD
            try {
                if (repositorio.save(articulo)) {
                    // Actualizamos la lista de artículos
                    this.articulos = listArticulos();

                    // Devolvemos el artículo
                    return this.articulos.get(this.articulos.sizeOf() - 1);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(MessageFormat.format("El artículo {0} ya existe", articulo.getDescripcion()));
        }

        return null;
    }

    // Añadimos un artículo a la lista recibiéndolo por parámetro
    public Articulo createArticulo(@NotNull Articulo articulo) {

        // Producto 3 -> Manejamos la creación de un artículo a través del Repositorio.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Intentamos añadir el artículo a la BD.
        // Comprobamos si el artículo ya existe
        if (repositorio.findOne(articulo.getCodArticulo()) == null) {
            // Añadimos el artículo a la BD
            try {
                if (repositorio.save(articulo)) {
                    // Actualizamos la lista de artículos
                    this.articulos = listArticulos();

                    // Devolvemos el artículo
                    return this.articulos.get(this.articulos.sizeOf() - 1);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(MessageFormat.format("El artículo {0} ya existe", articulo.getDescripcion()));
        }

        return null;
    }

    // Devolvemos una lista con todos los elementos de la lista de artículos, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Articulo> listArticulos() {

        // Producto 3 -> Obtenemos la lista de productos de la BD a través del Repositorio.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Ejecutamos el método para listar la entidad. El método nos devuelve una Lista, con lo que
        // podemos asignarla directamente a la lista de artículos.
        try {
            this.articulos = repositorio.findAll();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        // Comprobamos que la lista contenga elementos
        if (!this.articulos.isEmpty()) {

            // Intentamos devolver una copia de la lista
            try {
                return this.articulos.cloneOf();
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
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
            e.printStackTrace();
        }

        return null;
    }

    // Producto 3 --> Cambiamos el método para obtener un artículo de la BD a través de su código.
    public Articulo searchArticulo(@NotNull String codigo) {

        // Creamos un objeto para recibir la instancia.
        Articulo articulo = null;

        // Creamos un objeto Repositorio para la entidad Articulo.
        Repositorio<Articulo> repositorio = new ArticuloRepositorioImpl();

        // Comprobamos que haya artículos en la BD.
        if (repositorio.count() > 0) {
            // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
            // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un artículo.
            try {
                return repositorio.findOne(codigo);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // TODO -> Como extra podemos crear métodos para eliminar y modificar artículos

    // Eliminamos todos los elementos de la lista de artículos y devolvemos una lista con los elementos eliminados
    public ArrayList<Articulo> clearArticulos() {

        // Creamos una lista temporal
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
        } catch (IndexOutOfBoundsException e) {
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

        // Creamos el objeto
        Cliente cliente = null;

        // Añadimos el cliente a la BD, si se añade correctamente, devolvemos el Cliente.
        // Aplicamos la lógica de negocio para crear el cliente, no pueden haber dos clientes con el mismo
        // nif.
        try {
            // Seleccionamos el tipo de cliente
            switch (tipo) {
                case "Estandard" -> cliente = new ClienteEstandard(nombre, domicilio, nif, email);
                case "Premium" -> cliente = new ClientePremium(nombre, domicilio, nif, email);
            }

            // Comprobamos que el cliente no exista
            assert Objects.requireNonNull(cliente).getNif() != null;
            if (repositorio.findOne(cliente.getNif()) != null) {
                System.out.println(MessageFormat.format("El cliente {0} ya existe", cliente.getNombre()));
                return null;
            }

            if (repositorio.save(cliente)) {
                // Actualizamos la lista de clientes
                this.clientes = listClientes();

                // Devolvemos el cliente
                return this.clientes.get(this.clientes.sizeOf() - 1);
            }
        } catch (NullPointerException e) {
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
            assert Objects.requireNonNull(cliente).getNif() != null;
            if (repositorio.findOne(cliente.getNif()) != null) {
                System.out.println(MessageFormat.format("El cliente {0} ya existe", cliente.getNombre()));
                return null;
            }

            if (repositorio.save(cliente)) {
                // Actualizamos la lista de clientes
                this.clientes = listClientes();

                // Devolvemos el cliente
                return this.clientes.get(this.clientes.sizeOf() - 1);
            }
        } catch (NullPointerException e) {
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
            this.clientes = repositorio.findAll();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Comprobamos que la lista contenga elementos
        if (!this.clientes.isEmpty()) {

            // Intentamos devolver una copia de la lista
            try {
                return this.clientes.cloneOf();
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
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
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Cliente> filterClientesByType(String tipo) {

        // Nos aseguramos de que la lista esté actualizada
        this.clientes = listClientes();

        // Creamos el tipo de cliente según el tipo
        switch (tipo) {
            case "Estandard" -> tipo = "ClienteEstandard";
            case "Premium" -> tipo = "ClientePremium";
        }

        // Creamos una lista temporal
        ArrayList<Cliente> clientesTemp = new ArrayList<>();

        // Comprobamos que la lista contenga elementos
        if (!this.clientes.isEmpty()) {

            // Creamos un iterator
            ListIterator<Cliente> iterator = this.clientes.getLista().listIterator();

            try {
                // Recorremos la lista
                while (iterator.hasNext()) {
                    Cliente cliente = iterator.next();

                    // Si el tipo de cliente coincide, lo añadimos a la lista temporal
                    if (cliente.tipoCliente().equals(tipo)) {
                        clientesTemp.add(cliente);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // Si se produce una excepción, la lista temporal quedará vacía
                System.out.println(e.getMessage());
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
        if (repositorio.count() > 0) {
            // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un cliente.
            try {
                return repositorio.findOne(nif);
            } catch (NullPointerException e) {
                System.out.println(MessageFormat.format("Error al buscar el cliente con NIF {0}", nif));
                System.out.println(e.getMessage());
            }
        }

        // Si el proceso falla o el cliente no existe, devolvemos null.
        return null;
    }

    // Limpiamos la lista de clientes
    public ArrayList<Cliente> clearClientes() {

        // Creamos una lista temporal
        // Producto 3 --> Obtenemos los clientes directamente de la BD y la limpiamos en la BD.
        ArrayList<Cliente> clientesTemp = listClientes().getLista();

        // Creamos un flag de control.
        AtomicBoolean flag = new AtomicBoolean(false);

        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para eliminar todos los elementos de la tabla.
        if (repositorio.count() > 0) {

            try {
                listClientes().forEach(cliente -> flag.set(repositorio.delete(cliente.getId())));

                // Comprobamos que se haya ejecutado la limpieza
                if (flag.get()) {
                    // Reseteamos el contador de clientes
                    Cliente.resetTotalClientes();

                    // Limpiamos la lista de clientes
                    this.clientes.clear();
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }

        // En caso de producirse un error o no haber podido ejecutar la limpieza devolvemos una lista vacía
        return flag.get() ? clientesTemp : new ArrayList<>();
    }

    // TODO -> Como extra podemos crear métodos para eliminar y modificar clientes, el método getCliente será útil para la futura interacción con la BBDD
    public Cliente getCliente(@NotNull Cliente cliente) {

        // Actualizamos la lista de clientes
        this.clientes = listClientes();

        try {
            if (this.clientes.indexOf(cliente) != -1) {
                return this.clientes.get(this.clientes.indexOf(cliente));
            }
            ;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Cliente updateCliente(Cliente cliente) {

        // Producto 3 --> Actualizamos el cliente en la BD.
        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para actualizar el cliente en la BD.
        if (repositorio.count() > 0) {
            try {
                if (repositorio.save(cliente)) {
                    // Actualizamos la lista de clientes
                    this.clientes = listClientes();

                    // Devolvemos el cliente actualizado
                    return cliente;
                }
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    public Cliente deleteCliente(@NotNull Cliente cliente) {

        // Producto 3 --> Eliminamos el cliente de la BD.
        // Creamos un objeto Repositorio para la entidad Cliente.
        Repositorio<Cliente> repositorio = new ClienteRepositorioImpl();

        // Ejecutamos el método para eliminar el cliente de la BD.
        if (repositorio.count() > 0) {
            try {
                if (repositorio.delete(cliente.getId())) {

                    // Decrementamos el contador de clientes
                    Cliente.decreaseTotalClientes();

                    // Actualizamos la lista de clientes
                    this.clientes = listClientes();

                    // Devolvemos el cliente eliminado
                    return cliente;
                }
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        }

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

                // Actualizamos la lista de pedidos.
                this.pedidos = listPedidos();

                // Devolvemos el pedido con ID
                return this.pedidos.get(this.pedidos.sizeOf() - 1);
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Añadimos un pedido recibiendo la instancia del mismo
    // Producto 3 -> Refactorizando para actuar sobre la BD.
    public Pedido createPedido(@NotNull Pedido pedido) {

        // Creamos el repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

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

                // Actualizamos la lista de pedidos.
                this.pedidos = listPedidos();

                // Devolvemos el pedido con ID
                return this.pedidos.get(this.pedidos.sizeOf() - 1);
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
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
        try {
            this.pedidos = repositorio.findAll();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Comprobamos que la lista contenga elementos
        if (!this.pedidos.isEmpty()) {

            // Intentamos devolver una copia de la lista
            try {
                return this.pedidos.cloneOf();
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
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
            e.printStackTrace();
        }

        return null;
    }

    // Eliminamos un pedido
    public Pedido deletePedido(@NotNull Pedido pedido) {

        // Producto 3 --> Eliminamos el pedido de la BD.
        // Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Por seguridad, actualizamos y comprobamos que el pedido no está enviado.
        this.actualizarEstadoPedido(pedido);

        // Comprobamos si el pedido está enviado
        if (!pedido.pedidoEnviado() && repositorio.count() > 0) {
            try {
                // Ejecutamos el método para eliminar el pedido de la BD.
                if (repositorio.delete(pedido.getId())) {

                    // Decrementamos el contador de pedidos
                    Pedido.decrementarTotalPedidos();

                    // Actualizamos la lista de pedidos.
                    this.pedidos = listPedidos();

                    // Devolvemos el pedido eliminado.
                    return pedido;
                }
            } catch (NullPointerException e) {
                // Si se produce un error, devolvemos null
                e.printStackTrace();
            }
        }

        return null;
    }

    // Buscamos un pedido por su número de pedido
    public Pedido searchPedido(int numeroPedido) {

        // Producto 3 --> Buscamos el pedido en la BD.
        // Creamos un objeto Repositorio para la entidad Pedido.
        Repositorio<Pedido> repositorio = new PedidoRepositorioImpl();

        // Ejecutamos el método para obtener un objeto de la entidad desde la BD.
        // El método nos devuelve un objeto, con lo que podemos asignarlo directamente a un cliente.
        if (repositorio.count() > 0) {
            try {
                return repositorio.findOne(String.valueOf(numeroPedido));
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        // Si el proceso falla o el pedido no existe, devolvemos null.
        return null;
    }

    // Filtramos los pedidos según el cliente a través de su nif
    public ArrayList<Pedido> filterPedidosByCliente(@NotNull String nif) {

        // Creamos una lista temporal
        ArrayList<Pedido> pedidosTemp = new ArrayList<>();

        // Actualizamos la lista de pedidos.
        this.pedidos = listPedidos();

        // Comprobamos que la lista contenga elementos
        if (!this.pedidos.isEmpty()) {
            try {
                // Creamos un iterator
                ListIterator<Pedido> iterator = this.pedidos.getLista().listIterator();

                // Recorremos la lista
                while (iterator.hasNext()) {
                    Pedido pedido = iterator.next();

                    // Si los dni coinciden añadimos el pedido a la lista temporal
                    if (pedido.getCliente().getNif().equals(nif)) {
                        pedidosTemp.add(pedido);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
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

        // Actualizamos la lista de pedidos.
        this.pedidos = listPedidos();

        // Comprobamos que la lista contenga elementos
        if (!this.pedidos.isEmpty()) {

            try {
                // Creamos un iterador
                ListIterator<Pedido> iterator = this.pedidos.getLista().listIterator();

                // Recorremos la lista
                while (iterator.hasNext()) {
                    Pedido pedido = iterator.next();

                    // Añadimos el pedido a la lista temporal según el estado, si no se ha recibido ningún parámetro, se añaden todos los pedidos
                    switch (opt) {
                        case "enviado" -> {
                            // Si el pedido está enviado, lo añadimos a la lista temporal
                            if (pedido.getEsEnviado()) {
                                pedidosTemp.add(pedido);
                            }
                        }
                        case "pendiente" -> {
                            // Si el pedido está pendiente, lo añadimos a la lista temporal
                            if (!pedido.getEsEnviado()) {
                                pedidosTemp.add(pedido);
                            }
                        }
                        default -> {
                            // Si no se ha recibido ningún parámetro, añadimos todos los pedidos
                            pedidosTemp.add(pedido);
                        }
                    }
                }

                // Una vez obtenida la lista, la ordenamos por fecha
                pedidosTemp.sort(Comparator.comparing(Pedido::getFechaEnvio));
                // pedidosTemp.sort(Comparator.comparing(Pedido::getFechaPedido));
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }

        // Devolvemos la lista temporal, si se ha producido un error o no existen pedidos enviados devolvemos una lista vacía
        return pedidosTemp;
    }

    // Filtramos los pedidos enviados por su fecha de pedido
    public ArrayList<Pedido> filterPedidosEnviadosByFecha(@NotNull LocalDate fecha) {

        // Creamos una lista temporal
        ArrayList<Pedido> pedidosTemp = new ArrayList<>();

        // Actualizamos la lista de pedidos.
        this.pedidos = listPedidos();

        // Comprobamos que la lista contenga elementos
        if (!this.pedidos.isEmpty()) {

            try {
                // Creamos un iterador
                ListIterator<Pedido> iterator = this.pedidos.getLista().listIterator();

                // Recorremos la lista
                while (iterator.hasNext()) {
                    Pedido pedido = iterator.next();

                    // Si el pedido está enviado y la fecha de envío coincide con la fecha recibida, lo añadimos a la lista temporal
                    if (pedido.getEsEnviado() &&
                            pedido.getFechaPedido().equals(fecha)) {
                        pedidosTemp.add(pedido);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
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
        if (repositorio.count() > 0) {
            try {
                listPedidos().forEach(pedido -> flag.set(repositorio.delete(pedido.getId())));

                if (flag.get()) {
                    // Actualizamos el contador y limpiamos la lista de pedidos.
                    Pedido.resetTotalPedidos();

                    // Limpiamos la lista de pedidos
                    this.pedidos.clear();
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
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
        if (repositorio.count() > 0) {
            try {
                // Obtenemos la lista de pedidos de la BD y la iteramos con una función lambda.
                listPedidos().forEach(pedido -> {
                    // Comprobamos si el pedido está envidado
                    if (pedido.pedidoEnviado()) {
                        // Si está enviado, seteamos el nuevo estado del pedido y lo actualizamos
                        // en la BD.
                        pedido.setEsEnviado(true);

                        // Actualizamos el pedido en la BD.
                        repositorio.save(pedido);

                        // Incrementamos el contador
                        contPedidos.getAndIncrement();
                    }
                });
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }

        // Devolvemos el número de pedidos actualizados
        return contPedidos.get();
    }

    // Actualizamos el estado de un pedido
    public int actualizarEstadoPedido(@NotNull Pedido pedido) {

        // Comprobamos que el pedido no esté enviado
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
                pedido.getCliente().tipoCliente(),
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
    public void actualizarListas() {
        try {
            this.pedidos = listPedidos();
            this.clientes = listClientes();
            this.articulos = listArticulos();
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
package ciricefp.modelo;

import ciricefp.controlador.Controlador;
import ciricefp.modelo.listas.Listas;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;

/**
 * Esta clase funciona como un controlador interno para el módulo Modelo siguiendo el patrón MVC.
 * Implementa todos los métodos CRUD para los datos de la tienda.
 * También implementa métodos para la gestión de los pedidos.
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
    public Articulo createArticulo(String descripcion, double precio, double gastosEnvio, int preparacion)  {

        // Creamos el objeto y lo intentamos añadir a la lista
        Articulo articulo = new Articulo(descripcion, precio, gastosEnvio, preparacion);

        // Creamos un artículo temporal para realizar la comparación y comprobamos si el artículo ya existe
        Articulo articuloSrc = searchArticulo(articulo.getCodArticulo());
        // Comprobamos si el artículo ya existe
        if (articuloSrc != null) {
            // Comparamos los dos objetos
            if (articulo.compareTo(articuloSrc) == 0) {
                System.out.println(MessageFormat.format("El artículo {0} ya existe", articulo.getDescripcion()));
                return null;
            } else {
                // Manejamos la colisión de códigos
                articulo.setCodArticulo(articulo.generateCodigo(articulo.getDescripcion() + "$!*"));
            }
        }

        // Intentamos añadir el artículo a la lista
        try {
            return this.articulos.add(articulo)? articulo : null;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Añadimos un artículo a la lista recibiéndolo por parámetro
    public Articulo createArticulo(@NotNull Articulo articulo) {

        // Intentamos añadir el artículo a la lista
        try {
            return this.articulos.add(articulo)? articulo : null;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Devolvemos una lista con todos los elementos de la lista de artículos, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Articulo> listArticulos() {

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

    public Articulo searchArticulo(@NotNull String codigo) {

        if (!this.articulos.isEmpty()) {
            // Creamos un iterator
            ListIterator<Articulo> iterator = this.articulos.getLista().listIterator();

            try {
                // Recorremos la lista
                while (iterator.hasNext()) {
                    Articulo articulo = iterator.next();

                    // Si los códigos coinciden devolvemos el artículo
                    if (articulo.getCodArticulo().equals(codigo)) {
                        return articulo;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    // TODO -> Como extra podemos crear métodos para eliminar y modificar artículos

    // Eliminamos todos los elementos de la lista de artículos y devolvemos una lista con los elementos eliminados
    public ArrayList<Articulo> clearArticulos() {

        // Creamos una lista temporal
        ArrayList<Articulo> articulosTemp = this.articulos.getLista();

        // Limpiamos la lista
        try {
            if (this.articulos.clear()) {
                return articulosTemp;
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        // En caso de producirse un error o no haber podido ejecutar la limpieza devolvemos una lista vacía
        return new ArrayList<>();
    }

    /* Clientes */

    // Añadimos un cliente generando la instancia en el método
    public Cliente createCliente(String nombre, Direccion domicilio, String nif, String email, String tipo) {

        // Creamos el objeto y lo intentamos añadir a la lista
        Cliente cliente = null;

        // Creamos el cliente según el tipo
        switch (tipo) {
            case "Estandard" -> cliente = new ClienteEstandard(nombre, domicilio, nif, email);
            case "Premium" -> cliente = new ClientePremium(nombre, domicilio, nif, email);
        }

        // Nos aseguramos de que se haya inicializado correctamente
        if (cliente == null || searchCliente(cliente.getNif()) != null) {
            System.out.println("Error al crear el cliente o cliente duplicado");
            return null;
        }

        // Añadimos el cliente a la lista, si se ha añadido correctamente devolvemos el cliente
        try {
            return this.clientes.add(cliente)? cliente : null;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Añadimos un cliente recibiendo la instancia del mismo
    public Cliente createCliente(@NotNull Cliente cliente) {

        // Añadir el cliente a la lista, si se ha añadido correctamente devolvemos el cliente
        try {
            return this.clientes.add(cliente)? cliente : null;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Devolvemos una lista con todos los elementos de la lista de clientes, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Cliente> listClientes() {

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

    public ArrayList<Cliente> filterClientesByType(String tipo) {

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
    public Cliente searchCliente(@NotNull String nif) {

        // Comprobamos que la lista contenga elementos
        if (!this.clientes.isEmpty()) {
            // Creamos un iterator
            ListIterator<Cliente> iterator = this.clientes.getLista().listIterator();

            try {
                // Recorremos la lista
                while (iterator.hasNext()) {
                    Cliente cliente = iterator.next();

                    // Si los códigos coinciden devolvemos el artículo
                    if (cliente.getNif().equals(nif)) {
                        return cliente;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }

        // Si el proceso falla o el cliente no existe, devolvemos null.
        return null;
    }

    // Limpiamos la lista de clientes
    public ArrayList<Cliente> clearClientes() {

        // Creamos una lista temporal
        ArrayList<Cliente> clientesTemp = this.clientes.getLista();

        // Limpiamos la lista
        try {
            if (this.clientes.clear()) {
                return clientesTemp;
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        // En caso de producirse un error o no haber podido ejecutar la limpieza devolvemos una lista vacía
        return new ArrayList<>();
    }

    // TODO -> Como extra podemos crear métodos para eliminar y modificar clientes, el método getCliente será útil para la futura interacción con la BBDD
    public Cliente getCliente(@NotNull Cliente cliente) {

        try {
            if (this.clientes.indexOf(cliente) != -1) {
                return this.clientes.get(this.clientes.indexOf(cliente));
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Cliente updateCliente(Cliente cliente) {

        try {
            if (this.clientes.indexOf(cliente) != -1) {
                return this.clientes.update(cliente, this.clientes.indexOf(cliente));
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Cliente removeCliente(@NotNull Cliente cliente) {

        if (this.clientes.indexOf(cliente) != -1) {

            try {
                if (this.clientes.remove(cliente)) {
                    clientes.deacreaseClientesCounter();
                    return cliente;
                };
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    /* Pedidos */
    // Creamos un pedido recibiendo los parámetros necesarios
    public Pedido createPedido(@NotNull Cliente cliente, @NotNull Articulo articulo, @NotNull int cantidad) {

        // Creamos el pedido
        Pedido pedido = new Pedido(cliente, articulo, cantidad);

        // Añadimos el pedido a la lista, si se ha añadido correctamente devolvemos el pedido
        try {
            return this.pedidos.add(pedido)? pedido : null;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Añadimos un pedido recibiendo la instancia del mismo
    public Pedido createPedido(@NotNull Pedido pedido) {

        // Añadimos el pedido a la lista, si se ha añadido correctamente devolvemos el pedido
        try {
            return this.pedidos.add(pedido)? pedido : null;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Devolvemos una lista con todos los elementos de la lista de pedidos, usamos una copia para mantener las funciones lo más puras posibles
    public Listas<Pedido> listPedidos() {

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

    // Eliminamos un pedido
    public Pedido deletePedido(@NotNull Pedido pedido) {

        // Comprobamos si el pedido está enviado
        if (!pedido.pedidoEnviado()) {
            try {
                // Comprobamos que se elimine el pedido de la lista
                return this.pedidos.remove(pedido)? pedido : null;
            } catch (NullPointerException e) {
                // Si se produce un error, devolvemos null
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    // Buscamos un pedido por su número de pedido
    public Pedido searchPedido(int numeroPedido) {

        // Comprobamos que la lista contenga elementos y que el número de pedido sea válido
        if (!this.pedidos.isEmpty() && numeroPedido > 0) {
            // Creamos un iterator
            ListIterator<Pedido> iterator = this.pedidos.getLista().listIterator();

            try {
                // Recorremos la lista
                while (iterator.hasNext()) {
                    Pedido pedido = iterator.next();

                    // Si los códigos coinciden devolvemos el artículo
                    if (pedido.getNumeroPedido() == numeroPedido) {
                        return pedido;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }

        // Si el proceso falla o el pedido no existe, devolvemos null.
        return null;
    }

    // Filtramos los pedidos según el cliente a través de su nif
    public ArrayList<Pedido> filterPedidosByCliente(@NotNull String nif) {

        // Creamos una lista temporal
        ArrayList<Pedido> pedidosTemp = new ArrayList<>();

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

        // Creamos una lista temporal
        ArrayList<Pedido> pedidosTemp = this.pedidos.getLista();

        // Limpiamos la lista
        try {
            this.pedidos.getLista().clear();
            return pedidosTemp;
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    // Actualizamos el estado de los pedidos de la lista
    public int actualizarEstadoPedidos() {

        // Declaramos un contador
        int contPedidos = 0;

        // Comprobamos que la lista contenga elementos
        if (!pedidos.isEmpty()) {
            try {
                // Comprobamos todos los elementos de la lista
                for (Pedido pedido : this.pedidos) {
                    if (pedido.pedidoEnviado()) {
                        pedido.setEsEnviado(true);

                        // Incrementamos el contador
                        contPedidos++;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }

        // Devolvemos el número de pedidos actualizados
        return contPedidos;
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
}

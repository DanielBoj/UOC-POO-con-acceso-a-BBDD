package ciricefp.modelo;

import ciricefp.modelo.listas.Listas;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Implementamos los tests para la clase Datos.
 *
 * @author Cirice
 * @version 1.0
 * @since 03-2023
 */
class DatosTest {

    // Objeto para ejectuar los tests.
    Datos datos = new Datos();
    @Test
    void createArticulo() {

        try {
            datos.createArticulo(new Articulo("Corbatero", 10.50, 10.0, 1));

            assertEquals(1, datos.getArticulos().getLista().size(), "El tamaño de la lista de artículos no es correcto.");
            assertEquals("Corbatero", datos.getArticulos().getLista().get(0).getDescripcion(), "La descripción del artículo no es correcta.");
            assertEquals(10.50, datos.getArticulos().getLista().get(0).getPvp(), "El PVP del artículo no es correcto.");
            assertEquals(10.0, datos.getArticulos().getLista().get(0).getTiempoPreparacion(), "El tiempo de preparación del artículo no es correcto.");

            datos.getArticulos().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void listArticulos() {

        try {

            // Creamos artículos de prueba.
            datos.createArticulo(new Articulo("Corbatero", 10.50, 10.0, 1));
            datos.createArticulo(new Articulo("Camiseta", 3.50, 20.0, 2));
            datos.createArticulo(new Articulo("Pantalones", 9.85, 30.0, 3));

            Listas<Articulo> lista = datos.listArticulos();

            // Comprobamos que los datos del artículo se han guardado correctamente.
            assertEquals(3, lista.getLista().size(), "El tamaño de la lista de artículos no es correcto.");
            assertEquals("Corbatero", lista.getLista().get(0).getDescripcion(), "La descripción del artículo no es correcta.");
            assertEquals(10.50, lista.getLista().get(0).getPvp(), "El PVP del artículo no es correcto.");
            assertEquals(10.0, lista.getLista().get(0).getGastosEnvio(), "Los gastos de envío no son correctos.");
            assertEquals(1, lista.getLista().get(0).getTiempoPreparacion(), "El tiempo de preparación del artículo no es correcto.");
            assertNotNull(lista.getLista().get(0).getCodArticulo(), "El código de artículo no se genera.");
            assertEquals("Camiseta", lista.getLista().get(1).getDescripcion(), "La descripción del artículo no es correcta.");
            assertEquals(3.50, lista.getLista().get(1).getPvp(), "El PVP del artículo no es correcto.");
            assertEquals(20.0, lista.getLista().get(1).getGastosEnvio(), "Los gastos de envío no son correctos.");
            assertEquals(2, lista.getLista().get(1).getTiempoPreparacion(), "El tiempo de preparación del artículo no es correcto.");
            assertNotNull(lista.getLista().get(1).getCodArticulo(), "El código de artículo no se genera.");
            assertEquals("Pantalones", lista.getLista().get(2).getDescripcion(), "La descripción del artículo no es correcta.");
            assertEquals(9.85, lista.getLista().get(2).getPvp(), "El PVP del artículo no es correcto.");
            assertEquals(30.0, lista.getLista().get(2).getGastosEnvio(), "Los gastos de envío no son correctos.");
            assertEquals(3, lista.getLista().get(2).getTiempoPreparacion(), "El tiempo de preparación del artículo no es correcto.");
            assertNotNull(lista.getLista().get(2).getCodArticulo(), "El código de artículo no se genera.");

            // Mostramos los datos
            lista.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void createCliente() {

        try {

            // Creamos clientes de prueba.
            datos.createCliente(new ClienteEstandard("Cirice Hélada", new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España"),
                    "12345678A",
                    "cirice@algo.com"));

            datos.createCliente(new ClientePremium("Platón Heráclito", new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España"),
                    "12345678B",
                    "platon@alguien.com"));

            // Comprobamos que los datos del cliente se han guardado correctamente.
            assertEquals(2, datos.getClientes().getLista().size(), "El tamaño de la lista de clientes no es correcto.");

            // Datos cliente Estandard
            assertEquals("Cirice Hélada", datos.getClientes().getLista().get(0).getNombre(), "El nombre del cliente no es correcto.");
            assertEquals("12345678A", datos.getClientes().getLista().get(0).getNif(), "El NIF del cliente no es correcto.");

            // Datos cliente Premium
            assertEquals("Platón Heráclito", datos.getClientes().getLista().get(1).getNombre(), "El nombre del cliente no es correcto.");
            assertEquals("12345678B", datos.getClientes().getLista().get(1).getNif(), "El NIF del cliente no es correcto.");
            assertEquals(30.0, datos.getClientes().getLista().get(1).calcAnual(), "La cuota del cliente no es correcta.");
            assertTrue(datos.getClientes().getLista().get(1) instanceof ClientePremium, "El cliente no es de tipo ClientePremium.");
            assertNotNull(((ClientePremium) datos.getClientes().getLista().get(1)).getCodSocio(), "El código de socio no se genera.");

            // Direcciones
            assertEquals("Calle Media, 5", datos.getClientes().getLista().get(0).getDomicilio().getDireccion(), "El domicilio del cliente no es correcto.");
            assertEquals("Barcelona", datos.getClientes().getLista().get(0).getDomicilio().getCiudad(), "La población del cliente no es correcta.");
            assertEquals("Barcelona", datos.getClientes().getLista().get(0).getDomicilio().getProvincia(), "La provincia del cliente no es correcta.");
            assertEquals("08001", datos.getClientes().getLista().get(0).getDomicilio().getCodigoPostal(), "El código postal del cliente no es correcto.");
            assertEquals("España", datos.getClientes().getLista().get(0).getDomicilio().getPais(), "El país del cliente no es correcto.");
            assertTrue(datos.getClientes().getLista().get(0).getDomicilio() instanceof Direccion, "El domicilio no es de tipo Direccion.");

            assertEquals("Calle Media, 5", datos.getClientes().getLista().get(1).getDomicilio().getDireccion(), "El domicilio del cliente no es correcto.");
            assertEquals("Barcelona", datos.getClientes().getLista().get(1).getDomicilio().getCiudad(), "La población del cliente no es correcta.");
            assertEquals("Barcelona", datos.getClientes().getLista().get(1).getDomicilio().getProvincia(), "La provincia del cliente no es correcta.");
            assertEquals("08001", datos.getClientes().getLista().get(1).getDomicilio().getCodigoPostal(), "El código postal del cliente no es correcto.");
            assertEquals("España", datos.getClientes().getLista().get(1).getDomicilio().getPais(), "El país del cliente no es correcto.");
            assertTrue(datos.getClientes().getLista().get(1).getDomicilio() instanceof Direccion, "El domicilio no es de tipo Direccion.");

            // Mostramos los datos de los clientes.
            datos.getClientes().forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void listClientes() {

        try {

            // Creamos clientes de prueba.
            datos.createCliente(new ClienteEstandard("Cirice Hélada", new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España"),
                    "12345678A",
                    "cirice@algo.com"));

            datos.createCliente(new ClientePremium("Platón Heráclito", new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España"),
                    "12345678B",
                    "platon@alguien.com"));

            Listas<Cliente> lista = datos.getClientes();
            // Comprobamos que los datos del cliente se han guardado correctamente.
            assertEquals(2, lista.getLista().size(), "El tamaño de la lista de clientes no es correcto.");
            assertEquals("Cirice Hélada", lista.getLista().get(0).getNombre(), "El nombre del cliente no es correcto.");
            assertEquals("12345678A", lista.getLista().get(0).getNif(), "El NIF del cliente no es correcto.");
            assertEquals("Platón Heráclito", lista.getLista().get(1).getNombre(), "El nombre del cliente no es correcto.");
            assertEquals("12345678B", lista.getLista().get(1).getNif(), "El NIF del cliente no es correcto.");

            // Mostramos los datos de los clientes.
            lista.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void searchCliente() {

        try {
            // Creamos dos clientes.
            datos.createCliente(new ClienteEstandard("Cirice Hélada", new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España"),
                    "12345678A",
                    "cirice@algo.com"));

            datos.createCliente(new ClientePremium("Platón Heráclito", new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España"),
                    "12345678B",
                    "platon@alguien.com"));

            // Buscamos un cliente que existe.
            Cliente clienteSearched = datos.searchCliente("12345678A");

            // Comprobamos que el cliente buscado es el correcto.
            assertEquals("Cirice Hélada", clienteSearched.getNombre(), "El nombre del cliente no es correcto.");
            assertEquals("12345678A", clienteSearched.getNif(), "El NIF del cliente no es correcto.");
            assertTrue(clienteSearched instanceof ClienteEstandard, "El cliente no es de tipo ClienteEstandard.");

            System.out.println("Cliente buscado: " + clienteSearched);

            // Buscamos un cliente que no existe.
            clienteSearched = datos.searchCliente("12345678C");

            // Comprobamos que el cliente buscado no existe.
            assertNull(clienteSearched, "El cliente buscado existe.");

            System.out.println("Cliente buscado: " + clienteSearched);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void createPedido() {

        try {

            // Creamos un cliente.
            datos.createCliente(new ClientePremium("Platón Heráclito", new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España"),
                    "12345678B",
                    "platon@alguien.com"));

            // Creamos un artículo.
            datos.createArticulo(new Articulo("Corbatero", 10.50, 10.0, 1));

            // Creamos un pedido.
            datos.createPedido(datos.getClientes().getLista().get(0), datos.getArticulos().getLista().get(0), 5);

            // Comprobamos que el pedido se ha creado correctamente.
            assertEquals(1, datos.getPedidos().getLista().size(), "El tamaño de la lista de pedidos no es correcto.");
            assertEquals("Platón Heráclito", datos.getPedidos().getLista().get(0).getCliente().getNombre(), "El nombre del cliente no es correcto.");
            assertEquals("Corbatero", datos.getPedidos().getLista().get(0).getArticulo().getDescripcion(), "El nombre del artículo no es correcto.");
            assertEquals(5, datos.getPedidos().getLista().get(0).getUnidades(), "La cantidad del pedido no es correcta.");
            assertEquals(52.50, datos.getPedidos().getLista().get(0).calcularSubtotal(), "El importe del pedido no es correcto.");
            assertEquals(8.0, datos.getPedidos().getLista().get(0).precioEnvio(), "Los gastos de envío no son correctos.");
            assertEquals(60.50, datos.getPedidos().getLista().get(0).precioTotal(), "El importe total del pedido no es correcto.");
            assertEquals(LocalDate.now().plusDays(1), datos.getPedidos().getLista().get(0).calcularFechaEnvio(), "La fecha de entrega no es correcta.");

            // Mostramos los datos del pedido.
            datos.getPedidos().forEach(pedido -> datos.printTicket(pedido));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void filterPedidosByCliente() {

        try {
            // Creamos un cliente.
            datos.createCliente(new ClientePremium("Platón Heráclito", new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España"),
                    "12345678B",
                    "platon@alguien.com"));

            // Creamos un artículo.
            datos.createArticulo(new Articulo("Corbatero", 10.50, 10.0, 1));

            // Creamos un pedido.
            datos.createPedido(datos.getClientes().getLista().get(0), datos.getArticulos().getLista().get(0), 5);

            // Filtramos los pedidos por el cliente.
            ArrayList<Pedido> lista = datos.filterPedidosByCliente("12345678B");

            // Comprobamos que el pedido se ha creado correctamente.
            assertEquals(1, lista.size(), "El tamaño de la lista de pedidos no es correcto.");
            assertEquals("Platón Heráclito", lista.get(0).getCliente().getNombre(), "El nombre del cliente no es correcto.");
            assertEquals("Corbatero", lista.get(0).getArticulo().getDescripcion(), "El nombre del artículo no es correcto.");
            assertEquals(5, lista.get(0).getUnidades(), "La cantidad del pedido no es correcta.");
            assertEquals(52.50, lista.get(0).calcularSubtotal(), "El importe del pedido no es correcto.");
            assertEquals(8.0, lista.get(0).precioEnvio(), "Los gastos de envío no son correctos.");
            assertEquals(60.50, lista.get(0).precioTotal(), "El importe total del pedido no es correcto.");
            assertEquals(LocalDate.now().plusDays(1), lista.get(0).calcularFechaEnvio(), "La fecha de entrega no es correcta.");

            // Mostramos los datos del pedido.
            lista.forEach(pedido -> datos.printTicket(pedido));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void filterPedidoByEstado() {

        try {
            // Creamos un cliente.
            datos.createCliente(new ClientePremium("Platón Heráclito", new Direccion("Calle Media, 5",
                    "Barcelona",
                    "Barcelona",
                    "08001",
                    "España"),
                    "12345678B",
                    "platon@alguien.com"));

            // Creamos un artículo.
            datos.createArticulo(new Articulo("Corbatero", 10.50, 10.0, 1));

            // Creamos un pedido.
            datos.createPedido(datos.getClientes().getLista().get(0), datos.getArticulos().getLista().get(0), 5);

            // Filtramos los pedidos por estado
            ArrayList<Pedido> lista = datos.filterPedidosByEstado("Pendiente");

            // Comprobamos que el pedido se ha creado correctamente.
            assertEquals(1, lista.size(), "El tamaño de la lista de pedidos no es correcto.");
            assertEquals("Platón Heráclito", lista.get(0).getCliente().getNombre(), "El nombre del cliente no es correcto.");
            assertEquals("Corbatero", lista.get(0).getArticulo().getDescripcion(), "El nombre del artículo no es correcto.");
            assertEquals(5, lista.get(0).getUnidades(), "La cantidad del pedido no es correcta.");
            assertEquals(52.50, lista.get(0).calcularSubtotal(), "El importe del pedido no es correcto.");
            assertEquals(8.0, lista.get(0).precioEnvio(), "Los gastos de envío no son correctos.");
            assertEquals(60.50, lista.get(0).precioTotal(), "El importe total del pedido no es correcto.");
            assertEquals(LocalDate.now().plusDays(1), lista.get(0).calcularFechaEnvio(), "La fecha de entrega no es correcta.");

            // Mostramos los datos del pedido.
            lista.forEach(pedido -> datos.printTicket(pedido));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO --> Crear test para los métodos de implementación de acciones CRUD en la capa de acceso a BBDD
}
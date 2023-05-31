package ciricefp.vista.interfaces;

import ciricefp.modelo.Pedido;

import java.util.ArrayList;
import java.util.Optional;

public interface IPedidoController {
    public Optional<Pedido> addPedido(String nif, String codArticulo, int cantidad);
    public Optional<ArrayList<Pedido>> listPedidos();
    public Optional<ArrayList<Pedido>> listPedidosPendientes();
    public Optional<ArrayList<Pedido>> listPedidosCliente(String nif);
    public Optional<Integer> updatePedidos();
    public Optional<Boolean> deletePedido(Pedido actualPedido);

}

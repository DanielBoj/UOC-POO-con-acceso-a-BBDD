package ciricefp.vista;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.Cliente;
import ciricefp.modelo.Pedido;
import com.sun.source.tree.ReturnTree;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class VistaPedidos {
    public Pedido addPedido(MenuPrincipal menu){
        Scanner teclado = new Scanner(System.in);

        System.out.print("Introduce Numero del Pedido: ");
        int numeroPedido  = Integer.parseInt(teclado.nextLine());

        //Comprobamos si existe el cliente y sino lo creamos.
        System.out.print("Introduce Nif del Cliente: ");
        String nif = teclado.nextLine();
        Cliente cliente = menu.searchCliente(nif);
        if( cliente == null ){
            cliente = menu.getVistaClientes().addCliente();
            menu.addCliente(cliente);
        }

        //Comprobamos si existe el articulo.
        System.out.print("Introduce código del Articulo existente: ");
        String code = teclado.nextLine();
        Articulo articulo = menu.searchArticulo(code);
        if( articulo == null ){
            System.out.print("No existe el articulo ");
            return null;
        }

        System.out.print("Introduce Número de Unidades: ");
        int unidades  = Integer.parseInt(teclado.nextLine());
        LocalDateTime fechaPedido = LocalDateTime.now();
        boolean esEnviado = false;

        //Creamos el pedido a partir de todos los datos anteriores.
        Pedido nuevoPedido = new Pedido(cliente,articulo,unidades,fechaPedido,esEnviado);

        return nuevoPedido;
    }

    public Pedido deletePedido(MenuPrincipal menu){
        Scanner teclado = new Scanner(System.in);

        //Comprobamos si existe el pedido.
        System.out.print("Introduce código del Pedido existente: ");
        int code = Integer.parseInt(teclado.nextLine());
        Pedido pedido = menu.searchPedido(code);
        if( pedido == null ){
            System.out.print("No existe el pedido ");
            return null;
        }else{
            menu.actualizarEstadoPedido(pedido);
            if(pedido.getEsEnviado()){
                System.out.print("El pedido ha sido enviado y no se puede cancelar");
                return null;
            }
        }

        return pedido;
    }

    public void printPedidosPendientes(ArrayList<Pedido> listaPedidos){
        System.out.println("Listado Pedidos Pendientes:");
        for(Pedido ped : listaPedidos){
            System.out.println(ped.toString());
        }
    }
    public void printPedidosEnviados(ArrayList<Pedido> listaPedidos){
        System.out.println("Listado Pedidos Enviados:");
        for(Pedido ped : listaPedidos){
            System.out.println(ped.toString());
        }
    }
}

package ciricefp.vista;

import ciricefp.modelo.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class VistaClientes {
    public Cliente addCliente(){
        Scanner teclado = new Scanner(System.in);

        //Elegimos cliente
        System.out.println("");
        System.out.println("Tipos de clientes: ");
        System.out.println("1. Cliente Estandar.");
        System.out.println("2. Cliente Premium.");
        String tipoCliente;
        System.out.println("");
        System.out.print("Elige un tipo de cliente: ");
        tipoCliente = teclado.nextLine();

        //Recogemos datos comunes de ambos tipos de clientes.
        System.out.print("Introduce nombre del nuevo cliente: ");
        String nombre = teclado.nextLine();
        System.out.print("Introduce nif del nuevo cliente: ");
        String nif = teclado.nextLine();
        System.out.print("Introduce email del nuevo cliente: ");
        String email = teclado.nextLine();
        //Recogemos datos de direccion.
        System.out.print("Introduce direccion del nuevo cliente: ");
        String direccion = teclado.nextLine();
        System.out.print("Introduce ciudad del nuevo cliente: ");
        String ciudad = teclado.nextLine();
        System.out.print("Introduce provincia del nuevo cliente: ");
        String provincia = teclado.nextLine();
        System.out.print("Introduce codigoPostal del nuevo cliente: ");
        String codigoPostal = teclado.nextLine();
        System.out.print("Introduce pais del nuevo cliente: ");
        String pais = teclado.nextLine();
        Direccion nuevaDireccion = new Direccion(direccion, ciudad, provincia, codigoPostal, pais);

        if( tipoCliente.compareTo("2") == 0 ){
            System.out.print("Introduce cuota del nuevo cliente: ");
            double cuota = Double.parseDouble(teclado.nextLine());
            System.out.print("Introduce descuento del nuevo cliente: ");
            double descuento = Double.parseDouble(teclado.nextLine());

            Cliente nuevoCliente = new ClientePremium(nombre,nuevaDireccion,nif,email,cuota,descuento);
            return nuevoCliente;
        }else{
            Cliente nuevoCliente = new ClienteEstandard(nombre,nuevaDireccion,nif,email);
            return nuevoCliente;
        }
    }
    public void printClientes(ArrayList<Cliente> listaClientes){
        System.out.println("Listado Clientes:");
        for(Cliente cl : listaClientes){
            System.out.println(cl.toString());
        }
    }

    public void printClientesPremium(ArrayList<Cliente> listaClientes){
        System.out.println("Listado Clientes Premium:");
        for(Cliente cl : listaClientes){
            System.out.println(cl.toString());
        }
    }

    public void printClientesEstandar(ArrayList<Cliente> listaClientes){
        System.out.println("Listado Clientes Estandar:");
        for(Cliente cl : listaClientes){
            System.out.println(cl.toString());
        }
    }

}

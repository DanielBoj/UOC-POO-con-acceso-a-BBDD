package ciricefp.vista;

import ciricefp.modelo.Articulo;

import java.util.ArrayList;
import java.util.Scanner;

public class VistaArticulos {

    public Articulo addArticulo(){
        Scanner teclado = new Scanner(System.in);

        System.out.print("Introduce código del Articulo: ");
        String codigo = teclado.nextLine();
        System.out.print("Introduce descripción del Articulo: ");
        String descripcion = teclado.nextLine();
        System.out.print("Introduce precio del Articulo (€): ");
        double pvp = Double.parseDouble(teclado.nextLine());
        System.out.print("Introduce gastos de envío del Articulo (€): ");
        double gastosEnvio = Double.parseDouble(teclado.nextLine());
        System.out.print("Introduce tiempo de preparación del Articulo (Minutos): ");
        int tiempoPreparacion = Integer.parseInt(teclado.nextLine());

        Articulo nuevoArticulo = new Articulo(codigo, descripcion,pvp,gastosEnvio,tiempoPreparacion);

        return nuevoArticulo;
    }

    public void printArticulos(ArrayList<Articulo> listaArticulos){
        System.out.println("Listado Articulos:");
        for(Articulo art : listaArticulos){
            System.out.println( art.toString() );
        }
    }

}

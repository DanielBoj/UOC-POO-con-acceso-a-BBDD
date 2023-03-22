package ciricefp.modelo;

import java.util.ArrayList;

/**
 * Clase genérica para la creación de listas de objetos de elementos tipo Cliente, Artículo o Pedido.
 *
 * @author Cirice
 * @param <T> Parámetro de tipo genérico.
 */
public class Listas<T> {

    private ArrayList<T> lista;


    public Listas() {
        this.lista = new ArrayList<T>();
    }

    public Listas(ArrayList<T> lista) {
        this.lista = lista;
    }

    public ArrayList<T> getLista() {
        return lista;
    }

    public void setLista(ArrayList<T> lista) {
        this.lista = lista;
    }

    @Override
    public String toString() {
        return "Listas{" +
                "lista=" + lista +
                '}';
    }
}

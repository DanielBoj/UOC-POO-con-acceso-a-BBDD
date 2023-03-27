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

    public int getSize() {
        return this.lista.size();
    }
    public void add(T t) {
        this.lista.add(t);
    }
    public void borrar(T t) {
        this.lista.remove(t);
    }
    public T getAt(int position) {
        return this.lista.get(position);
    }
    public void clear() {
        this.lista.clear();
    }
    public boolean isEmpty() {
        return this.lista.isEmpty();
    }
    public ArrayList<T> getArrayList() {
        ArrayList<T> arrlist = new ArrayList<>(lista);
        return arrlist;
    }

    @Override
    public String toString() {
        return "Listas{" +
                "lista=" + lista +
                '}';
    }
}

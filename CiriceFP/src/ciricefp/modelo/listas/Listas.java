package ciricefp.modelo.listas;

import ciricefp.modelo.Cliente;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Clase genérica para la creación de listas de objetos de elementos tipo Cliente, Artículo o Pedido.
 * Implementa todos los métodos necesarios para realizar las operaciones CRUD sobre las listas, también incluye
 * implementaciones de los métodos de la interfaz Iterable para poder recorrer las listas con un bucle for-each.
 * Además, se han añadido métodos extra pensando en prevenir cualquier deuda técnica para futuras mejoras.
 *
 * @author Cirice
 */
public class Listas<T> implements Iterable<T> {

    // Todas las listas parten de una generalización de la clase ArrayList de Java. Usamos la clase genérica T para
    // que pueda recibir cualquier tipo de objeto. Especificamos la generalización mediante el operador diamante.
    protected ArrayList<T> lista;

    // Preparamos el constructor para que pueda recibir una lista de elementos de tipo T.
    public Listas() {
        this.lista = new ArrayList<>();
    }

    // Preparamos el constructor para que pueda recibir una lista de elementos de tipo T que recibe por parámetro.
    public Listas(@NotNull ArrayList<T> lista) {
        this.lista = new ArrayList<>(lista);
        this.lista.addAll(lista);
    }

    /* Getters & Setters */
    public ArrayList<T> getLista() {
        return new ArrayList<>(lista);
    }

    public void setLista(ArrayList<T> lista) {
        this.lista.addAll(lista);
    }

    // StringBuilder nos permite implementar un patrón de diseño de string para el método toString() de una forma visual muy clara.
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("Lista " + this.getClass().getSimpleName() + ":\n");
        for (T elemento : lista) {
            sb.append(elemento).append("\n");
        }

        return sb.toString();
    }

    /* Operaciones CRUD sobre las listas */
    // Añadimos un elemento a la lista.
    public boolean add(@NotNull T elemento) {

        try {
            this.lista.add(elemento);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return false; // No se añade el elemento nulo a la lista.
        }

        return true;
    }

    // Eliminamos un elemento de la lista.
    public boolean remove(@NotNull T elemento) {

        try {
            return this.lista.remove(elemento);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    // Eliminamos un elemento de la lista por su índice.
    public T removeByInd(int indexOfElemento) {
        try {
            return this.lista.remove(indexOfElemento);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return null; // No se elimina el elemento nulo de la lista.
        }
    }

    // Actualizamos un elemento de la lista.
    public T update(@NotNull T elemento, int indexOfElemento) {

        try {
            return this.lista.set(indexOfElemento, elemento);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            return null; // No se actualiza el elemento nulo de la lista.
        }
    }

    // Obtenemos un elemento de la lista por su índice.
    public T get(int indexOfElemento) {

        try {
            return this.lista.get(indexOfElemento);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            return null; // No se devuelve el elemento nulo de la lista.
        }
    }

    // Obtenemos el índice de un elemento de la lista.
    public int indexOf(T elemento) {

        try {
            return this.lista.indexOf(elemento);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            return -1; // No se devuelve el elemento nulo de la lista.
        }
    }

    // Comprobamos si un elemento está en la lista.
    public boolean contains(@NotNull T elemento) {

        try {
            return this.lista.contains(elemento);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return false; // No se devuelve el elemento nulo de la lista.
        }
    }

    // Devuelve true si la lista está vacía
    public boolean isEmpty() {
        return this.lista.isEmpty();

        /* Equivale a lo siguiente:
        if (this.lista.size() == 0) {
            return true;
         */
    }

    // Vaciamos la lista.
    public boolean clear() {

        // Si la lista está vacía, ya está limpia.
        if (this.lista.isEmpty()) {
            return true;
        }

        // Intentamos vaciar la lista
        try {
            this.lista.clear();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false; // No se limpia la lista.
        }
    }

    // Implementamos un método para devolver una copia de la lista y mantener las funciones lo más puras posibles
    public Listas<T> cloneOf() {

        // Trabajamos con una copia para mantener la original intacta
        Listas<T> cpyLista = new Listas<>();

        // Recorremos la lista y copiamos los elementos
        for (T elemento : lista) {
            cpyLista.add(elemento);
        }

        return cpyLista;
    }

    // Implementamos el método de la interfaz Iterable<T> para poder recorrer la lista con un bucle for-each.
    @Override
    public Iterator<T> iterator() {
        return this.lista.iterator();
    }

    // Implementamos un método forEach() para poder usar la interfaz Iterable<T> en todas las listas.
    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    /* Métodos para prevenir deuda técnica */

    // Permite comparar dos listas de cualquier tipo.
    public boolean equals(@NotNull Listas<T> lista) {
        return this.lista.equals(lista.lista);
    }

    // Añade todos los elementos de un parámetro tipo ArrayList
    public boolean addAll(@NotNull ArrayList<T> lista) {
        return this.lista.addAll(lista);
    }

    // Método para eliminar todos los elementos de un parámetro tipo ArrayList coincidentes con nuestra lista
    public boolean removeAll(@NotNull ArrayList<T> lista) {
        return this.lista.removeAll(lista);
    }

    // Devuelve true si todos los elementos del ArrayList están presentes en nuestra lista
    public boolean containsAll(@NotNull ArrayList<T> lista) {
        return this.lista.containsAll(lista);
    }

    // Devuelve la última posición de un elemento en la lista
    public int lastIndexOf(T elemento) {
        return this.lista.lastIndexOf(elemento);
    }

    // Añade una posición al contador de elementos de la lista
    public void advanceClientesCounter() {
        Cliente.advanceTotalClientes();
    }

    // Resta una posición al contador de elementos de la lista
    public void deacreaseClientesCounter() {
        Cliente.decreaseTotalClientes();
    }

    // Obtenemos el tamaño de la lista.
    public int sizeOf() {
        return this.lista.size();
    }
}

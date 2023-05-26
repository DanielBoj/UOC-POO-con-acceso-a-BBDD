package ciricefp.vista.interfaces;

import ciricefp.modelo.Articulo;

import java.util.ArrayList;
import java.util.Optional;

public interface IArticuloController {
    boolean addArticulo(String descripcion, double precio, double gastosEnvio, int tiempoPreparacion);
    public Optional<ArrayList<Articulo>> listArticulos();
    public Optional<Integer> clearArticulos();

}

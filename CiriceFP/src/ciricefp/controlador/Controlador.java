package ciricefp.controlador;

import ciricefp.modelo.*;
import ciricefp.vista.*;

/**
 * Esta clase implementa el controlador de la tienda.
 *
 * @author Cirice
 */
public class Controlador {

    private Datos datos;
    private MenuPrincipal menu;

    public Controlador(Datos datos, MenuPrincipal menu) {
        this.datos = datos;
        this.menu = menu;
    }

    public Datos getDatos() {
        return datos;
    }

    public void setDatos(Datos datos) {
        this.datos = datos;
    }

    public MenuPrincipal getMenu() {
        return menu;
    }

    public void setMenu(MenuPrincipal menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Controlador{" +
                "datos=" + datos +
                ", menu=" + menu +
                '}';
    }
}

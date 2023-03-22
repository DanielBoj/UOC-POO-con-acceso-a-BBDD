package ciricefp.vista;

import ciricefp.controlador.*;

public class MenuPrincipal {
    private Controlador controlador;
    private VistaArticulos vistaArticulos;
    private VistaClientes vistaClientes;
    private VistaPedidos vistaPedidos;

    public MenuPrincipal(Controlador controlador) {
        this.controlador = controlador;
    }

    public MenuPrincipal(Controlador controlador, VistaArticulos vistaArticulos, VistaClientes vistaClientes, VistaPedidos vistaPedidos) {
        this.controlador = controlador;
        this.vistaArticulos = vistaArticulos;
        this.vistaClientes = vistaClientes;
        this.vistaPedidos = vistaPedidos;
    }

    public Controlador getControlador() {
        return controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public VistaArticulos getVistaArticulos() {
        return vistaArticulos;
    }

    public void setVistaArticulos(VistaArticulos vistaArticulos) {
        this.vistaArticulos = vistaArticulos;
    }

    public VistaClientes getVistaClientes() {
        return vistaClientes;
    }

    public void setVistaClientes(VistaClientes vistaClientes) {
        this.vistaClientes = vistaClientes;
    }

    public VistaPedidos getVistaPedidos() {
        return vistaPedidos;
    }

    public void setVistaPedidos(VistaPedidos vistaPedidos) {
        this.vistaPedidos = vistaPedidos;
    }

    @Override
    public String toString() {
        return "MenuPrincipal{" +
                "controlador=" + controlador +
                ", vistaArticulos=" + vistaArticulos +
                ", vistaClientes=" + vistaClientes +
                ", vistaPedidos=" + vistaPedidos +
                '}';
    }
}

package ciricefp.vista;

public class MenuPrincipal {
    private Controlador controlador;

    public MenuPrincipal(Controlador controlador) {
        this.controlador = controlador;
    }

    public Controlador getControlador() {
        return controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public String toString() {
        return "MenuPrincipal{" +
                "controlador=" + controlador +
                '}';
    }
}

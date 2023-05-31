package ciricefp.vista.interfaces;

import ciricefp.modelo.Cliente;

import java.util.ArrayList;
import java.util.Optional;

public interface IClienteController {
    public Optional<Cliente> addCliente(String nombre, String nif, String email, String domicilio, String poblacion,
                                        String provincia, String cp, String pais, String tipo);
    public Optional<ArrayList<Cliente>> listClientes();
    public Optional<ArrayList<String>> listClientesNif();
    public Optional<Cliente> buscarCliente(String nif);
    public Optional<ArrayList<Cliente>> filtrarClientes(String tipo);

}

package ciricefp.modelo.services;

import ciricefp.modelo.Cliente;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.services.interfaces.ClientesService;

import java.util.Optional;

public class ClienteServiceImpl implements ClientesService {
    @Override
    public Listas<Cliente> findAll() {
        return null;
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Cliente> findOne(String key) {
        return Optional.empty();
    }

    @Override
    public boolean save(Cliente articulo) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public Optional<Cliente> getLast() {
        return Optional.empty();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean resetId() {
        return false;
    }
}

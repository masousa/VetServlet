package br.com.letscode.service;

import br.com.letscode.dao.ClienteDao;
import br.com.letscode.dominio.Cliente;
import jakarta.inject.Inject;

import java.util.UUID;

public class ClienteServiceImpl implements ClienteService{
    @Inject
    private ClienteDao clienteDao;

    @Override
    public Cliente inserir(Cliente cliente) {
        cliente.setIdentificador(UUID.randomUUID().toString());
        return clienteDao.inserirArquivo(cliente);
    }
}


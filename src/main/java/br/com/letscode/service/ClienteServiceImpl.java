package br.com.letscode.service;

import br.com.letscode.dao.ClienteDao;
import br.com.letscode.dominio.Cliente;
import br.com.letscode.excecoes.UsuarioJaExisteException;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClienteServiceImpl implements ClienteService{
    @Inject
    private ClienteDao clienteDao;

    @Override
    public Cliente inserir(Cliente cliente) throws IOException {
        if(clienteDao.findByCpf(cliente.getCpf()).isPresent()){
            throw new UsuarioJaExisteException("Já existe um usuário com este registro");
        }
        cliente.setIdentificador(UUID.randomUUID().toString());
        return clienteDao.inserirNoArquivo(cliente);
    }

    @Override
    public List<Cliente> listAll() throws IOException {
        return clienteDao.getAll();
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) throws IOException {
        return clienteDao.findByCpf(cpf);
    }

    @Override
    public Cliente alterar(Cliente cliente, String identificador) throws IOException {

        return clienteDao.alterarArquivo(cliente, identificador);
    }

    @Override
    public void remove(String identificador) throws IOException{
        clienteDao.removerItemArquivo(identificador);
    }
}


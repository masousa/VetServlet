package br.com.letscode.service;

import br.com.letscode.dominio.Cliente;
import br.com.letscode.excecoes.UsuarioJaExisteException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ClienteService {

     Cliente inserir(Cliente cliente) throws IOException, UsuarioJaExisteException;

     List<Cliente> listAll() throws IOException;

     Optional<Cliente> findByCpf(String cpf) throws IOException;


    Cliente alterar(Cliente cliente, String identificador) throws IOException;

    void remove(String identificador) throws IOException;
}

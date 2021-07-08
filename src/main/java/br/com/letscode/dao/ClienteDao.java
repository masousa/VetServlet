package br.com.letscode.dao;

import br.com.letscode.dominio.Cliente;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ClienteDao {
    Cliente inserirNoArquivo(Cliente cliente) throws IOException;

    List<Cliente> getAll() throws IOException;

    Optional<Cliente> findByCpf(String cpf) throws IOException;

    Cliente alterarArquivo(Cliente cliente, String identificador) throws IOException;

    void removerItemArquivo(String identificador) throws IOException;
}

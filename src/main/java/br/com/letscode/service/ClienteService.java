package br.com.letscode.service;

import br.com.letscode.dominio.Cliente;

import java.io.IOException;

public interface ClienteService {

     Cliente inserir(Cliente cliente) throws IOException;

}

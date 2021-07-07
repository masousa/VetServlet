package br.com.letscode.dao;

import br.com.letscode.dominio.Cliente;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class CliendaDaoImpl implements ClienteDao{
    private String caminho = "/home/matheus/Documentos/clientes.csv";

    private Path path;
    @PostConstruct
    public void init(){
        path = Paths.get(caminho);
    }
    @Override
    public Cliente inserirArquivo(Cliente cliente) throws IOException{
        try(BufferedWriter bf = Files.newBufferedWriter(path)){
            bf.write(format(cliente));
        }
        return cliente;
    }

    @Override
    public List<Cliente> getAll() throws IOException{
        List<Cliente> clientes;
        try(BufferedReader br = Files.newBufferedReader(path)){
            clientes = br.lines().map(this::convert).collect(Collectors.toList());
            // List<String> identificadores = clientes.stream().map(Cliente::getIdentificador).collect(Collectors.toList());
        }
        return clientes;
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) throws IOException {
        List<Cliente> clientes = getAll();
        return clientes.stream().filter(cliente -> cliente.getCpf().equals(cpf)).findFirst();

    }

    private String format(Cliente cliente){
        return String.format("%s;%s;%s \r\n",cliente.getIdentificador(),cliente.getCpf(), cliente.getNome());
    }

    private Cliente convert(String linha){
        StringTokenizer token = new StringTokenizer(linha,";");
        Cliente cliente = new Cliente();
        cliente.setIdentificador(token.nextToken());
        cliente.setCpf(token.nextToken());
        cliente.setNome(token.nextToken());
        return cliente;
    }
}

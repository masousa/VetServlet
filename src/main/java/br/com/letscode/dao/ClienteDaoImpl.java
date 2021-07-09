package br.com.letscode.dao;

import br.com.letscode.dominio.Cliente;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClienteDaoImpl implements ClienteDao{
    private String caminho = "/arquivosLogicos"+ File.separator+"clientes.csv";

    private Path path;
    @PostConstruct
    public void init(){
        try {
            path = Paths.get(getClass().getResource(caminho).toURI());
            if (!path.toFile().exists()) {
                Files.createFile(path);
            }
        }catch (IOException | URISyntaxException ioException){
            ioException.printStackTrace();
        }
    }
    @Override
    public Cliente inserirNoArquivo(Cliente cliente) throws IOException{
        write(format(cliente), StandardOpenOption.APPEND);
        return cliente;
    }

    //escreve o arquivo ou adiciona um conteudo junto ao mesmo.
    private void write(String clienteStr, StandardOpenOption option) throws IOException {

        try(BufferedWriter bf = Files.newBufferedWriter(path, option)){
            bf.flush();
            bf.write(clienteStr);
        }
    }

    @Override
    //lista todos os elementos contidos no arquivo.
    public List<Cliente> getAll() throws IOException{
        List<Cliente> clientes;
        try(BufferedReader br = Files.newBufferedReader(path)){
            clientes = br.lines().filter(Objects::nonNull).filter(Predicate.not(String::isEmpty)).map(this::convert).collect(Collectors.toList());
        }
        return clientes;
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) throws IOException {
        List<Cliente> clientes = getAll();
        return clientes.stream().filter(cliente -> cliente.getCpf().equals(cpf)).findFirst();

    }

    @Override
    public Cliente alterarArquivo(Cliente cliente, String identificador ) throws IOException {

        List<Cliente> clientes = getAll();
        Optional<Cliente> optionalCliente = clientes.stream()
                .filter(clienteSearch -> clienteSearch.getIdentificador().equals(identificador)).findFirst();
        if(optionalCliente.isPresent()){
            System.out.println("CONTEUDO ENCONTRADO");
            optionalCliente.get().setNome(cliente.getNome());

            reescreverArquivo(clientes);
            return optionalCliente.get();
        }
        return cliente;



    }

    private void reescreverArquivo(List<Cliente> clientes) throws IOException {

        StringBuilder builder = new StringBuilder();
        for (Cliente clienteBuilder: clientes) {
            builder.append(format(clienteBuilder));
        }

        write(builder.toString(),StandardOpenOption.CREATE);
    }

    @Override
    public void removerItemArquivo(String identificador) throws IOException {
        List<Cliente> clientes = getAll();
        List<Cliente> clienteResultante = new ArrayList<>();
        for (Cliente cliente:clientes){
            if(!cliente.getIdentificador().equals(identificador)){
                clienteResultante.add(cliente);
            }
        }
        eraseContent();
        reescreverArquivo(clienteResultante);

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

    public void eraseContent() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path);
        writer.write("");
        writer.flush();
    }
}

package br.com.letscode;

import br.com.letscode.dominio.Cliente;
import br.com.letscode.dominio.CustomMessage;
import br.com.letscode.service.ClienteService;
import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@WebServlet(name = "clienteServlet" , urlPatterns = "/cliente")
public class ClienteServlet extends HttpServlet {

    public static final String CLIENTES_SESSION = "clientes";
    @Inject
    private ClienteService clienteService;

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader br = request.getReader();
        String line="";
        StringBuilder conteudo = new StringBuilder();
        Gson gson = new Gson();
        while(null!= (line= br.readLine())){
            conteudo.append(line);
        }
        Cliente clienteRequest = gson.fromJson(conteudo.toString(), Cliente.class);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter print = response.getWriter();
        String resposta = "";
        if(null==clienteRequest.getNome() || null==clienteRequest.getCpf()){
            CustomMessage message = new CustomMessage(HttpServletResponse.SC_BAD_REQUEST, "Invalid Parameters");
            response.setStatus(message.getStatus());
            resposta= gson.toJson(message);
        }else{


            HttpSession sessao = request.getSession(true);
            List<Cliente> clientes;
            if(null== (clientes= (List<Cliente>) sessao.getAttribute(CLIENTES_SESSION))){
                clientes = new ArrayList<>();
            }
            clienteService.inserir(clienteRequest);
            clientes.add(clienteRequest);
            sessao.setAttribute(CLIENTES_SESSION, clientes);

            resposta= gson.toJson(clientes);



        }
        print.write(resposta);
        print.close();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String cpfPesquisa =  request.getParameter("cpf");
        HttpSession sessao = request.getSession();
        List<Cliente> clientes = (List<Cliente>) sessao.getAttribute(CLIENTES_SESSION);
        Gson gson = new Gson();
        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if(null!=cpfPesquisa && Objects.nonNull(clientes)){
            Optional<Cliente> optionalCliente = clientes.stream().filter(cliente -> cliente.getCpf().equals(cpfPesquisa)).findFirst();
            if(optionalCliente.isPresent()){

                printWriter.write(gson.toJson(optionalCliente.get()));
            }else{

                CustomMessage message = new CustomMessage(404, "Conteúdo não encontrado");
                response.setStatus(404);
                printWriter.write(gson.toJson(message));
            }
        }else {

            printWriter.write(gson.toJson(clientes));

        }

        printWriter.close();
    }
}

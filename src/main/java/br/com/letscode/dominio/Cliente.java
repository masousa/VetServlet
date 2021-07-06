package br.com.letscode.dominio;

import lombok.Data;

@Data
public class Cliente {
    private String identificador;
    private String cpf;
    private String nome;
}

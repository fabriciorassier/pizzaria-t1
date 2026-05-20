package com.bcopstein.ex4_lancheriaddd_v1.application.dto.response;

public class LoginResponse {
    private String token;
    private String cpf;
    private String nome;
    private String mensagem;

    public LoginResponse(String token, String cpf, String nome, String mensagem) {
        this.token = token;
        this.cpf = cpf;
        this.nome = nome;
        this.mensagem = mensagem;
    }

    public String getToken() {
        return token;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getMensagem() {
        return mensagem;
    }
}

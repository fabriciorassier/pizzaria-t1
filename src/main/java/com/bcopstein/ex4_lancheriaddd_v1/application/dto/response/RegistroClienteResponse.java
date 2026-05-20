package com.bcopstein.ex4_lancheriaddd_v1.application.dto.response;

public class RegistroClienteResponse {
    private String mensagem;
    private String cpf;

    public RegistroClienteResponse(String mensagem, String cpf) {
        this.mensagem = mensagem;
        this.cpf = cpf;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getCpf() {
        return cpf;
    }
}

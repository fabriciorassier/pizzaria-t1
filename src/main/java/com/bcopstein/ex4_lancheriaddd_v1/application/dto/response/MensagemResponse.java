package com.bcopstein.ex4_lancheriaddd_v1.application.dto.response;

public class MensagemResponse {
    private String mensagem;

    public MensagemResponse(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}

package com.bcopstein.ex4_lancheriaddd_v1.application.dto.response;

public class CancelamentoPedidoResponse {
    private String mensagem;
    private long pedidoId;

    public CancelamentoPedidoResponse(String mensagem, long pedidoId) {
        this.mensagem = mensagem;
        this.pedidoId = pedidoId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public long getPedidoId() {
        return pedidoId;
    }
}

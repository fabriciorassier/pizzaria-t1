package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

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

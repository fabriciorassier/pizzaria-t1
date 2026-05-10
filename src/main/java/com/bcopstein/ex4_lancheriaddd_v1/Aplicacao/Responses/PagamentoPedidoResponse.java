package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public class PagamentoPedidoResponse {
    private long pedidoId;
    private String status;
    private String mensagem;
    private long numeroPedido;

    public PagamentoPedidoResponse(long pedidoId, String status, String mensagem, long numeroPedido) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.mensagem = mensagem;
        this.numeroPedido = numeroPedido;
    }

    public long getPedidoId() {
        return pedidoId;
    }

    public String getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public long getNumeroPedido() {
        return numeroPedido;
    }
}

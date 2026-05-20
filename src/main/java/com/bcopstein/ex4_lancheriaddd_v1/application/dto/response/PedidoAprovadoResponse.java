package com.bcopstein.ex4_lancheriaddd_v1.application.dto.response;

public class PedidoAprovadoResponse {
    private long pedidoId;
    private String status;
    private double custoItens;
    private double desconto;
    private double imposto;
    private double custoTotal;
    private String mensagem;

    public PedidoAprovadoResponse(long pedidoId, String status, double custoItens, double desconto,
            double imposto, double custoTotal, String mensagem) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.custoItens = custoItens;
        this.desconto = desconto;
        this.imposto = imposto;
        this.custoTotal = custoTotal;
        this.mensagem = mensagem;
    }

    public long getPedidoId() {
        return pedidoId;
    }

    public String getStatus() {
        return status;
    }

    public double getCustoItens() {
        return custoItens;
    }

    public double getDesconto() {
        return desconto;
    }

    public double getImposto() {
        return imposto;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public String getMensagem() {
        return mensagem;
    }
}

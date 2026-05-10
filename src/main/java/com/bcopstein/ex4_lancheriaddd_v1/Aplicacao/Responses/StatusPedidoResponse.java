package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;

public class StatusPedidoResponse {
    private long pedidoId;
    private String status;
    private LocalDateTime dataAtualizacao;
    private double custoItens;
    private double desconto;
    private double imposto;
    private double custoTotal;

    public StatusPedidoResponse(long pedidoId, String status, LocalDateTime dataAtualizacao,
            double custoItens, double desconto, double imposto, double custoTotal) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.dataAtualizacao = dataAtualizacao;
        this.custoItens = custoItens;
        this.desconto = desconto;
        this.imposto = imposto;
        this.custoTotal = custoTotal;
    }

    public long getPedidoId() {
        return pedidoId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
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
}

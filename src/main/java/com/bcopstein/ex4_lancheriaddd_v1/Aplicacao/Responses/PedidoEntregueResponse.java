package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;

public class PedidoEntregueResponse {
    private long pedidoId;
    private String clienteCpf;
    private LocalDateTime dataPedido;
    private double custoTotal;
    private String status;

    public PedidoEntregueResponse(long pedidoId, String clienteCpf, LocalDateTime dataPedido, double custoTotal, String status) {
        this.pedidoId = pedidoId;
        this.clienteCpf = clienteCpf;
        this.dataPedido = dataPedido;
        this.custoTotal = custoTotal;
        this.status = status;
    }

    public long getPedidoId() {
        return pedidoId;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public String getStatus() {
        return status;
    }
}

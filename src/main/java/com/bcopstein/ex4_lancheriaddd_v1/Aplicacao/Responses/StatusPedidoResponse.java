package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;

public class StatusPedidoResponse {
    private long pedidoId;
    private String status;
    private LocalDateTime dataAtualizacao;

    public StatusPedidoResponse(long pedidoId, String status, LocalDateTime dataAtualizacao) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.dataAtualizacao = dataAtualizacao;
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
}

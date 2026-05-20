package com.bcopstein.ex4_lancheriaddd_v1.domain.port.service;

public interface IPagamentoService {
    boolean processarPagamento(long pedidoId);
}

package com.bcopstein.ex4_lancheriaddd_v1.domain.port.service;

public interface IDescontoService {
    double calcularDesconto(String cpfCliente, double custoItens);
}

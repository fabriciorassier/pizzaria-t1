package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

public interface DescontoStrategy {
    String getTipo();

    double calcularDesconto(String cpfCliente, double custoItens);
}

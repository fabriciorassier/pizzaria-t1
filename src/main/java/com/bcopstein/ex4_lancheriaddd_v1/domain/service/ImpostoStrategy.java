package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

public interface ImpostoStrategy {
    String getTipo();

    double calcularImposto(double custoItens);
}

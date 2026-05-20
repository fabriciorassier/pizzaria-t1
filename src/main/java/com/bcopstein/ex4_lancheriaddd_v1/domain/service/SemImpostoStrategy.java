package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import org.springframework.stereotype.Component;

@Component
public class SemImpostoStrategy implements ImpostoStrategy {
    @Override
    public String getTipo() {
        return "nenhum";
    }

    @Override
    public double calcularImposto(double custoItens) {
        return 0.0;
    }
}

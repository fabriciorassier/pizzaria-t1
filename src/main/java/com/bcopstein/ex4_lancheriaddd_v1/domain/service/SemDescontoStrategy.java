package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import org.springframework.stereotype.Component;

@Component
public class SemDescontoStrategy implements DescontoStrategy {
    @Override
    public String getTipo() {
        return "nenhum";
    }

    @Override
    public double calcularDesconto(String cpfCliente, double custoItens) {
        return 0.0;
    }
}

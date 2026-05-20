package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaxaFixaImpostoStrategy implements ImpostoStrategy {
    private final PoliticaPrecoProperties politicaPrecoProperties;

    @Autowired
    public TaxaFixaImpostoStrategy(PoliticaPrecoProperties politicaPrecoProperties) {
        this.politicaPrecoProperties = politicaPrecoProperties;
    }

    @Override
    public String getTipo() {
        return "taxa-fixa";
    }

    @Override
    public double calcularImposto(double custoItens) {
        double taxa = politicaPrecoProperties.getTaxaImposto();
        if (taxa < 0 || taxa > 1) {
            throw new IllegalArgumentException("Taxa de imposto invalida: " + taxa);
        }
        return custoItens * taxa;
    }
}

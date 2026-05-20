package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "precos")
public class PoliticaPrecoProperties {
    private String estrategiaImposto = "taxa-fixa";
    private String estrategiaDesconto = "fidelidade";
    private double taxaImposto = 0.10;
    private double percentualDescontoFidelidade = 0.07;
    private int minimoPedidosFidelidade = 3;
    private int janelaDiasFidelidade = 20;

    public String getEstrategiaImposto() {
        return estrategiaImposto;
    }

    public void setEstrategiaImposto(String estrategiaImposto) {
        this.estrategiaImposto = estrategiaImposto;
    }

    public String getEstrategiaDesconto() {
        return estrategiaDesconto;
    }

    public void setEstrategiaDesconto(String estrategiaDesconto) {
        this.estrategiaDesconto = estrategiaDesconto;
    }

    public double getTaxaImposto() {
        return taxaImposto;
    }

    public void setTaxaImposto(double taxaImposto) {
        this.taxaImposto = taxaImposto;
    }

    public double getPercentualDescontoFidelidade() {
        return percentualDescontoFidelidade;
    }

    public void setPercentualDescontoFidelidade(double percentualDescontoFidelidade) {
        this.percentualDescontoFidelidade = percentualDescontoFidelidade;
    }

    public int getMinimoPedidosFidelidade() {
        return minimoPedidosFidelidade;
    }

    public void setMinimoPedidosFidelidade(int minimoPedidosFidelidade) {
        this.minimoPedidosFidelidade = minimoPedidosFidelidade;
    }

    public int getJanelaDiasFidelidade() {
        return janelaDiasFidelidade;
    }

    public void setJanelaDiasFidelidade(int janelaDiasFidelidade) {
        this.janelaDiasFidelidade = janelaDiasFidelidade;
    }
}

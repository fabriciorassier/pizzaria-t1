package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public class ProdutoCardapioResponse {
    private long id;
    private String descricao;
    private double preco;

    public ProdutoCardapioResponse(long id, String descricao, double preco) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
    }

    public long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }
}

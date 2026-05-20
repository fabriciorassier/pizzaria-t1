package com.bcopstein.ex4_lancheriaddd_v1.application.dto.response;

public class ProdutoCardapioResponse {
    private long id;
    private String descricao;
    private double preco;
    private boolean disponivel;

    public ProdutoCardapioResponse(long id, String descricao, double preco, boolean disponivel) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivel = disponivel;
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

    public boolean isDisponivel() {
        return disponivel;
    }
}

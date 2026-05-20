package com.bcopstein.ex4_lancheriaddd_v1.domain.model;

public class ItemPedido {
    private long id;
    private long pedidoId;
    private long produtoId;
    private String descricaoProduto;
    private int quantidade;
    private int precoUnitario;

    public ItemPedido(long id, long pedidoId, long produtoId, String descricaoProduto, int quantidade, int precoUnitario) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.descricaoProduto = descricaoProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public ItemPedido(long produtoId, String descricaoProduto, int quantidade, int precoUnitario) {
        this(0L, 0L, produtoId, descricaoProduto, quantidade, precoUnitario);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(long produtoId) {
        this.produtoId = produtoId;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(int precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}

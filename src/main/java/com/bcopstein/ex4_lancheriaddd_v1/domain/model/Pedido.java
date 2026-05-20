package com.bcopstein.ex4_lancheriaddd_v1.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {
    private long id;
    private String clienteCpf;
    private String enderecoEntrega;
    private LocalDateTime dataPedido;
    private List<ItemPedido> itens;
    private String status;
    private double custoItens;
    private double desconto;
    private double imposto;
    private double custoTotal;

    public Pedido(long id, String clienteCpf, String status, String enderecoEntrega, LocalDateTime dataPedido,
            List<ItemPedido> itens, double custoItens, double desconto, double imposto, double custoTotal) {
        this.id = id;
        this.clienteCpf = clienteCpf;
        this.status = status;
        this.enderecoEntrega = enderecoEntrega;
        this.dataPedido = dataPedido;
        this.itens = itens;
        this.custoItens = custoItens;
        this.desconto = desconto;
        this.imposto = imposto;
        this.custoTotal = custoTotal;
    }

    public Pedido(String clienteCpf, String status, String enderecoEntrega, LocalDateTime dataPedido, List<ItemPedido> itens) {
        this(0L, clienteCpf, status, enderecoEntrega, dataPedido, itens, 0.0, 0.0, 0.0, 0.0);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCustoItens() {
        return custoItens;
    }

    public void setCustoItens(double custoItens) {
        this.custoItens = custoItens;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getImposto() {
        return imposto;
    }

    public void setImposto(double imposto) {
        this.imposto = imposto;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(double custoTotal) {
        this.custoTotal = custoTotal;
    }
}

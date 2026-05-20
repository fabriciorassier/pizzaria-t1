package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import java.util.List;

public class EstoqueInsuficienteException extends IllegalArgumentException {
    private List<String> detalhes;

    public EstoqueInsuficienteException(String mensagem, List<String> detalhes) {
        super(mensagem);
        this.detalhes = detalhes;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }
}

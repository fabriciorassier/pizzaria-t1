package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.ProdutoCardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CardapioService;

@Component
public class CarregarCardapioUC {
    private CardapioService cardapioService;

    @Autowired
    public CarregarCardapioUC(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    public List<ProdutoCardapioResponse> run() {
        Cardapio cardapio = cardapioService.recuperaCardapioCorrente();
        if (cardapio == null) {
            return List.of();
        }
        return cardapio.getProdutos().stream()
                .map(this::converter)
                .toList();
    }

    private ProdutoCardapioResponse converter(Produto produto) {
        return new ProdutoCardapioResponse(produto.getId(), produto.getDescricao(), produto.getPreco() / 100.0);
    }
}

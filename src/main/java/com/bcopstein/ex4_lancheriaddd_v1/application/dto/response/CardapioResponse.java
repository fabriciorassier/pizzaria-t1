package com.bcopstein.ex4_lancheriaddd_v1.application.dto.response;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Produto;

public class CardapioResponse {
    private Cardapio cardapio;
    private List<Produto> sugestoesDoChef;

    public CardapioResponse(Cardapio cardapio, List<Produto> sugestoesDoChef) {
        this.cardapio = cardapio;
        this.sugestoesDoChef = sugestoesDoChef;
    }

    public Cardapio getCardapio() {
        return cardapio;
    }

    public List<Produto> getSugestoesDoChef() {
        return sugestoesDoChef;
    }
}

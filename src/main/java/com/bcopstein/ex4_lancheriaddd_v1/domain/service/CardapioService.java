package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.CabecalhoCardapio;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Produto;

@Service
public class CardapioService {
    private CardapioRepository cardapioRepository;

    @Autowired
    public CardapioService(CardapioRepository cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    public Cardapio recuperaCardapio(long Id) {
        return cardapioRepository.recuperaPorId(Id);
    }

    public Cardapio recuperaCardapioCorrente() {
        return cardapioRepository.recuperaCorrente();
    }

    public List<CabecalhoCardapio> recuperaListaDeCardapios() {
        return cardapioRepository.cardapiosDisponiveis();
    }

    public List<Produto> recuperaSugestoesDoChef() {
        return cardapioRepository.indicacoesDoChef();
    }
}

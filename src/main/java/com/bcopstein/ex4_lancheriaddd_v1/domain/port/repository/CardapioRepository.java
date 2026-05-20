package com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.domain.model.CabecalhoCardapio;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Produto;

public interface CardapioRepository {
    List<CabecalhoCardapio> cardapiosDisponiveis();
    Cardapio recuperaPorId(long id);
    Cardapio recuperaCorrente();
    List<Produto> indicacoesDoChef();
}

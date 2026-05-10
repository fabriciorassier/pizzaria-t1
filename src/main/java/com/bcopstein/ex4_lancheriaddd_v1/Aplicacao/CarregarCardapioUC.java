package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.ProdutoCardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CardapioService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IEstoqueService;

@Component
public class CarregarCardapioUC {
    private CardapioService cardapioService;
    private IEstoqueService estoqueService;

    @Autowired
    public CarregarCardapioUC(CardapioService cardapioService, IEstoqueService estoqueService) {
        this.cardapioService = cardapioService;
        this.estoqueService = estoqueService;
    }

    public List<ProdutoCardapioResponse> run() {
        Cardapio cardapio = cardapioService.recuperaCardapioCorrente();
        if (cardapio == null) {
            return List.of();
        }
        List<Produto> produtos = cardapio.getProdutos();
        Set<Long> indisponiveis = estoqueService.identificarProdutosIndisponiveis(produtos);
        return produtos.stream()
                .map(produto -> converter(produto, !indisponiveis.contains(produto.getId())))
                .toList();
    }

    private ProdutoCardapioResponse converter(Produto produto, boolean disponivel) {
        return new ProdutoCardapioResponse(produto.getId(), produto.getDescricao(), produto.getPreco() / 100.0, disponivel);
    }
}

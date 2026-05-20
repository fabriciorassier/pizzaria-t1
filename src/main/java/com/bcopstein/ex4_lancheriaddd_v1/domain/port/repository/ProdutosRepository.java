package com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Produto;

public interface ProdutosRepository {
    Produto recuperaProdutoPorid(long id);
    List<Produto> recuperaProdutosCardapio(long id);
}

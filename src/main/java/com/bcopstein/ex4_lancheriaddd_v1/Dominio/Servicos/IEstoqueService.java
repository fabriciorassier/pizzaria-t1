package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import java.util.Set;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

public interface IEstoqueService {
    boolean verificarEstoque(List<ItemPedido> itens);
    List<String> detalharFaltas(List<ItemPedido> itens);
    void baixarEstoque(List<ItemPedido> itens);
    Set<Long> identificarProdutosIndisponiveis(List<Produto> produtos);
}

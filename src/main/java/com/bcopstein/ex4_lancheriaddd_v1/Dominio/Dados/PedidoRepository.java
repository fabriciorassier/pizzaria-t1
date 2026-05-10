package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDate;
import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoRepository {
    long salvar(Pedido pedido);
    void salvarItens(long pedidoId, List<ItemPedido> itens);
    void atualizarStatus(long pedidoId, String novoStatus);
    void atualizarCustos(long pedidoId, double custoItens, double desconto, double imposto, double custoTotal);
    Pedido buscarPorId(long id);
    int contarPedidosClienteUltimos20Dias(String cpf);
    List<Pedido> buscarEntreguesEntreDatas(LocalDate inicio, LocalDate fim);
    List<Pedido> buscarDoClienteEntreguesEntreDatas(String cpf, LocalDate inicio, LocalDate fim);
}

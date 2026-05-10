package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoAprovadoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class SubmeterPedidoUC {
    private PedidoService pedidoService;

    @Autowired
    public SubmeterPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public PedidoAprovadoResponse run(String cpfCliente, SubmeterPedidoRequest request) {
        if (request == null || request.getItens() == null || request.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido precisa ter itens");
        }

        List<ItemPedido> itens = request.getItens().stream()
                .map(this::mapearItem)
                .toList();

        Pedido pedido = pedidoService.submeterPedido(cpfCliente, request.getEnderecoEntrega(), itens);
        return new PedidoAprovadoResponse(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getCustoItens(),
                pedido.getDesconto(),
                pedido.getImposto(),
                pedido.getCustoTotal(),
                "Pedido aprovado!");
    }

    private ItemPedido mapearItem(ItemPedidoRequest itemRequest) {
        return pedidoService.criarItemPedido(itemRequest.getProdutoId(), itemRequest.getQuantidade());
    }
}

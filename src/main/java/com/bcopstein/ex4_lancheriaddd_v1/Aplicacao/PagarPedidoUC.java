package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class PagarPedidoUC {
    private PedidoService pedidoService;

    @Autowired
    public PagarPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public PagamentoPedidoResponse run(long pedidoId) {
        Pedido pedido = pedidoService.pagarPedido(pedidoId);
        return new PagamentoPedidoResponse(
                pedido.getId(),
                pedido.getStatus(),
                "Pagamento realizado! Acompanhe seu pedido pelo numero " + pedido.getId() + ".",
                pedido.getId());
    }
}

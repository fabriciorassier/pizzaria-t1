package com.bcopstein.ex4_lancheriaddd_v1.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.PagamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.service.PedidoService;

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

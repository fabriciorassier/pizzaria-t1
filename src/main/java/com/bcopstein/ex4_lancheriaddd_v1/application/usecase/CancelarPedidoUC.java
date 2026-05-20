package com.bcopstein.ex4_lancheriaddd_v1.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.CancelamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.service.PedidoService;

@Component
public class CancelarPedidoUC {
    private PedidoService pedidoService;

    @Autowired
    public CancelarPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public CancelamentoPedidoResponse run(long pedidoId) {
        Pedido pedido = pedidoService.cancelarPedido(pedidoId);
        return new CancelamentoPedidoResponse("Pedido cancelado com sucesso", pedido.getId());
    }
}

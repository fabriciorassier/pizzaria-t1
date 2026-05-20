package com.bcopstein.ex4_lancheriaddd_v1.application.usecase;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.StatusPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.service.PedidoService;

@Component
public class ConsultarStatusPedidoUC {
    private PedidoService pedidoService;

    @Autowired
    public ConsultarStatusPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public StatusPedidoResponse run(long pedidoId) {
        Pedido pedido = pedidoService.consultarPedido(pedidoId);
        if (pedido == null) {
            return null;
        }
        return new StatusPedidoResponse(
            pedido.getId(),
            pedido.getStatus(),
            LocalDateTime.now(),
            pedido.getCustoItens(),
            pedido.getDesconto(),
            pedido.getImposto(),
            pedido.getCustoTotal());
    }
}

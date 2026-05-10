package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

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

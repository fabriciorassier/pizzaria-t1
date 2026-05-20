package com.bcopstein.ex4_lancheriaddd_v1.adapters.outbound.fake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.StatusPedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.IEntregaService;

@Service
public class EntregaServiceSimulado implements IEntregaService {
    private PedidoRepository pedidoRepository;

    @Autowired
    public EntregaServiceSimulado(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void processarEntrega(long pedidoId) {
        Thread thread = new Thread(() -> {
            try {
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.TRANSPORTE.name());
                Thread.sleep(5000);
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.ENTREGUE.name());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
    }
}

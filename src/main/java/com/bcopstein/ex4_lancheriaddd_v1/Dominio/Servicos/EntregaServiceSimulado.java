package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.StatusPedido;

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
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.TRANSPORTE);
                Thread.sleep(5000);
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.ENTREGUE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
    }
}

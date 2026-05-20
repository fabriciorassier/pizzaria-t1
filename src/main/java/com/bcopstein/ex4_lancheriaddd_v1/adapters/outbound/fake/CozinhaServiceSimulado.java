package com.bcopstein.ex4_lancheriaddd_v1.adapters.outbound.fake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.StatusPedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.ICozinhaService;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.IEntregaService;

@Service
public class CozinhaServiceSimulado implements ICozinhaService {
    private PedidoRepository pedidoRepository;
    private IEntregaService entregaService;

    @Autowired
    public CozinhaServiceSimulado(PedidoRepository pedidoRepository, IEntregaService entregaService) {
        this.pedidoRepository = pedidoRepository;
        this.entregaService = entregaService;
    }

    @Override
    public void processarPedido(long pedidoId) {
        /* A simulacao roda em background para nao bloquear o endpoint de pagamento. */
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.AGUARDANDO.name());
                Thread.sleep(5000);
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.PREPARACAO.name());
                Thread.sleep(5000);
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.PRONTO.name());
                entregaService.processarEntrega(pedidoId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
    }
}

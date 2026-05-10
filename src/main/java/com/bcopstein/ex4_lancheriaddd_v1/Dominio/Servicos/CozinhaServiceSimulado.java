package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.StatusPedido;

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
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.AGUARDANDO);
                Thread.sleep(3000);
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.PREPARACAO);
                Thread.sleep(5000);
                pedidoRepository.atualizarStatus(pedidoId, StatusPedido.PRONTO);
                entregaService.processarEntrega(pedidoId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
    }
}

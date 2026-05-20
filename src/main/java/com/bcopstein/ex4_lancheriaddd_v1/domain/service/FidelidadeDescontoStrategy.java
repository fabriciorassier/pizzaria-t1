package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.PedidoRepository;

@Component
public class FidelidadeDescontoStrategy implements DescontoStrategy {
    private final PedidoRepository pedidoRepository;
    private final PoliticaPrecoProperties politicaPrecoProperties;

    @Autowired
    public FidelidadeDescontoStrategy(PedidoRepository pedidoRepository, PoliticaPrecoProperties politicaPrecoProperties) {
        this.pedidoRepository = pedidoRepository;
        this.politicaPrecoProperties = politicaPrecoProperties;
    }

    @Override
    public String getTipo() {
        return "fidelidade";
    }

    @Override
    public double calcularDesconto(String cpfCliente, double custoItens) {
        validarPolitica();

        int totalPedidos = pedidoRepository.contarPedidosClienteUltimosNDias(
                cpfCliente,
                politicaPrecoProperties.getJanelaDiasFidelidade());

        if (totalPedidos > politicaPrecoProperties.getMinimoPedidosFidelidade()) {
            return custoItens * politicaPrecoProperties.getPercentualDescontoFidelidade();
        }
        return 0.0;
    }

    private void validarPolitica() {
        double percentual = politicaPrecoProperties.getPercentualDescontoFidelidade();
        int minimoPedidos = politicaPrecoProperties.getMinimoPedidosFidelidade();
        int janelaDias = politicaPrecoProperties.getJanelaDiasFidelidade();

        if (percentual < 0 || percentual > 1) {
            throw new IllegalArgumentException("Percentual de desconto invalido: " + percentual);
        }
        if (minimoPedidos < 0) {
            throw new IllegalArgumentException("Minimo de pedidos invalido: " + minimoPedidos);
        }
        if (janelaDias <= 0) {
            throw new IllegalArgumentException("Janela de dias invalida: " + janelaDias);
        }
    }
}

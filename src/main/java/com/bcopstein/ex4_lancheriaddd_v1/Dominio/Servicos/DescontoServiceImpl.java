package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;

@Service
public class DescontoServiceImpl implements IDescontoService {
    private static final double PERCENTUAL_DESCONTO = 0.07;
    private static final int MINIMO_PEDIDOS = 3;

    private PedidoRepository pedidoRepository;

    @Autowired
    public DescontoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public double calcularDesconto(String cpfCliente, double custoItens) {
        int totalPedidos = pedidoRepository.contarPedidosClienteUltimos20Dias(cpfCliente);
        if (totalPedidos > MINIMO_PEDIDOS) {
            return custoItens * PERCENTUAL_DESCONTO;
        }
        return 0.0;
    }
}

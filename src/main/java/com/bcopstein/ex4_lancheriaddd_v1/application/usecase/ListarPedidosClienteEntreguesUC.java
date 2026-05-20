package com.bcopstein.ex4_lancheriaddd_v1.application.usecase;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.PedidoEntregueResponse;
import com.bcopstein.ex4_lancheriaddd_v1.domain.service.PedidoService;

@Component
public class ListarPedidosClienteEntreguesUC {
    private PedidoService pedidoService;

    @Autowired
    public ListarPedidosClienteEntreguesUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public List<PedidoEntregueResponse> run(String cpf, LocalDate inicio, LocalDate fim) {
        return pedidoService.listarEntreguesDoCliente(cpf, inicio, fim).stream()
                .map(p -> new PedidoEntregueResponse(p.getId(), p.getClienteCpf(), p.getDataPedido(), p.getCustoTotal(), p.getStatus()))
                .toList();
    }
}

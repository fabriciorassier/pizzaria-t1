package com.bcopstein.ex4_lancheriaddd_v1.adapters.outbound.fake;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.IPagamentoService;

@Service
public class PagamentoServiceFake implements IPagamentoService {
    @Override
    public boolean processarPagamento(long pedidoId) {
        return true;
    }
}

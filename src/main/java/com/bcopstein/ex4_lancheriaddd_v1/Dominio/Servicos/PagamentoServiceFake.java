package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class PagamentoServiceFake implements IPagamentoService {
    @Override
    public boolean processarPagamento(long pedidoId) {
        return true;
    }
}

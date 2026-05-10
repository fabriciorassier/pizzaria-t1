package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class ImpostoServiceImpl implements IImpostoService {
    private static final double TAXA_IMPOSTO = 0.10;

    @Override
    public double calcularImposto(double custoItens) {
        return custoItens * TAXA_IMPOSTO;
    }
}

package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.IImpostoService;

@Service
public class ImpostoServiceImpl implements IImpostoService {
    private final Map<String, ImpostoStrategy> estrategias;
    private final PoliticaPrecoProperties politicaPrecoProperties;

    @Autowired
    public ImpostoServiceImpl(List<ImpostoStrategy> estrategias, PoliticaPrecoProperties politicaPrecoProperties) {
        this.estrategias = estrategias.stream()
                .collect(Collectors.toMap(ImpostoStrategy::getTipo, Function.identity()));
        this.politicaPrecoProperties = politicaPrecoProperties;
    }

    @Override
    public double calcularImposto(double custoItens) {
        String tipo = politicaPrecoProperties.getEstrategiaImposto();
        ImpostoStrategy strategy = estrategias.get(tipo);
        if (strategy == null) {
            throw new IllegalArgumentException("Estrategia de imposto nao suportada: " + tipo);
        }
        return strategy.calcularImposto(custoItens);
    }
}

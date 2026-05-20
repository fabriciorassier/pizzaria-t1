package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.IDescontoService;

@Service
public class DescontoServiceImpl implements IDescontoService {
    private final Map<String, DescontoStrategy> estrategias;
    private final PoliticaPrecoProperties politicaPrecoProperties;

    @Autowired
    public DescontoServiceImpl(List<DescontoStrategy> estrategias, PoliticaPrecoProperties politicaPrecoProperties) {
        this.estrategias = estrategias.stream()
                .collect(Collectors.toMap(DescontoStrategy::getTipo, Function.identity()));
        this.politicaPrecoProperties = politicaPrecoProperties;
    }

    @Override
    public double calcularDesconto(String cpfCliente, double custoItens) {
        String tipo = politicaPrecoProperties.getEstrategiaDesconto();
        DescontoStrategy strategy = estrategias.get(tipo);
        if (strategy == null) {
            throw new IllegalArgumentException("Estrategia de desconto nao suportada: " + tipo);
        }
        return strategy.calcularDesconto(cpfCliente, custoItens);
    }
}

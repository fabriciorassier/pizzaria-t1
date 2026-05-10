package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;

@Service
public class EstoqueServiceFake implements IEstoqueService {
    private JdbcTemplate jdbcTemplate;
    private ProdutosRepository produtosRepository;

    @Autowired
    public EstoqueServiceFake(JdbcTemplate jdbcTemplate, ProdutosRepository produtosRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.produtosRepository = produtosRepository;
    }

    @Override
    public boolean verificarEstoque(List<ItemPedido> itens) {
        return detalharFaltas(itens).isEmpty();
    }

    @Override
    public List<String> detalharFaltas(List<ItemPedido> itens) {
        Map<Long, Integer> estoqueAtual = carregarEstoqueAtual();
        List<String> faltas = new ArrayList<>();

        for (ItemPedido item : itens) {
            Produto produto = produtosRepository.recuperaProdutoPorid(item.getProdutoId());
            if (produto == null) {
                faltas.add("Produto #" + item.getProdutoId() + " nao encontrado.");
                continue;
            }

            Receita receita = produto.getReceita();
            if (receita == null || receita.getIngredientes() == null || receita.getIngredientes().isEmpty()) {
                faltas.add(produto.getDescricao() + " sem receita cadastrada.");
                continue;
            }

            Map<Long, Long> necessidadePorIngrediente = receita.getIngredientes().stream()
                    .collect(Collectors.groupingBy(Ingrediente::getId, Collectors.counting()));

            List<String> ingredientesEmFalta = new ArrayList<>();
            for (Ingrediente ingrediente : receita.getIngredientes()) {
                long ingredienteId = ingrediente.getId();
                int porcoesNecessarias = Math.toIntExact(necessidadePorIngrediente.get(ingredienteId)) * item.getQuantidade();
                int porcoesDisponiveis = estoqueAtual.getOrDefault(ingredienteId, 0);
                if (porcoesDisponiveis < porcoesNecessarias) {
                    String detalhe = ingrediente.getDescricao() + " (necessario " + porcoesNecessarias
                            + ", disponivel " + porcoesDisponiveis + ")";
                    if (!ingredientesEmFalta.contains(detalhe)) {
                        ingredientesEmFalta.add(detalhe);
                    }
                }
            }

            if (!ingredientesEmFalta.isEmpty()) {
                faltas.add("" + produto.getDescricao() + " x" + item.getQuantidade() + ": " + String.join(", ", ingredientesEmFalta));
            }
        }

        return faltas;
    }

    @Override
    public void baixarEstoque(List<ItemPedido> itens) {
        Map<Long, Integer> consumoPorIngrediente = new HashMap<>();

        for (ItemPedido item : itens) {
            Produto produto = produtosRepository.recuperaProdutoPorid(item.getProdutoId());
            if (produto == null || produto.getReceita() == null || produto.getReceita().getIngredientes() == null) {
                continue;
            }
            for (Ingrediente ingrediente : produto.getReceita().getIngredientes()) {
                long ingredienteId = ingrediente.getId();
                int atual = consumoPorIngrediente.getOrDefault(ingredienteId, 0);
                consumoPorIngrediente.put(ingredienteId, atual + item.getQuantidade());
            }
        }

        for (Map.Entry<Long, Integer> entry : consumoPorIngrediente.entrySet()) {
            jdbcTemplate.update(
                    "UPDATE itensEstoque SET quantidade = quantidade - ? WHERE ingrediente_id = ?",
                    entry.getValue(),
                    entry.getKey());
        }
    }

    @Override
    public Set<Long> identificarProdutosIndisponiveis(List<Produto> produtos) {
        Map<Long, Integer> estoqueAtual = carregarEstoqueAtual();
        return produtos.stream()
                .filter(produto -> produto == null
                        || produto.getReceita() == null
                        || produto.getReceita().getIngredientes() == null
                        || produto.getReceita().getIngredientes().isEmpty()
                        || produto.getReceita().getIngredientes().stream()
                                .map(Ingrediente::getId)
                                .anyMatch(ingredienteId -> estoqueAtual.getOrDefault(ingredienteId, 0) < 1))
                .map(Produto::getId)
                .collect(Collectors.toSet());
    }

    private Map<Long, Integer> carregarEstoqueAtual() {
        String sql = "SELECT ingrediente_id, quantidade FROM itensEstoque";
        return jdbcTemplate.query(sql, rs -> {
            Map<Long, Integer> estoque = new HashMap<>();
            while (rs.next()) {
                estoque.put(rs.getLong("ingrediente_id"), rs.getInt("quantidade"));
            }
            return estoque;
        });
    }
}

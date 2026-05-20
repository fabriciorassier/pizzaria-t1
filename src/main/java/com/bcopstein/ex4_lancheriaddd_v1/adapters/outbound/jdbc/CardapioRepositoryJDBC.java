package com.bcopstein.ex4_lancheriaddd_v1.adapters.outbound.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.CabecalhoCardapio;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Produto;

@Component
public class CardapioRepositoryJDBC implements CardapioRepository {
    private JdbcTemplate jdbcTemplate;
    private ProdutosRepository produtosRepository;

    @Autowired
    public CardapioRepositoryJDBC(JdbcTemplate jdbcTemplate, ProdutosRepository produtosRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.produtosRepository = produtosRepository;
    }

    @Override
    public Cardapio recuperaPorId(long id) {
        String sql = "SELECT id, titulo FROM cardapios WHERE id = ?";
        List<Cardapio> cardapios = this.jdbcTemplate.query(
            sql,
            ps -> ps.setLong(1, id),
            (rs, rowNum) -> new Cardapio(new CabecalhoCardapio(rs.getLong("id"), rs.getString("titulo")), null)
        );
        if (cardapios.isEmpty()) {
            return null;
        }
        Cardapio cardapio = cardapios.get(0);
        List<Produto> produtos = produtosRepository.recuperaProdutosCardapio(id);
        cardapio.setProdutos(produtos);
        return cardapio;
    }

    @Override
    public Cardapio recuperaCorrente() {
        String sql = "SELECT cardapio_id FROM cardapio_corrente WHERE id = 1";
        List<Long> ids = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("cardapio_id"));
        if (ids.isEmpty()) {
            return null;
        }
        return recuperaPorId(ids.get(0));
    }

    @Override
    public List<Produto> indicacoesDoChef() {
        return List.of(produtosRepository.recuperaProdutoPorid(2L));
    }

    @Override
    public List<CabecalhoCardapio> cardapiosDisponiveis() {
        String sql = "SELECT id, titulo FROM cardapios";
        List<CabecalhoCardapio> cabCardapios = this.jdbcTemplate.query(
            sql,
            ps -> {},
            (rs, rowNum) -> new CabecalhoCardapio(rs.getLong("id"), rs.getString("titulo"))
        );
        return cabCardapios;
    }
}

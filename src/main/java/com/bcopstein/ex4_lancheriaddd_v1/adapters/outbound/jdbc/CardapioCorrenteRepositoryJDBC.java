package com.bcopstein.ex4_lancheriaddd_v1.adapters.outbound.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.CardapioCorrenteRepository;

@Component
public class CardapioCorrenteRepositoryJDBC implements CardapioCorrenteRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CardapioCorrenteRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long getIdCardapioCorrente() {
        List<Long> ids = jdbcTemplate.query("SELECT cardapio_id FROM cardapio_corrente WHERE id = 1",
                (rs, rowNum) -> rs.getLong("cardapio_id"));
        return ids.isEmpty() ? 0L : ids.get(0);
    }
}

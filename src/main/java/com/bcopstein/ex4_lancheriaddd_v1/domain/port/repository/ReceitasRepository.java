package com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository;

import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Receita;

public interface ReceitasRepository {
    Receita recuperaReceita(long id);
}

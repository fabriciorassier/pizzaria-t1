package com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Ingrediente;

public interface IngredientesRepository {
    List<Ingrediente> recuperaTodos();
    List<Ingrediente> recuperaIngredientesReceita(long id);
}

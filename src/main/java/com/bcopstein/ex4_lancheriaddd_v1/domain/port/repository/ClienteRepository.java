package com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository;

import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Cliente;

public interface ClienteRepository {
    void salvar(Cliente cliente);
    Cliente buscarPorEmail(String email);
    Cliente buscarPorCpf(String cpf);
    boolean existeEmail(String email);
}

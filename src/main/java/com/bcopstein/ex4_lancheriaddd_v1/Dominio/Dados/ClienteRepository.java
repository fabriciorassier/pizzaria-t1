package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface ClienteRepository {
    void salvar(Cliente cliente);
    Cliente buscarPorEmail(String email);
    Cliente buscarPorCpf(String cpf);
    boolean existeEmail(String email);
}

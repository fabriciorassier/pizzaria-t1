package com.bcopstein.ex4_lancheriaddd_v1.adapters.outbound.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Cliente;

@Component
public class ClienteRepositoryJDBC implements ClienteRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ClienteRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO clientes(cpf, nome, celular, endereco, email, senha) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getCelular(),
                cliente.getEndereco(),
                cliente.getEmail(),
                cliente.getSenha());
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        String sql = "SELECT cpf, nome, celular, endereco, email, senha FROM clientes WHERE email = ?";
        List<Cliente> clientes = jdbcTemplate.query(sql,
                ps -> ps.setString(1, email),
                (rs, rowNum) -> new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("celular"),
                        rs.getString("endereco"),
                        rs.getString("email"),
                        rs.getString("senha")));
        return clientes.isEmpty() ? null : clientes.get(0);
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        String sql = "SELECT cpf, nome, celular, endereco, email, senha FROM clientes WHERE cpf = ?";
        List<Cliente> clientes = jdbcTemplate.query(sql,
                ps -> ps.setString(1, cpf),
                (rs, rowNum) -> new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("celular"),
                        rs.getString("endereco"),
                        rs.getString("email"),
                        rs.getString("senha")));
        return clientes.isEmpty() ? null : clientes.get(0);
    }

    @Override
    public boolean existeEmail(String email) {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM clientes WHERE email = ?", Integer.class, email);
        return total != null && total > 0;
    }
}

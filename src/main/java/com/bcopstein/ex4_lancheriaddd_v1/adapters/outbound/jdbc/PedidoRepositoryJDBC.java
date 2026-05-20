package com.bcopstein.ex4_lancheriaddd_v1.adapters.outbound.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.StatusPedido;

@Component
public class PedidoRepositoryJDBC implements PedidoRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long salvar(Pedido pedido) {
        String sql = "INSERT INTO pedidos(cliente_cpf, status, endereco_entrega, data_pedido, custo_itens, desconto, imposto, custo_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pedido.getClienteCpf());
            ps.setString(2, pedido.getStatus());
            ps.setString(3, pedido.getEnderecoEntrega());
            ps.setTimestamp(4, Timestamp.valueOf(pedido.getDataPedido()));
            ps.setDouble(5, pedido.getCustoItens());
            ps.setDouble(6, pedido.getDesconto());
            ps.setDouble(7, pedido.getImposto());
            ps.setDouble(8, pedido.getCustoTotal());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key == null ? 0L : key.longValue();
    }

    @Override
    public void salvarItens(long pedidoId, List<ItemPedido> itens) {
        String sql = "INSERT INTO itens_pedido(pedido_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        for (ItemPedido item : itens) {
            jdbcTemplate.update(sql, pedidoId, item.getProdutoId(), item.getQuantidade(), item.getPrecoUnitario());
        }
    }

    @Override
    public void atualizarStatus(long pedidoId, String novoStatus) {
        jdbcTemplate.update("UPDATE pedidos SET status = ? WHERE id = ?", novoStatus, pedidoId);
    }

    @Override
    public void atualizarCustos(long pedidoId, double custoItens, double desconto, double imposto, double custoTotal) {
        String sql = "UPDATE pedidos SET custo_itens = ?, desconto = ?, imposto = ?, custo_total = ? WHERE id = ?";
        jdbcTemplate.update(sql, custoItens, desconto, imposto, custoTotal, pedidoId);
    }

    @Override
    public Pedido buscarPorId(long id) {
        String sqlPedido = "SELECT id, cliente_cpf, status, endereco_entrega, data_pedido, custo_itens, desconto, imposto, custo_total FROM pedidos WHERE id = ?";
        List<Pedido> pedidos = jdbcTemplate.query(sqlPedido,
                ps -> ps.setLong(1, id),
                (rs, rowNum) -> new Pedido(
                        rs.getLong("id"),
                        rs.getString("cliente_cpf"),
                        rs.getString("status"),
                        rs.getString("endereco_entrega"),
                        rs.getTimestamp("data_pedido").toLocalDateTime(),
                        List.of(),
                        rs.getDouble("custo_itens"),
                        rs.getDouble("desconto"),
                        rs.getDouble("imposto"),
                        rs.getDouble("custo_total")));
        if (pedidos.isEmpty()) {
            return null;
        }
        Pedido pedido = pedidos.get(0);
        pedido.setItens(buscarItensPedido(pedido.getId()));
        return pedido;
    }

    @Override
    public int contarPedidosClienteUltimosNDias(String cpf, int dias) {
        LocalDateTime limite = LocalDateTime.now().minusDays(dias);
        String sql = "SELECT COUNT(*) FROM pedidos WHERE cliente_cpf = ? AND data_pedido >= ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, cpf, Timestamp.valueOf(limite));
        return total == null ? 0 : total;
    }

    @Override
    public List<Pedido> buscarEntreguesEntreDatas(LocalDate inicio, LocalDate fim) {
        String sql = "SELECT id, cliente_cpf, status, endereco_entrega, data_pedido, custo_itens, desconto, imposto, custo_total "
                + "FROM pedidos WHERE status = ? AND data_pedido >= ? AND data_pedido < ? ORDER BY data_pedido";
        LocalDateTime inicioDia = inicio.atStartOfDay();
        LocalDateTime fimExclusivo = fim.plusDays(1).atStartOfDay();
        return jdbcTemplate.query(sql,
                ps -> {
                    ps.setString(1, StatusPedido.ENTREGUE.name());
                    ps.setTimestamp(2, Timestamp.valueOf(inicioDia));
                    ps.setTimestamp(3, Timestamp.valueOf(fimExclusivo));
                },
                (rs, rowNum) -> new Pedido(
                        rs.getLong("id"),
                        rs.getString("cliente_cpf"),
                        rs.getString("status"),
                        rs.getString("endereco_entrega"),
                        rs.getTimestamp("data_pedido").toLocalDateTime(),
                        List.of(),
                        rs.getDouble("custo_itens"),
                        rs.getDouble("desconto"),
                        rs.getDouble("imposto"),
                        rs.getDouble("custo_total")));
    }

    @Override
    public List<Pedido> buscarDoClienteEntreguesEntreDatas(String cpf, LocalDate inicio, LocalDate fim) {
        String sql = "SELECT id, cliente_cpf, status, endereco_entrega, data_pedido, custo_itens, desconto, imposto, custo_total "
                + "FROM pedidos WHERE status = ? AND cliente_cpf = ? AND data_pedido >= ? AND data_pedido < ? ORDER BY data_pedido";
        LocalDateTime inicioDia = inicio.atStartOfDay();
        LocalDateTime fimExclusivo = fim.plusDays(1).atStartOfDay();
        return jdbcTemplate.query(sql,
                ps -> {
                    ps.setString(1, StatusPedido.ENTREGUE.name());
                    ps.setString(2, cpf);
                    ps.setTimestamp(3, Timestamp.valueOf(inicioDia));
                    ps.setTimestamp(4, Timestamp.valueOf(fimExclusivo));
                },
                (rs, rowNum) -> new Pedido(
                        rs.getLong("id"),
                        rs.getString("cliente_cpf"),
                        rs.getString("status"),
                        rs.getString("endereco_entrega"),
                        rs.getTimestamp("data_pedido").toLocalDateTime(),
                        List.of(),
                        rs.getDouble("custo_itens"),
                        rs.getDouble("desconto"),
                        rs.getDouble("imposto"),
                        rs.getDouble("custo_total")));
    }

    private List<ItemPedido> buscarItensPedido(long pedidoId) {
        String sql = "SELECT ip.id, ip.pedido_id, ip.produto_id, p.descricao, ip.quantidade, ip.preco_unitario "
                + "FROM itens_pedido ip JOIN produtos p ON p.id = ip.produto_id WHERE ip.pedido_id = ?";
        return jdbcTemplate.query(sql,
                ps -> ps.setLong(1, pedidoId),
                (rs, rowNum) -> new ItemPedido(
                        rs.getLong("id"),
                        rs.getLong("pedido_id"),
                        rs.getLong("produto_id"),
                        rs.getString("descricao"),
                        rs.getInt("quantidade"),
                        rs.getInt("preco_unitario")));
    }
}

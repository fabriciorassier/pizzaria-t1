package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.StatusPedido;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.ICozinhaService;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.IDescontoService;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.IEstoqueService;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.IImpostoService;
import com.bcopstein.ex4_lancheriaddd_v1.domain.port.service.IPagamentoService;

@Service
public class PedidoService {
    private PedidoRepository pedidoRepository;
    private IEstoqueService estoqueService;
    private IImpostoService impostoService;
    private IDescontoService descontoService;
    private IPagamentoService pagamentoService;
    private ICozinhaService cozinhaService;
    private ProdutosRepository produtosRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, IEstoqueService estoqueService,
            IImpostoService impostoService, IDescontoService descontoService,
            IPagamentoService pagamentoService, ICozinhaService cozinhaService,
            ProdutosRepository produtosRepository) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueService = estoqueService;
        this.impostoService = impostoService;
        this.descontoService = descontoService;
        this.pagamentoService = pagamentoService;
        this.cozinhaService = cozinhaService;
        this.produtosRepository = produtosRepository;
    }

    public ItemPedido criarItemPedido(long produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade invalida para o produto " + produtoId);
        }
        Produto produto = produtosRepository.recuperaProdutoPorid(produtoId);
        if (produto == null) {
            throw new IllegalArgumentException("Produto nao encontrado: " + produtoId);
        }
        return new ItemPedido(produto.getId(), produto.getDescricao(), quantidade, produto.getPreco());
    }

    @Transactional
    public Pedido submeterPedido(String cpfCliente, String enderecoEntrega, List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Pedido precisa ter pelo menos um item");
        }
        List<String> faltas = estoqueService.detalharFaltas(itens);
        if (!faltas.isEmpty()) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para alguns itens do pedido.", faltas);
        }

        double custoItens = calcularCustoItens(itens);
        double desconto = descontoService.calcularDesconto(cpfCliente, custoItens);
        double imposto = impostoService.calcularImposto(custoItens);
        double custoTotal = custoItens - desconto + imposto;

        Pedido pedido = new Pedido(cpfCliente, StatusPedido.NOVO.name(), enderecoEntrega, LocalDateTime.now(), itens);
        long pedidoId = pedidoRepository.salvar(pedido);

        for (ItemPedido item : itens) {
            item.setPedidoId(pedidoId);
        }
        pedidoRepository.salvarItens(pedidoId, itens);
        pedidoRepository.atualizarCustos(pedidoId, custoItens, desconto, imposto, custoTotal);
        pedidoRepository.atualizarStatus(pedidoId, StatusPedido.APROVADO.name());
        estoqueService.baixarEstoque(itens);

        return pedidoRepository.buscarPorId(pedidoId);
    }

    public Pedido consultarPedido(long pedidoId) {
        return pedidoRepository.buscarPorId(pedidoId);
    }

    public Pedido cancelarPedido(long pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido nao encontrado");
        }
        if (!StatusPedido.APROVADO.name().equals(pedido.getStatus())) {
            throw new IllegalArgumentException("Pedido nao pode ser cancelado. Status atual: " + pedido.getStatus());
        }
        pedidoRepository.atualizarStatus(pedidoId, StatusPedido.CANCELADO.name());
        return pedidoRepository.buscarPorId(pedidoId);
    }

    public Pedido pagarPedido(long pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido nao encontrado");
        }
        if (!StatusPedido.APROVADO.name().equals(pedido.getStatus())) {
            throw new IllegalArgumentException("Pedido precisa estar APROVADO para pagar");
        }
        if (!pagamentoService.processarPagamento(pedidoId)) {
            pedidoRepository.atualizarStatus(pedidoId, StatusPedido.ABANDONADO.name());
            throw new IllegalArgumentException("Pagamento nao aprovado");
        }
        pedidoRepository.atualizarStatus(pedidoId, StatusPedido.PAGO.name());
        cozinhaService.processarPedido(pedidoId);
        return pedidoRepository.buscarPorId(pedidoId);
    }

    public List<Pedido> listarEntregues(LocalDate inicio, LocalDate fim) {
        return pedidoRepository.buscarEntreguesEntreDatas(inicio, fim);
    }

    public List<Pedido> listarEntreguesDoCliente(String cpf, LocalDate inicio, LocalDate fim) {
        return pedidoRepository.buscarDoClienteEntreguesEntreDatas(cpf, inicio, fim);
    }

    private double calcularCustoItens(List<ItemPedido> itens) {
        int totalCentavos = 0;
        for (ItemPedido item : itens) {
            totalCentavos += item.getPrecoUnitario() * item.getQuantidade();
        }
        return totalCentavos / 100.0;
    }
}

package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.StatusPedido;

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
        /*
         * Fluxo UC4: valida itens, calcula custos, persiste pedido e move para APROVADO.
         */
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

        Pedido pedido = new Pedido(cpfCliente, StatusPedido.NOVO, enderecoEntrega, LocalDateTime.now(), itens);
        long pedidoId = pedidoRepository.salvar(pedido);

        for (ItemPedido item : itens) {
            item.setPedidoId(pedidoId);
        }
        pedidoRepository.salvarItens(pedidoId, itens);
        pedidoRepository.atualizarCustos(pedidoId, custoItens, desconto, imposto, custoTotal);
        pedidoRepository.atualizarStatus(pedidoId, StatusPedido.APROVADO);
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
        if (!StatusPedido.APROVADO.equals(pedido.getStatus())) {
            throw new IllegalArgumentException("Pedido nao pode ser cancelado. Status atual: " + pedido.getStatus());
        }
        pedidoRepository.atualizarStatus(pedidoId, StatusPedido.CANCELADO);
        return pedidoRepository.buscarPorId(pedidoId);
    }

    public Pedido pagarPedido(long pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido nao encontrado");
        }
        if (!StatusPedido.APROVADO.equals(pedido.getStatus())) {
            throw new IllegalArgumentException("Pedido precisa estar APROVADO para pagar");
        }
        if (!pagamentoService.processarPagamento(pedidoId)) {
            pedidoRepository.atualizarStatus(pedidoId, StatusPedido.ABANDONADO);
            throw new IllegalArgumentException("Pagamento nao aprovado");
        }
        pedidoRepository.atualizarStatus(pedidoId, StatusPedido.PAGO);
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
        /* Precos ficam em centavos no banco e a resposta usa reais. */
        int totalCentavos = 0;
        for (ItemPedido item : itens) {
            totalCentavos += item.getPrecoUnitario() * item.getQuantidade();
        }
        return totalCentavos / 100.0;
    }
}

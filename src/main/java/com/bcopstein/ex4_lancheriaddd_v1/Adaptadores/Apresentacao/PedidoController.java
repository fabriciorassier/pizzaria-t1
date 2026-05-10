package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ConsultarStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosClienteEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.TokenUtil;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.ErroResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin("*")
public class PedidoController {
    private SubmeterPedidoUC submeterPedidoUC;
    private ConsultarStatusPedidoUC consultarStatusPedidoUC;
    private CancelarPedidoUC cancelarPedidoUC;
    private PagarPedidoUC pagarPedidoUC;
    private ListarPedidosEntreguesUC listarPedidosEntreguesUC;
    private ListarPedidosClienteEntreguesUC listarPedidosClienteEntreguesUC;

    public PedidoController(SubmeterPedidoUC submeterPedidoUC,
            ConsultarStatusPedidoUC consultarStatusPedidoUC,
            CancelarPedidoUC cancelarPedidoUC,
            PagarPedidoUC pagarPedidoUC,
            ListarPedidosEntreguesUC listarPedidosEntreguesUC,
            ListarPedidosClienteEntreguesUC listarPedidosClienteEntreguesUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.consultarStatusPedidoUC = consultarStatusPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
        this.listarPedidosClienteEntreguesUC = listarPedidosClienteEntreguesUC;
    }

    @PostMapping("/submeter")
    public ResponseEntity<?> submeterPedido(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody SubmeterPedidoRequest request) {
        try {
            String cpf = TokenUtil.extrairCpf(authorizationHeader);
            return ResponseEntity.ok(submeterPedidoUC.run(cpf, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> consultarStatus(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable long id) {
        try {
            TokenUtil.extrairCpf(authorizationHeader);
            StatusPedidoResponse response = consultarStatusPedidoUC.run(id);
            if (response == null) {
                return ResponseEntity.status(404).body(new ErroResponse("Pedido nao encontrado"));
            }
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarPedido(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable long id) {
        try {
            TokenUtil.extrairCpf(authorizationHeader);
            return ResponseEntity.ok(cancelarPedidoUC.run(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<?> pagarPedido(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable long id) {
        try {
            TokenUtil.extrairCpf(authorizationHeader);
            return ResponseEntity.ok(pagarPedidoUC.run(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        }
    }

    @GetMapping("/entregues")
    public ResponseEntity<?> listarEntregues(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) {
        try {
            return ResponseEntity.ok(listarPedidosEntreguesUC.run(inicio, fim));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        }
    }

    @GetMapping("/cliente/{cpf}/entregues")
    public ResponseEntity<?> listarEntreguesCliente(@PathVariable String cpf,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) {
        try {
            return ResponseEntity.ok(listarPedidosClienteEntreguesUC.run(cpf, inicio, fim));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        }
    }
}

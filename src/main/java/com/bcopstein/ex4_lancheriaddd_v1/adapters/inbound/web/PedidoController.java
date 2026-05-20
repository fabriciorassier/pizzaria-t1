package com.bcopstein.ex4_lancheriaddd_v1.adapters.inbound.web;

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

import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.ConsultarStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.ListarPedidosClienteEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.ListarPedidosEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.PagarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.dto.request.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.ErroResponse;
import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.StatusPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.application.auth.AuthTokenService;

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
    private AuthTokenService authTokenService;

    public PedidoController(SubmeterPedidoUC submeterPedidoUC,
            ConsultarStatusPedidoUC consultarStatusPedidoUC,
            CancelarPedidoUC cancelarPedidoUC,
            PagarPedidoUC pagarPedidoUC,
            ListarPedidosEntreguesUC listarPedidosEntreguesUC,
            ListarPedidosClienteEntreguesUC listarPedidosClienteEntreguesUC,
            AuthTokenService authTokenService) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.consultarStatusPedidoUC = consultarStatusPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
        this.listarPedidosClienteEntreguesUC = listarPedidosClienteEntreguesUC;
        this.authTokenService = authTokenService;
    }

    @PostMapping("/submeter")
    public ResponseEntity<?> submeterPedido(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody SubmeterPedidoRequest request) {
        String cpf = authTokenService.extrairCpf(authorizationHeader);
        return ResponseEntity.ok(submeterPedidoUC.run(cpf, request));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> consultarStatus(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable long id) {
        authTokenService.extrairCpf(authorizationHeader);
        StatusPedidoResponse response = consultarStatusPedidoUC.run(id);
        if (response == null) {
            return ResponseEntity.status(404).body(new ErroResponse("Pedido nao encontrado"));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarPedido(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable long id) {
        authTokenService.extrairCpf(authorizationHeader);
        return ResponseEntity.ok(cancelarPedidoUC.run(id));
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<?> pagarPedido(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable long id) {
        authTokenService.extrairCpf(authorizationHeader);
        return ResponseEntity.ok(pagarPedidoUC.run(id));
    }

    @GetMapping("/entregues")
    public ResponseEntity<?> listarEntregues(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) {
        return ResponseEntity.ok(listarPedidosEntreguesUC.run(inicio, fim));
    }

    @GetMapping("/cliente/{cpf}/entregues")
    public ResponseEntity<?> listarEntreguesCliente(@PathVariable String cpf,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) {
        return ResponseEntity.ok(listarPedidosClienteEntreguesUC.run(cpf, inicio, fim));
    }
}

package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.LoginClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RegistrarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.LoginRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.ErroResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.LoginResponse;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("*")
public class ClienteController {
    private RegistrarClienteUC registrarClienteUC;
    private LoginClienteUC loginClienteUC;

    public ClienteController(RegistrarClienteUC registrarClienteUC, LoginClienteUC loginClienteUC) {
        this.registrarClienteUC = registrarClienteUC;
        this.loginClienteUC = loginClienteUC;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistrarClienteRequest request) {
        try {
            return ResponseEntity.ok(registrarClienteUC.run(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = loginClienteUC.run(request.getEmail(), request.getSenha());
            if (response == null) {
                return ResponseEntity.badRequest().body(new ErroResponse("Email ou senha incorretos"));
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        }
    }
}

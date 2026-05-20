package com.bcopstein.ex4_lancheriaddd_v1.adapters.inbound.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.LoginClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.RegistrarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.dto.request.LoginRequest;
import com.bcopstein.ex4_lancheriaddd_v1.application.dto.request.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.ErroResponse;
import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.LoginResponse;

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
        return ResponseEntity.ok(registrarClienteUC.run(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginClienteUC.run(request.getEmail(), request.getSenha());
        if (response == null) {
            return ResponseEntity.badRequest().body(new ErroResponse("Email ou senha incorretos"));
        }
        return ResponseEntity.ok(response);
    }
}

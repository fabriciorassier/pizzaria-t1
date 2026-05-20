package com.bcopstein.ex4_lancheriaddd_v1.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.LoginResponse;
import com.bcopstein.ex4_lancheriaddd_v1.application.auth.TokenUtil;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.domain.service.ClienteService;

@Component
public class LoginClienteUC {
    private ClienteService clienteService;

    @Autowired
    public LoginClienteUC(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public LoginResponse run(String email, String senha) {
        Cliente cliente = clienteService.autenticar(email, senha);
        if (cliente == null) {
            return null;
        }
        String token = TokenUtil.gerarTokenCpf(cliente.getCpf());
        return new LoginResponse(token, cliente.getCpf(), cliente.getNome(), "Login realizado com sucesso!");
    }
}

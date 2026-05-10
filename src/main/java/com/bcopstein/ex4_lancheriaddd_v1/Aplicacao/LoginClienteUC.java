package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.LoginResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;

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

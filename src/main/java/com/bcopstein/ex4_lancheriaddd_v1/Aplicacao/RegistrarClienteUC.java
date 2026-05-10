package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistroClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;

@Component
public class RegistrarClienteUC {
    private ClienteService clienteService;

    @Autowired
    public RegistrarClienteUC(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public RegistroClienteResponse run(RegistrarClienteRequest request) {
        Cliente cliente = new Cliente(request.getCpf(), request.getNome(), request.getCelular(),
                request.getEndereco(), request.getEmail(), request.getSenha());
        clienteService.registrarCliente(cliente);
        return new RegistroClienteResponse("Cliente registrado com sucesso!", cliente.getCpf());
    }
}

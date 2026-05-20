package com.bcopstein.ex4_lancheriaddd_v1.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.domain.port.repository.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Cliente;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void registrarCliente(Cliente cliente) {
        validarCamposCliente(cliente);
        if (clienteRepository.existeEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }
        clienteRepository.salvar(cliente);
    }

    public Cliente autenticar(String email, String senha) {
        if (email == null || senha == null || email.isBlank() || senha.isBlank()) {
            return null;
        }
        Cliente cliente = clienteRepository.buscarPorEmail(email);
        if (cliente == null) {
            return null;
        }
        if (!cliente.getSenha().equals(senha)) {
            return null;
        }
        return cliente;
    }

    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.buscarPorCpf(cpf);
    }

    private void validarCamposCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente invalido");
        }
        if (vazio(cliente.getCpf()) || vazio(cliente.getNome()) || vazio(cliente.getCelular())
                || vazio(cliente.getEndereco()) || vazio(cliente.getEmail()) || vazio(cliente.getSenha())) {
            throw new IllegalArgumentException("Todos os campos sao obrigatorios");
        }
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}

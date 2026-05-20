package com.bcopstein.ex4_lancheriaddd_v1.application.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenUtil {
    public static String extrairCpf(String authorizationHeader) {
        /* Token simples do trabalho: Authorization Bearer com CPF em Base64. */
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new IllegalArgumentException("Header Authorization obrigatorio");
        }
        String token = authorizationHeader.replace("Bearer ", "").trim();
        if (token.isBlank()) {
            throw new IllegalArgumentException("Token invalido");
        }
        byte[] decoded = Base64.getDecoder().decode(token);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    public static String gerarTokenCpf(String cpf) {
        return Base64.getEncoder().encodeToString(cpf.getBytes(StandardCharsets.UTF_8));
    }
}

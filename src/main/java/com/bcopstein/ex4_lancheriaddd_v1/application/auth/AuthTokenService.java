package com.bcopstein.ex4_lancheriaddd_v1.application.auth;

import org.springframework.stereotype.Component;

@Component
public class AuthTokenService {
    public String extrairCpf(String authorizationHeader) {
        return TokenUtil.extrairCpf(authorizationHeader);
    }
}

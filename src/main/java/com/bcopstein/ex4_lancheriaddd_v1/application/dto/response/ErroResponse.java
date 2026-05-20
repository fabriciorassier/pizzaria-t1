package com.bcopstein.ex4_lancheriaddd_v1.application.dto.response;

import java.util.List;

public class ErroResponse {
    private String erro;
    private List<String> detalhes;

    public ErroResponse(String erro) {
        this.erro = erro;
    }

    public ErroResponse(String erro, List<String> detalhes) {
        this.erro = erro;
        this.detalhes = detalhes;
    }

    public String getErro() {
        return erro;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }
}

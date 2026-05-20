package com.bcopstein.ex4_lancheriaddd_v1.application.dto.response;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.domain.model.CabecalhoCardapio;

public record CabecalhoCardapioResponse(List<CabecalhoCardapio> cabecalhos) {
}

package com.bcopstein.ex4_lancheriaddd_v1.adapters.inbound.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.ErroResponse;
import com.bcopstein.ex4_lancheriaddd_v1.domain.service.EstoqueInsuficienteException;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ErroResponse> handleEstoqueInsuficiente(EstoqueInsuficienteException ex) {
        return ResponseEntity.badRequest().body(new ErroResponse(ex.getMessage(), ex.getDetalhes()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErroResponse(ex.getMessage()));
    }
}

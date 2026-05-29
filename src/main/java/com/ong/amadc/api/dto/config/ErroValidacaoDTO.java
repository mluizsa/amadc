package com.ong.amadc.api.dto.config;

import org.springframework.validation.FieldError;

public record ErroValidacaoDTO(String campo, String mensagem) {
    public ErroValidacaoDTO(FieldError erro) {
        this(erro.getField(), erro.getDefaultMessage());
    }
}
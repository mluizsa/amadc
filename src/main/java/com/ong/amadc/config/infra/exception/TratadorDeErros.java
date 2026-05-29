package com.ong.amadc.config.infra.exception;

import com.ong.amadc.api.dto.config.ErroValidacaoDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroValidacaoDTO>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(ErroValidacaoDTO::new).toList());
    }

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> tratarErroNegocio(BusinessException ex) {
        // Retorna 400 Bad Request com a mensagem: "Este voluntário já se encontra inativo..."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> tratarErroIntegridade(DataIntegrityViolationException ex) {
        String msg = ex.getMostSpecificCause().getMessage();
        String mensagemAmigavel = "Erro de integridade de dados no servidor.";

        if (msg != null) {
            if (msg.contains("volu_email_key") || msg.contains("email")) {
                mensagemAmigavel = "O e-mail informado já está cadastrado no sistema.";
            } else if (msg.contains("volu_cpf_key") || msg.contains("cpf")) {
                mensagemAmigavel = "O CPF informado já está cadastrado no sistema.";
            }
        }

        // Retorna HTTP 409 Conflict com o JSON contendo a propriedade "message"
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", mensagemAmigavel));
    }
}
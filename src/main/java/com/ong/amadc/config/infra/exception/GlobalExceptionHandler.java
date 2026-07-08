package com.ong.amadc.config.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        return criarRespostaErro(HttpStatus.BAD_REQUEST, "Regra de Negócio", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return criarRespostaErro(HttpStatus.BAD_REQUEST, "Argumento Inválido", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        // Logamos o erro real no console do servidor para fins de debug
        ex.printStackTrace();

        return criarRespostaErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro Interno",
                "Ocorreu um erro inesperado no servidor ao processar a requisição.");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return criarRespostaErro(HttpStatus.NOT_FOUND, "Não Encontrado", ex.getMessage());
    }

    /**
     * Método utilitário privado para garantir que TODO erro retornado pela API
     * possua exatamente o mesmo formato de JSON.
     */
    private ResponseEntity<Object> criarRespostaErro(HttpStatus status, String erro, String mensagem) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", erro);
        body.put("message", mensagem);

        return ResponseEntity.status(status).body(body);
    }
}
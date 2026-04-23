package com.ong.amadc.controller;

import com.ong.amadc.domain.model.Usuario;
import com.ong.amadc.dto.LoginRequestDTO;
import com.ong.amadc.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ENDPOINT DE LOGIN (Necessário para o Swagger gerar o token)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO data) {
        try {
            var response = authService.autenticar(data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication auth) {
        return ResponseEntity.ok(authService.obterDadosUsuarioLogado(auth));
    }
}
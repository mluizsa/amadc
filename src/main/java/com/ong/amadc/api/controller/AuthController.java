package com.ong.amadc.api.controller;

import com.ong.amadc.api.dto.request.LoginRequestDTO;
import com.ong.amadc.api.dto.response.MeResponseDTO; // Importar
import com.ong.amadc.domain.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO data) {
        return authService.login(data);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return authService.logout();
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponseDTO> getMe(Authentication auth) { // Corrigir tipo de retorno
        return ResponseEntity.ok(authService.obterDadosUsuarioLogado(auth));
    }
}
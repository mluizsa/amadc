package com.ong.amadc.domain.service;

import com.ong.amadc.config.infra.security.TokenService;
import com.ong.amadc.domain.model.UsuarioEntidade;
import com.ong.amadc.api.dto.request.LoginRequestDTO;
import com.ong.amadc.api.dto.response.UsuarioDetalhesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public ResponseEntity<?> login(LoginRequestDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            String token = tokenService.generateToken((UsuarioEntidade) auth.getPrincipal());

            ResponseCookie cookie = ResponseCookie.from("user_token", token)
                    .httpOnly(true)
                    .secure(false) // Mudar para true em produção
                    .path("/")
                    .maxAge(7200)
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body("Login realizado com sucesso");

        } catch (Exception e) {
            // Centralizamos o erro 403 aqui
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credenciais inválidas");
        }
    }

    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("user_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout realizado com sucesso");
    }

    public UsuarioDetalhesDTO obterDadosUsuarioLogado(Authentication auth) {
        UsuarioEntidade user = (UsuarioEntidade) auth.getPrincipal();
        var permissoes = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new UsuarioDetalhesDTO(user.getId(), user.getUsername(), user.getEmail(), permissoes);
    }
}

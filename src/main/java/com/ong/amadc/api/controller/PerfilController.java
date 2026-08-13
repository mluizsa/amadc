package com.ong.amadc.api.controller;

import com.ong.amadc.domain.model.PerfilEntidade;
import com.ong.amadc.domain.repository.PerfilRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/perfis")
@Tag(name = "Perfis")
public class PerfilController {

    private final PerfilRepository perfilRepository;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VOLUNTARIO_WRITE', 'ADMIN')")
    public ResponseEntity<List<PerfilEntidade>> listarPerfis() {
        // Retorna todos os perfis cadastrados no banco para o Front-end listar
        return ResponseEntity.ok(perfilRepository.findAll());
    }
}

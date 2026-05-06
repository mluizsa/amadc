package com.ong.amadc.api.controller;

import com.ong.amadc.api.dto.VoluntarioRequestDTO;
import com.ong.amadc.api.dto.VoluntarioResponseDTO;
import com.ong.amadc.domain.model.VoluntarioEntidade;
import com.ong.amadc.domain.service.VoluntarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voluntarios")
public class VoluntarioController {

    @Autowired
    private VoluntarioService service;

    @PostMapping
    @PreAuthorize("hasAuthority('VOLUNTARIO_WRITE')")
    public ResponseEntity<VoluntarioEntidade> cadastrar(@RequestBody @Valid VoluntarioRequestDTO dto) {
        var salvo = service.cadastrar(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VOLUNTARIO_READ')")
    public ResponseEntity<List<VoluntarioResponseDTO>> listar() {
        List<VoluntarioResponseDTO> voluntarios = service.listarTodos();
        return ResponseEntity.ok(voluntarios);
    }
}
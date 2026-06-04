package com.ong.amadc.api.controller;

import com.ong.amadc.api.dto.request.AnimalFiltroRequest;
import com.ong.amadc.api.dto.request.AnimalRequestDTO;
import com.ong.amadc.api.dto.response.AnimalResponseDTO;
import com.ong.amadc.domain.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animais")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ANIMAL_READ', 'ADMIN')")
    @Operation(summary = "Listagem Animais",
            description = "Lista de Animais Resgatados ou Acolhidos")
    public Page<AnimalResponseDTO> listar(
            @ParameterObject Pageable paginacao,
            @ParameterObject AnimalFiltroRequest filtro) {
        return animalService.listarTodos(paginacao, filtro);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ANIMAL_WRITE', 'ADMIN')")
    @Operation(summary = "Cadastrar Animais",
            description = "Cadastro de animais Resgatados ou Acolhidos")
    public ResponseEntity<AnimalResponseDTO> adicionar(
            @RequestBody AnimalRequestDTO animalRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(animalService.salvar(animalRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ANIMAL_WRITE', 'ADMIN')")
    @Operation(summary = "Edição do Animal",
            description = "Edição de Animal na ONG")
    public ResponseEntity<AnimalResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AnimalRequestDTO animalRequest) {
        return ResponseEntity.ok(animalService.atualizar(id, animalRequest));
    }
}
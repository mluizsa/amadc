package com.ong.amadc.api.controller;

import com.ong.amadc.api.dto.request.AnimalFiltroRequest;
import com.ong.amadc.api.dto.request.AnimalRequestDTO;
import com.ong.amadc.api.dto.response.AnimalResponseDTO;
import com.ong.amadc.api.dto.response.StatusAnimalResponseDTO;
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

import java.util.List;

@RestController
@RequestMapping("/api/animais")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping
    @PreAuthorize("hasPermission(null, 'ANIMAL_READ')")
    @Operation(summary = "Listagem Animais",
            description = "Lista de Animais Resgatados ou Acolhidos")
    public Page<AnimalResponseDTO> listar(
            @ParameterObject Pageable paginacao,
            @ParameterObject AnimalFiltroRequest filtro) {
        return animalService.listarTodos(paginacao, filtro);
    }

    @GetMapping("/status")
    @PreAuthorize("hasPermission(null, 'ANIMAL_READ')")
    @Operation(summary = "Listar Status de Animais",
            description = "Retorna uma lista de todos os status de animais disponíveis.")
    public ResponseEntity<List<StatusAnimalResponseDTO>> listarStatus() {
        return ResponseEntity.ok(animalService.listarStatus());
    }

    @GetMapping("/portes")
    @PreAuthorize("hasPermission(null, 'ANIMAL_READ')")
    @Operation(summary = "Listar Portes de Animais",
            description = "Retorna uma lista de todos os portes de animais disponíveis.")
    public ResponseEntity<List<String>> listarPortes() {
        return ResponseEntity.ok(animalService.listarPortes());
    }

    @GetMapping("/sexos")
    @PreAuthorize("hasPermission(null, 'ANIMAL_READ')")
    @Operation(summary = "Listar Sexos de Animais",
            description = "Retorna uma lista de todos os sexos de animais disponíveis.")
    public ResponseEntity<List<String>> listarSexos() {
        return ResponseEntity.ok(animalService.listarSexos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'ANIMAL_READ')")
    @Operation(summary = "Detalhes do Animal",
            description = "Retorna os detalhes completos de um animal pelo seu ID.")
    public ResponseEntity<AnimalResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'ANIMAL_WRITE')")
    @Operation(summary = "Cadastrar Animais",
            description = "Cadastro de animais Resgatados ou Acolhidos")
    public ResponseEntity<AnimalResponseDTO> adicionar(
            @RequestBody AnimalRequestDTO animalRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(animalService.salvar(animalRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'ANIMAL_WRITE')")
    @Operation(summary = "Edição do Animal",
            description = "Edição de Animal na ONG")
    public ResponseEntity<AnimalResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AnimalRequestDTO animalRequest) {
        return ResponseEntity.ok(animalService.atualizar(id, animalRequest));
    }
}
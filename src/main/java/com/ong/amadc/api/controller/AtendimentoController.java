package com.ong.amadc.api.controller;

import com.ong.amadc.api.dto.request.AtendimentoRequestDTO;
import com.ong.amadc.api.dto.response.AtendimentoResponseDTO;
import com.ong.amadc.domain.service.AtendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
@Tag(name = "Atendimentos", description = "Gerenciamento de atendimentos médicos e clínicos dos animais")
public class AtendimentoController {

    @Autowired
    private AtendimentoService atendimentoService;

    @GetMapping
    @PreAuthorize("hasPermission(null, 'ATENDIMENTO_READ')")
    @Operation(summary = "Listar atendimentos", description = "Retorna a listagem de todos os atendimentos cadastrados")
    public ResponseEntity<List<AtendimentoResponseDTO>> listar() {
        return ResponseEntity.ok(atendimentoService.listarTodosDTO());
    }

    @GetMapping("/animal/{animalId}")
    @PreAuthorize("hasPermission(null, 'ATENDIMENTO_READ')")
    @Operation(summary = "Listar atendimentos por animal", description = "Retorna o histórico de atendimentos clínicos de um animal específico")
    public ResponseEntity<List<AtendimentoResponseDTO>> listarPorAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(atendimentoService.listarPorAnimalDTO(animalId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'ATENDIMENTO_READ')")
    @Operation(summary = "Buscar atendimento por ID", description = "Retorna os detalhes de um atendimento específico")
    public ResponseEntity<AtendimentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atendimentoService.buscarPorIdDTO(id));
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'ATENDIMENTO_WRITE')")
    @Operation(summary = "Cadastrar atendimento", description = "Registra um novo atendimento médico ou clínico")
    public ResponseEntity<AtendimentoResponseDTO> criar(@RequestBody @Valid AtendimentoRequestDTO requestDTO) {
        AtendimentoResponseDTO novoAtendimento = atendimentoService.salvar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAtendimento);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'ATENDIMENTO_WRITE')")
    @Operation(summary = "Remover atendimento", description = "Exclui um registro de atendimento do sistema")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        atendimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
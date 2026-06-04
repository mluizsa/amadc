package com.ong.amadc.api.controller;

import com.ong.amadc.api.dto.request.VoluntarioRequestDTO;
import com.ong.amadc.api.dto.response.VoluntarioResponseDTO;
import com.ong.amadc.domain.model.VoluntarioEntidade;
import com.ong.amadc.domain.service.VoluntarioService;
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
@RequestMapping("/api/voluntarios")
@Tag(name = "Voluntários")
public class VoluntarioController {

    @Autowired
    private VoluntarioService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('VOLUNTARIO_WRITE', 'ADMIN')")
    @Operation(summary = "Cadastrar voluntários",
            description = "Cadastro de voluntários na ONG")
    public ResponseEntity<VoluntarioEntidade> cadastrar(@RequestBody @Valid VoluntarioRequestDTO dto) {
        var salvo = service.cadastrar(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VOLUNTARIO_READ', 'ADMIN')")
    @Operation(summary = "Lista voluntários",
               description = "Lista ativos por padrão. Passe 'ativos=false' para ver os inativos.")
    public ResponseEntity<List<VoluntarioResponseDTO>> listar(
            @RequestParam(name = "ativos", required = false, defaultValue = "true") Boolean ativos) {
        var voluntarios = service.listarTodos(ativos);
        return ResponseEntity.ok(voluntarios);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VOLUNTARIO_READ', 'ADMIN')")
    @Operation(summary = "Voluntário por ID",
               description = "Busca e exibe os dados detalhados de um único voluntário pelo seu ID.")
    public ResponseEntity<VoluntarioResponseDTO> exibirVoluntario(
            @PathVariable(name = "id") Long idVoluntario) {
        var voluntarios = service.buscarPorId(idVoluntario);
        return ResponseEntity.ok(voluntarios);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VOLUNTARIO_WRITE', 'ADMIN')")
    @Operation(summary = "Edição do voluntário",
            description = "Edição de voluntários na ONG")
    public ResponseEntity<VoluntarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid VoluntarioRequestDTO dto) {
        var atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(new VoluntarioResponseDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VOLUNTARIO_WRITE', 'ADMIN')")
    @Operation(summary = "Exclusão de voluntário",
            description = "Exclusão lógica de voluntários na ONG")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
package com.ong.amadc.api.controller;

import com.ong.amadc.domain.model.CustoAnimalEntidade;
import com.ong.amadc.domain.service.CustoAnimalService;
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
@RequestMapping("/api/custos-animais")
@Tag(name = "Custos e Despesas", description = "Gerenciamento financeiro dos custos por animal")
public class CustoAnimalController {

    @Autowired
    private CustoAnimalService custoAnimalService;

    @GetMapping("/animal/{animalId}")
    @PreAuthorize("hasPermission(null, 'FINANCE_READ')")
    @Operation(summary = "Listar custos por animal", description = "Retorna o histórico financeiro de despesas de um animal específico")
    public ResponseEntity<List<CustoAnimalEntidade>> listarPorAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(custoAnimalService.listarPorAnimal(animalId));
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, 'FINANCE_WRITE')")
    @Operation(summary = "Cadastrar custo avulso", description = "Registra uma nova despesa avulsa para o animal (ex: banho, ração, etc.)")
    public ResponseEntity<CustoAnimalEntidade> criar(@RequestBody @Valid CustoAnimalEntidade custo) {
        CustoAnimalEntidade novoCusto = custoAnimalService.salvar(custo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCusto);
    }
}
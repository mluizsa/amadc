package com.ong.amadc.api.controller;

import com.ong.amadc.domain.service.ArquivoSistemaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/arquivos")
@RequiredArgsConstructor
public class ArquivoSistemaController {

    private final ArquivoSistemaService arquivoSistemaService;

    @PostMapping(value = "/animal/{animalId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasPermission(null, 'ANIMAL_WRITE')")
    @Operation(summary = "Fazer upload de fotos do animal",
            description = "Recebe um lote (array) de fotos para integrar e normatizar na Linha do Tempo do animal")
    public ResponseEntity<Map<String, List<String>>> uploadFotoAnimal(
            @PathVariable Long animalId,
            @RequestParam("tipoVinculo") String tipoVinculo,
            @RequestParam("fotos") MultipartFile[] fotos) {

        List<String> urlResultante = arquivoSistemaService.armazenarFotosAnimal(animalId, tipoVinculo, fotos);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("urls", urlResultante));
    }
}
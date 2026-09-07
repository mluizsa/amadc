package com.ong.amadc.api.controller;

import com.ong.amadc.domain.model.ArquivoSistemaEntidade;
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

    @PostMapping(value = "/voluntario/{voluntarioId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasPermission(null, 'VOLUNTARIO_WRITE')")
    @Operation(summary = "Fazer upload de documentos/arquivos do voluntário",
            description = "Recebe um lote (array) de arquivos para vincular ao voluntário")
    public ResponseEntity<Map<String, List<String>>> uploadArquivosVoluntario(
            @PathVariable Long voluntarioId,
            @RequestParam("tipoVinculo") String tipoVinculo,
            @RequestParam("arquivos") MultipartFile[] arquivos) {

        List<String> urlResultante = arquivoSistemaService.armazenarArquivosVoluntario(voluntarioId, tipoVinculo, arquivos);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("urls", urlResultante));
    }

    @GetMapping("/voluntario/{voluntarioId}")
    @PreAuthorize("hasPermission(null, 'VOLUNTARIO_READ')")
    @Operation(summary = "Listar arquivos do voluntário",
            description = "Retorna todos os arquivos vinculados a um voluntário específico")
    public ResponseEntity<List<ArquivoSistemaEntidade>> listarArquivosVoluntario(@PathVariable Long voluntarioId) {
        List<ArquivoSistemaEntidade> arquivos = arquivoSistemaService.listarArquivosVoluntario(voluntarioId);
        return ResponseEntity.ok(arquivos);
    }
}
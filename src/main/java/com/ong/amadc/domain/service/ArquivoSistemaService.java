package com.ong.amadc.domain.service;

import com.ong.amadc.domain.validator.ArquivoSistemaValidator;
import com.ong.amadc.domain.business.ArquivoBusiness;
import com.ong.amadc.domain.model.AnimalEntidade;
import com.ong.amadc.domain.model.ArquivoSistemaEntidade;
import com.ong.amadc.domain.model.TipoVinculoArquivoEntidade;
import com.ong.amadc.domain.repository.AnimalRepository;
import com.ong.amadc.domain.repository.ArquivoSistemaRepository;
import com.ong.amadc.domain.repository.TipoVinculoArquivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArquivoSistemaService {

    private final ArquivoSistemaRepository arquivoRepository;
    private final TipoVinculoArquivoRepository tipoVinculoRepository;
    private final AnimalRepository animalRepository;
    private final ArquivoSistemaValidator validator;
    private final ArquivoBusiness arquivoBusiness; // 👈 Injetado aqui

    @Value("${amadc.upload.diretorio:./uploads}")
    private String diretorioUpload;

    @Transactional
    public List<String> armazenarFotosAnimal(Long animalId, String tipoVinculoId, MultipartFile[] fotos) {
        // 1. Validações preliminares do lote
        if (fotos == null || fotos.length == 0) {
            throw new IllegalArgumentException("Nenhum arquivo foi enviado.");
        }

        AnimalEntidade animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("Animal não localizado."));

        TipoVinculoArquivoEntidade tipoVinculo = tipoVinculoRepository.getReferenceById(tipoVinculoId);

        List<String> urlsProcessadas = new ArrayList<>();

        // 2. Processa cada foto do array individualmente
        for (int i = 0; i < fotos.length; i++) {
            MultipartFile arquivo = fotos[i];

            validator.validarUpload(arquivo, tipoVinculoId);

            String nomeNormatizado = arquivoBusiness.extrairEGerarNomeNormatizado(arquivo, animal.getNome(), tipoVinculoId, i);

            arquivoBusiness.salvarArquivoNoDisco(arquivo, nomeNormatizado, this.diretorioUpload);

            ArquivoSistemaEntidade arquivoEntidade = ArquivoSistemaEntidade.builder()
                    .url("/uploads/" + nomeNormatizado)
                    .nomeOriginal(arquivo.getOriginalFilename())
                    .tipoVinculo(tipoVinculo)
                    .animal(animal)
                    .atendimentoId(null)
                    .voluntarioId(null)
                    .build();

            arquivoRepository.save(arquivoEntidade);
            urlsProcessadas.add(arquivoEntidade.getUrl());
        }

        return urlsProcessadas;
    }
}
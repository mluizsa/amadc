package com.ong.amadc.domain.service;

import com.ong.amadc.api.dto.request.AnimalFiltroRequest;
import com.ong.amadc.api.dto.request.AnimalRequestDTO;
import com.ong.amadc.api.dto.response.AnimalResponseDTO;
import com.ong.amadc.api.dto.response.FotoGaleriaResponseDTO;
import com.ong.amadc.api.dto.response.StatusAnimalResponseDTO;
import com.ong.amadc.api.mapper.AnimalMapper;
import com.ong.amadc.domain.business.AnimalBusiness; // 🆕 Injetando a classe de regras
import com.ong.amadc.domain.model.AnimalEntidade;
import com.ong.amadc.domain.model.ArquivoSistemaEntidade;
import com.ong.amadc.domain.repository.AnimalRepository;
import com.ong.amadc.domain.repository.ArquivoSistemaRepository;
import com.ong.amadc.domain.repository.StatusAnimalRepository;
import com.ong.amadc.config.infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final StatusAnimalRepository statusAnimalRepository;
    private final ArquivoSistemaRepository arquivoSistemaRepository;
    private final AnimalBusiness animalBusiness; // 🆕 Adicionado aqui

    @Transactional
    public AnimalResponseDTO salvar(AnimalRequestDTO request) {
        AnimalEntidade animal = new AnimalEntidade();
        animalMapper.mapRequestToEntity(request, animal);

        // 🎯 Regra de negócio aplicada antes de persistir
        animalBusiness.validarRegrasDeCadastro(animal);

        animal = animalRepository.save(animal);
        return AnimalResponseDTO.fromEntity(animal, null, Collections.<FotoGaleriaResponseDTO>emptyList());
    }

    @Transactional
    public AnimalResponseDTO atualizar(Long id, AnimalRequestDTO request) {
        AnimalEntidade animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado com o ID: " + id));

        animalMapper.mapRequestToEntity(request, animal);

        // 🎯 Regra de negócio aplicada antes de atualizar
        animalBusiness.validarRegrasDeCadastro(animal);

        animal = animalRepository.save(animal);
        return this.buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public Page<AnimalResponseDTO> listarTodos(Pageable paginacao, AnimalFiltroRequest filtro) {
        return animalRepository.findAllWithFilters(
                paginacao,
                filtro.nomeParaLike(),
                filtro.statusId(),
                filtro.porte(),
                filtro.possivelAdocao(),
                filtro.sexo(),
                filtro.castrado()
        ).map(animal -> AnimalResponseDTO.fromEntity(animal, animal.getUrlFotoCapa(), Collections.emptyList()));
    }

    @Transactional(readOnly = true)
    public AnimalResponseDTO buscarPorId(Long id) {
        AnimalEntidade animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado com o ID: " + id));

        // 🎯 CORREÇÃO AQUI: Trocado o método antigo pelo método correto que busca direto pelo ID do animal
        List<ArquivoSistemaEntidade> arquivos = arquivoSistemaRepository
                .findByAnimalIdOrderByDataCriacaoAsc(id);

        List<FotoGaleriaResponseDTO> galeria = arquivos.stream()
                .map(arq -> new FotoGaleriaResponseDTO(
                        arq.getId(),
                        arq.getUrl(),
                        arq.getIsCapa() != null && arq.getIsCapa()
                ))
                .collect(Collectors.toList());

        String urlCapa = animal.getUrlFotoCapa();
        if (urlCapa == null) {
            urlCapa = arquivos.stream()
                    .filter(arq -> arq.getIsCapa() != null && arq.getIsCapa())
                    .map(ArquivoSistemaEntidade::getUrl)
                    .findFirst()
                    .orElse(null);
        }

        return AnimalResponseDTO.fromEntity(animal, urlCapa, galeria);
    }

    @Transactional
    public void definirFotoComoCapa(Long animalId, Long arquivoId) {
        AnimalEntidade animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado com o ID: " + animalId));

        List<ArquivoSistemaEntidade> arquivos = arquivoSistemaRepository.findByAnimalIdOrderByDataCriacaoAsc(animalId);

        animalBusiness.validarPertencimentoDoArquivo(arquivoId, arquivos);

        String novaUrlCapa = null;

        for (ArquivoSistemaEntidade arq : arquivos) {
            if (arq.getId().equals(arquivoId)) {
                arq.setIsCapa(true);
                novaUrlCapa = arq.getUrl();
            } else {
                arq.setIsCapa(false);
            }
        }

        animal.setUrlFotoCapa(novaUrlCapa);
    }

    public List<StatusAnimalResponseDTO> listarStatus() {
        return statusAnimalRepository.findAll().stream()
                .map(StatusAnimalResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<String> listarPortes() {
        return Arrays.asList("PEQUENO", "MEDIO", "GRANDE");
    }

    public List<String> listarSexos() {
        return Arrays.asList("MACHO", "FEMEA");
    }
}
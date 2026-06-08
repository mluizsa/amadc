package com.ong.amadc.domain.service;

import com.ong.amadc.api.dto.request.AnimalFiltroRequest;
import com.ong.amadc.api.dto.request.AnimalRequestDTO;
import com.ong.amadc.api.dto.response.AnimalResponseDTO;
import com.ong.amadc.api.dto.response.StatusAnimalResponseDTO;
import com.ong.amadc.api.mapper.AnimalMapper;
import com.ong.amadc.domain.model.AnimalEntidade;
import com.ong.amadc.domain.repository.AnimalRepository;
import com.ong.amadc.domain.repository.StatusAnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final StatusAnimalRepository statusAnimalRepository;

    @Transactional
    public AnimalResponseDTO salvar(AnimalRequestDTO request) {
        AnimalEntidade animal = new AnimalEntidade();
        animalMapper.mapRequestToEntity(request, animal);

        animal = animalRepository.save(animal);
        return AnimalResponseDTO.fromEntity(animal);
    }

    @Transactional
    public AnimalResponseDTO atualizar(Long id, AnimalRequestDTO request) {
        AnimalEntidade animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));

        animalMapper.mapRequestToEntity(request, animal);

        animal = animalRepository.save(animal);
        return AnimalResponseDTO.fromEntity(animal);
    }

    public Page<AnimalResponseDTO> listarTodos(Pageable paginacao,AnimalFiltroRequest filtro) {
        Boolean buscarAdotaveis = filtro.possivelAdocao() == null || filtro.possivelAdocao();
        String likeNome = null;
        if (filtro.nome() != null && !filtro.nome().isEmpty()) {
            likeNome = "%" + filtro.nome().toLowerCase() + "%";
        }

        return animalRepository.findAllWithFilters(
                paginacao,
                likeNome,
                filtro.statusId(),
                filtro.porte(),
                buscarAdotaveis,
                filtro.sexo()
        ).map(AnimalResponseDTO::fromEntity);
    }

    public AnimalResponseDTO buscarPorId(Long id) {
        AnimalEntidade animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
        return AnimalResponseDTO.fromEntity(animal);
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
        return Arrays.asList("MACHO", "FÊMEA");
    }
}
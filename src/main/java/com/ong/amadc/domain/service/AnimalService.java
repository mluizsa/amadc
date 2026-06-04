package com.ong.amadc.domain.service;

import com.ong.amadc.api.dto.request.AnimalFiltroRequest;
import com.ong.amadc.api.dto.request.AnimalRequestDTO;
import com.ong.amadc.api.dto.response.AnimalResponseDTO;
import com.ong.amadc.api.mapper.AnimalMapper;
import com.ong.amadc.domain.model.AnimalEntidade;
import com.ong.amadc.domain.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

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
        return animalRepository.findAllWithFilters(
                paginacao,
                filtro.statusId(),
                filtro.porte(),
                buscarAdotaveis,
                filtro.sexo()
        ).map(AnimalResponseDTO::fromEntity);
    }
}
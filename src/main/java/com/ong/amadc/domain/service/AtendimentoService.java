package com.ong.amadc.domain.service;

import com.ong.amadc.api.dto.request.AtendimentoRequestDTO;
import com.ong.amadc.api.dto.response.AtendimentoResponseDTO;
import com.ong.amadc.domain.business.AtendimentoBusiness;
import com.ong.amadc.domain.model.AtendimentoEntidade;
import com.ong.amadc.domain.repository.AtendimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtendimentoService {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private CustoAnimalService custoAnimalService;

    @Autowired
    private AtendimentoBusiness atendimentoBusiness;

    public List<AtendimentoEntidade> listarTodos() {
        return atendimentoRepository.findAll();
    }

    public List<AtendimentoResponseDTO> listarTodosDTO() {
        return listarTodos().stream()
                .map(AtendimentoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AtendimentoEntidade> listarPorAnimal(Long animalId) {
        return atendimentoRepository.findByAnimalId(animalId);
    }

    public List<AtendimentoResponseDTO> listarPorAnimalDTO(Long animalId) {
        return listarPorAnimal(animalId).stream()
                .map(AtendimentoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AtendimentoEntidade buscarPorId(Long id) {
        return atendimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado com o ID: " + id));
    }

    public AtendimentoResponseDTO buscarPorIdDTO(Long id) {
        return AtendimentoResponseDTO.fromEntity(buscarPorId(id));
    }

    @Transactional
    public AtendimentoResponseDTO salvar(AtendimentoRequestDTO requestDTO) {
        // Delega para a Business processar a regra/montagem da entidade a partir do DTO
        AtendimentoEntidade entidade = atendimentoBusiness.prepararEntidadeParaSalvar(requestDTO);

        AtendimentoEntidade salvo = atendimentoRepository.save(entidade);
        custoAnimalService.registrarCustoDeAtendimento(salvo);

        return AtendimentoResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public void deletar(Long id) {
        AtendimentoEntidade atendimento = buscarPorId(id);
        custoAnimalService.deletarPorAtendimento(id);
        atendimentoRepository.delete(atendimento);
    }
}
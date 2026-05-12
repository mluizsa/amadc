package com.ong.amadc.domain.service;

import com.ong.amadc.api.dto.VoluntarioRequestDTO;
import com.ong.amadc.api.dto.VoluntarioResponseDTO;
import com.ong.amadc.config.infra.exception.BusinessException;
import com.ong.amadc.domain.business.VoluntarioBusiness;
import com.ong.amadc.domain.model.VoluntarioEntidade;
import com.ong.amadc.domain.repository.VoluntarioRepository;
import com.ong.amadc.domain.validator.VoluntarioValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository repository;

    @Autowired
    private VoluntarioValidator validator;

    @Autowired
    private VoluntarioBusiness business;

    @Transactional
    public VoluntarioEntidade cadastrar(VoluntarioEntidade voluntario) {
        validator.validarNovoVoluntario(voluntario);

        if (business.precisaDeTermoResponsabilidade(voluntario)) {
            voluntario.setObservacoes(voluntario.getObservacoes() + " [PENDENTE TERMO]");
        }
        return repository.save(voluntario);
    }

    @Transactional(readOnly = true)
    public List<VoluntarioResponseDTO> listarTodos(Boolean apenasAtivos) {
        var lista = apenasAtivos ? repository.findAllByAtivoTrue() : repository.findAllByAtivoFalse();

        return lista.stream()
                .map(VoluntarioResponseDTO::new)
                .toList();
    }

    @Transactional
    public VoluntarioEntidade atualizar(Long id, VoluntarioRequestDTO dto) {
        var voluntario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voluntário não encontrado"));

        BeanUtils.copyProperties(dto, voluntario, "id", "dataCriacao");

        return repository.save(voluntario);
    }

    @Transactional
    public void desativar(Long id) {
        var voluntario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voluntário não encontrado"));

        if (!voluntario.getAtivo()) {
            throw new BusinessException("Este voluntário já se encontra inativo no sistema.");
        }

        voluntario.setAtivo(false);
        repository.save(voluntario);
    }
}
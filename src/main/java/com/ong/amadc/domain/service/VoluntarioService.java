package com.ong.amadc.domain.service;

import com.ong.amadc.api.dto.VoluntarioResponseDTO;
import com.ong.amadc.domain.business.VoluntarioBusiness;
import com.ong.amadc.domain.model.VoluntarioEntidade;
import com.ong.amadc.domain.repository.VoluntarioRepository;
import com.ong.amadc.domain.validator.VoluntarioValidator;
import jakarta.transaction.Transactional;
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

    public List<VoluntarioResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(VoluntarioResponseDTO::new) // Construtor que recebe a Entity
                .toList();
    }
}
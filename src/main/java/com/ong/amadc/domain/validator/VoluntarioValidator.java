package com.ong.amadc.domain.validator;

import com.ong.amadc.config.infra.exception.BusinessException;
import com.ong.amadc.domain.model.VoluntarioEntidade;
import com.ong.amadc.domain.repository.VoluntarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VoluntarioValidator {

    @Autowired
    private VoluntarioRepository repository;

    public void validarNovoVoluntario(VoluntarioEntidade voluntario) {
        if (repository.existsByCpf(voluntario.getCpf())) {
            throw new BusinessException("Já existe um voluntário cadastrado com este CPF.");
        }

        if (voluntario.getDataNascimento().isAfter(LocalDate.now().minusYears(16))) {
            throw new BusinessException("O voluntário deve ter pelo menos 16 anos.");
        }
        // Adicione outras validações específicas aqui
    }
}
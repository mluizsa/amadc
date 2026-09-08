package com.ong.amadc.domain.service;

import com.ong.amadc.domain.model.AtendimentoEntidade;
import com.ong.amadc.domain.model.CustoAnimalEntidade;
import com.ong.amadc.domain.repository.CustoAnimalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustoAnimalService {

    @Autowired
    private CustoAnimalRepository custoAnimalRepository;

    public List<CustoAnimalEntidade> listarPorAnimal(Long animalId) {
        return custoAnimalRepository.findByAnimalId(animalId);
    }

    @Transactional
    public CustoAnimalEntidade salvar(CustoAnimalEntidade custo) {
        if (custo.getDataCusto() == null) {
            custo.setDataCusto(LocalDateTime.now());
        }
        return custoAnimalRepository.save(custo);
    }

    @Transactional
    public void registrarCustoDeAtendimento(AtendimentoEntidade atendimento) {
        if (atendimento.getValorCobrado() != null && atendimento.getValorCobrado().compareTo(BigDecimal.ZERO) > 0) {
            List<CustoAnimalEntidade> existentes = custoAnimalRepository.findByAtendimentoId(atendimento.getId());

            CustoAnimalEntidade custo;
            if (existentes.isEmpty()) {
                custo = new CustoAnimalEntidade();
                custo.setAnimal(atendimento.getAnimal());
                custo.setAtendimento(atendimento);
            } else {
                custo = existentes.getFirst();
            }

            String descTipo = atendimento.getTipoAtendimento() != null ? atendimento.getTipoAtendimento().getDescricao() : "ATENDIMENTO";
            custo.setDescricao("Referente a atendimento: " + descTipo);
            custo.setValor(atendimento.getValorCobrado());
            custo.setDataCusto(atendimento.getDataAtendimento() != null ? atendimento.getDataAtendimento() : LocalDateTime.now());

            custoAnimalRepository.save(custo);
        }
    }

    @Transactional
    public void deletarPorAtendimento(Long atendimentoId) {
        List<CustoAnimalEntidade> custos = custoAnimalRepository.findByAtendimentoId(atendimentoId);
        if (!custos.isEmpty()) {
            custoAnimalRepository.deleteAll(custos);
        }
    }
}
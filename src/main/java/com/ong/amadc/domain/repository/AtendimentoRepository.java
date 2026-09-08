package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.AtendimentoEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<AtendimentoEntidade, Long> {
    List<AtendimentoEntidade> findByAnimalId(Long animalId);
    List<AtendimentoEntidade> findByVeterinarioId(Long veterinarioId);
}
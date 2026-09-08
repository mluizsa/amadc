package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.CustoAnimalEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustoAnimalRepository extends JpaRepository<CustoAnimalEntidade, Long> {
    List<CustoAnimalEntidade> findByAnimalId(Long animalId);
    List<CustoAnimalEntidade> findByAtendimentoId(Long atendimentoId);
}
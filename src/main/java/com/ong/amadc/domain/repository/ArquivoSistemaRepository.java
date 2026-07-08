package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.ArquivoSistemaEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArquivoSistemaRepository extends JpaRepository<ArquivoSistemaEntidade, Long> {

    List<ArquivoSistemaEntidade> findByAnimalIdOrderByDataCriacaoAsc(Long animalId);

}
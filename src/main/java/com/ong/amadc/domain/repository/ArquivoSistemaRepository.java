package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.ArquivoSistemaEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArquivoSistemaRepository extends JpaRepository<ArquivoSistemaEntidade, Long> {

    @Query("SELECT a FROM ArquivoSistemaEntidade a WHERE a.animal.id = :animalId ORDER BY a.dataCriacao ASC, a.id ASC")
    List<ArquivoSistemaEntidade> findByAnimalIdOrderByDataCriacaoAscIdAsc(Long animalId);

}
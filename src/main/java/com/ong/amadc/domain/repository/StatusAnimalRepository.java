package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.StatusAnimalEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatusAnimalRepository extends JpaRepository<StatusAnimalEntidade, Long> {
    Optional<StatusAnimalEntidade> findByDescricao(String descricao);
}
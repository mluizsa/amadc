package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.VoluntarioEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoluntarioRepository extends JpaRepository<VoluntarioEntidade, Long> {

    Optional<VoluntarioEntidade> findByCpf(String cpf);

    Optional<VoluntarioEntidade> findByEmail(String email);

    boolean existsByCpf(String cpf);

    List<VoluntarioEntidade> findAllByAtivoTrue();

    List<VoluntarioEntidade> findAllByAtivoFalse();
}
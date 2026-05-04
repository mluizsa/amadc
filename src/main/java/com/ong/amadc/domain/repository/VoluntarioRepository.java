package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {

    Optional<Voluntario> findByCpf(String cpf);

    Optional<Voluntario> findByEmail(String email);

    boolean existsByCpf(String cpf);
}
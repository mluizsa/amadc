package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.PerfilEntidade;
import com.ong.amadc.domain.model.UsuarioEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<PerfilEntidade, Long> {
}

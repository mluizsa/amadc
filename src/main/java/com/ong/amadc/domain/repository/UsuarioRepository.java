package com.ong.amadc.domain.repository;

import com.ong.amadc.domain.model.UsuarioEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntidade, Long> {

    // Este método é usado pelo Spring Security para a autenticação
    // Ele busca o usuário pelo campo 'login' ou 'username'
    UserDetails findByUsername(String username);

    // Útil para verificar se um email já está cadastrado no sistema
    boolean existsByEmail(String email);
}
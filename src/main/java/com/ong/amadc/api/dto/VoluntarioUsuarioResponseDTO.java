package com.ong.amadc.api.dto;

import com.ong.amadc.domain.model.PerfilEntidade;
import com.ong.amadc.domain.model.UsuarioEntidade;

import java.util.List;

public record VoluntarioUsuarioResponseDTO(
        String username,
        boolean ativo,
        List<PerfilResumidoDTO> perfis
) {
    public VoluntarioUsuarioResponseDTO(UsuarioEntidade usuario) {
        this(
                usuario.getUsername(),
                usuario.getAtivo(),
                usuario.getPerfis() != null ?
                        usuario.getPerfis().stream().map(PerfilResumidoDTO::new).toList() : List.of()
        );
    }
}

// DTO auxiliar para os perfis mapeados no seu formulário Vue
record PerfilResumidoDTO(Long id, String nome) {
    public PerfilResumidoDTO(PerfilEntidade perfil) {
        this(perfil.getId(), perfil.getNome());
    }
}
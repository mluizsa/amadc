package com.ong.amadc.api.dto;

import java.util.List;

public record UsuarioDetalhesDTO(
        Long id,
        String username,
        String email,
        List<String> permissions
) {
}

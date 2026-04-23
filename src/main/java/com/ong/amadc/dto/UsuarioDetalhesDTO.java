package com.ong.amadc.dto;

import java.util.List;

public record UsuarioDetalhesDTO(
        Long id,
        String username,
        String email,
        List<String> permissions
) {
}

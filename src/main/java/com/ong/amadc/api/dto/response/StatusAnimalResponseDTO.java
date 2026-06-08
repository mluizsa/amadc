package com.ong.amadc.api.dto.response;

import com.ong.amadc.domain.model.StatusAnimalEntidade;

public record StatusAnimalResponseDTO(
    Long id,
    String descricao
) {
    public static StatusAnimalResponseDTO fromEntity(StatusAnimalEntidade entidade) {
        return new StatusAnimalResponseDTO(entidade.getId(), entidade.getDescricao());
    }
}

package com.ong.amadc.api.dto;

import com.ong.amadc.domain.model.VoluntarioEntidade;

public record VoluntarioResponseDTO(
        Long id,
        String nome,
        String cpf,
        String telefone,
        String email,
        String ocupacao
) {
    public VoluntarioResponseDTO(VoluntarioEntidade voluntario) {
        this(
                voluntario.getId(),
                voluntario.getNome(),
                voluntario.getCpf(),
                voluntario.getTelefone(),
                voluntario.getEmail(),
                voluntario.getOcupacao()
        );
    }
}
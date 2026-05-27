package com.ong.amadc.api.dto;

import com.ong.amadc.domain.model.VoluntarioEntidade;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VoluntarioResponseDTO(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String telefone,
        String email,
        String ocupacao,
        LocalDateTime dataCriacao, // Novo campo
        String registradoPor,
        String observacoes// Novo campo
) {
    public VoluntarioResponseDTO(VoluntarioEntidade voluntario) {
        this(
                voluntario.getId(),
                voluntario.getNome(),
                voluntario.getCpf(),
                voluntario.getDataNascimento(),
                voluntario.getTelefone(),
                voluntario.getEmail(),
                voluntario.getOcupacao(),
                voluntario.getDataCriacao(),
                voluntario.getRegistradoPor(),
                voluntario.getObservacoes()
        );
    }
}
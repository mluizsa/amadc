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
        LocalDateTime dataCriacao,
        String registradoPor,
        String observacoes,
        boolean ativo,
        VoluntarioUsuarioResponseDTO usuario
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
                voluntario.getObservacoes(),
                voluntario.getAtivo(),
                voluntario.getUsuario() != null ? new VoluntarioUsuarioResponseDTO(voluntario.getUsuario()) : null
        );
    }
}
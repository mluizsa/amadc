package com.ong.amadc.api.dto.response;

import com.ong.amadc.domain.model.PerfilEntidade;
import com.ong.amadc.domain.model.VoluntarioEntidade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record VoluntarioResponseDTO(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String telefone,
        String email,
        String ocupacao,
        LocalDateTime dataCriacao,
        Boolean ativo,
        String registradoPor,
        String observacoes,
        List<String> perfisAcesso,
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
                voluntario.getAtivo(),
                voluntario.getRegistradoPor(),
                voluntario.getObservacoes(),
                // Lógica de mapeamento aqui:
                (voluntario.getUsuario() != null && voluntario.getUsuario().getPerfis() != null)
                        ? voluntario.getUsuario().getPerfis().stream()
                          .map(PerfilEntidade::getNome)
                          .collect(Collectors.toList())
                        : Collections.emptyList()
        );

    }
}
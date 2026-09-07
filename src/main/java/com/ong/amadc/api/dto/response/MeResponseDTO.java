package com.ong.amadc.api.dto.response;

import com.ong.amadc.domain.model.UsuarioEntidade;
import com.ong.amadc.domain.model.VoluntarioEntidade;

import java.time.LocalDate;
import java.util.List;

public record MeResponseDTO(
    String username,
    String email,
    String nomeCompleto, // Renomeado de 'nome' para 'nomeCompleto'
    String cpf,
    LocalDate dataNascimento,
    String telefone,
    String ocupacao,
    String observacoes,
    List<String> permissions
) {
    public static MeResponseDTO fromEntities(UsuarioEntidade usuario, VoluntarioEntidade voluntario, List<String> permissions) {
        if (usuario == null) {
            return null;
        }

        // Se não houver voluntário associado, retorna apenas os dados do usuário e suas permissões
        if (voluntario == null) {
            return new MeResponseDTO(
                usuario.getUsername(),
                usuario.getEmail(),
                null, null, null, null, null, null,
                permissions
            );
        }

        return new MeResponseDTO(
            usuario.getUsername(),
            voluntario.getEmail(), // Usa o e-mail do voluntário, que é mais completo
            voluntario.getNome(), // O campo 'nome' da entidade é mapeado para 'nomeCompleto' no DTO
            voluntario.getCpf(),
            voluntario.getDataNascimento(),
            voluntario.getTelefone(),
            voluntario.getOcupacao(),
            voluntario.getObservacoes(),
            permissions
        );
    }
}

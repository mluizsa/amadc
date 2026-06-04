package com.ong.amadc.api.dto.request;

import com.ong.amadc.domain.model.VoluntarioEntidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

/**
 * DTO para entrada de dados no cadastro de voluntários.
 * Utilizamos Bean Validation para garantir a integridade antes de chegar na Service.
 */
public record VoluntarioRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "O CPF deve estar no formato 000.000.000-00 ou apenas números")
        String cpf,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @Email(message = "O e-mail deve ser válido")
        String email,

        @NotNull(message = "A data de nascimento é obrigatória")
        LocalDate dataNascimento,

        String ocupacao,

        String observacoes
) {
        public VoluntarioEntidade toEntity() {
                var entidade = new VoluntarioEntidade();
                BeanUtils.copyProperties(this, entidade);
                return entidade;
        }
}
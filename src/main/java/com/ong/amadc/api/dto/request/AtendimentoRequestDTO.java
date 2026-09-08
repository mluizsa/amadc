package com.ong.amadc.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtendimentoRequestDTO(
        @NotNull(message = "O ID do animal é obrigatório")
        Long animalId,

        @NotNull(message = "O tipo de atendimento é obrigatório")
        Long tipoAtendimentoId,

        @NotNull(message = "A data e hora do atendimento são obrigatórias")
        LocalDateTime dataAtendimento,

        @Positive(message = "O peso deve ser um valor positivo")
        Double pesoNaData,

        @Positive(message = "O valor cobrado deve ser positivo")
        BigDecimal valorCobrado,

        String descricao
) {}
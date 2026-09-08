package com.ong.amadc.api.dto.response;

import com.ong.amadc.domain.model.AtendimentoEntidade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtendimentoResponseDTO(
        Long id,
        Long animalId,
        String animalNome,
        TipoAtendimentoDTO tipoAtendimento,
        LocalDateTime dataAtendimento,
        Double pesoNaData,
        BigDecimal valorCobrado,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao
) {
    // Record auxiliar interno para representar o tipo de atendimento de forma limpa
    public record TipoAtendimentoDTO(Long id, String descricao) {}

    public static AtendimentoResponseDTO fromEntity(AtendimentoEntidade entidade) {
        return new AtendimentoResponseDTO(
                entidade.getId(),
                entidade.getAnimal() != null ? entidade.getAnimal().getId() : null,
                entidade.getAnimal() != null ? entidade.getAnimal().getNome() : null,
                entidade.getTipoAtendimento() != null
                        ? new TipoAtendimentoDTO(entidade.getTipoAtendimento().getId(), entidade.getTipoAtendimento().getDescricao())
                        : null,
                entidade.getDataAtendimento(),
                entidade.getPesoNaData(),
                entidade.getValorCobrado(),
                entidade.getDescricao(),
                entidade.getDataCriacao(),
                entidade.getDataAlteracao()
        );
    }
}
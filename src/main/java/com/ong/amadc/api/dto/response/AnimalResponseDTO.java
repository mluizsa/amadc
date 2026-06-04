package com.ong.amadc.api.dto.response;

import com.ong.amadc.domain.model.AnimalEntidade;

import java.time.LocalDate;
import java.time.LocalTime;

public record AnimalResponseDTO(
    Long id,
    String nome,
    String especie,
    String raca,
    String porte,
    String sexo,
    String idadeEstimada,
    String pelagemCor,
    String pelagemTipo,
    String marcasCicatrizes,
    LocalDate dataResgate,
    LocalTime horaResgate,
    String localResgateBairro,
    String localResgateRua,
    String localResgateReferencia,
    String origem,
    String resgatadorNome,
    String resgatadorContato,
    Double pesoEntrada,
    String condicaoEntrada,
    String microchip,
    Long statusId,
    String statusDescricao,
    Boolean possivelAdocao,
    String historia
) {
    public static AnimalResponseDTO fromEntity(AnimalEntidade entidade) {
        return new AnimalResponseDTO(
                entidade.getId(),
                entidade.getNome(),
                entidade.getEspecie(),
                entidade.getRaca(),
                entidade.getPorte(),
                entidade.getSexo(),
                entidade.getIdadeEstimada(),
                entidade.getPelagemCor(),
                entidade.getPelagemTipo(),
                entidade.getMarcasCicatrizes(),
                entidade.getDataResgate(),
                entidade.getHoraResgate(),
                entidade.getLocalResgateBairro(),
                entidade.getLocalResgateRua(),
                entidade.getLocalResgateReferencia(),
                entidade.getOrigem(),
                entidade.getResgatadorNome(),
                entidade.getResgatadorContato(),
                entidade.getPesoEntrada(),
                entidade.getCondicaoEntrada(),
                entidade.getMicrochip(),
                entidade.getStatus() != null ? entidade.getStatus().getId() : null,
                entidade.getStatus() != null ? entidade.getStatus().getDescricao() : null,
                entidade.getPossivelAdocao(),
                entidade.getHistoria()
        );
    }
}
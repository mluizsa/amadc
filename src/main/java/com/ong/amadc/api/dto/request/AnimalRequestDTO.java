package com.ong.amadc.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AnimalRequestDTO(
    @NotBlank(message = "O nome é obrigatório")
    String nome,
    
    @NotBlank(message = "A espécie é obrigatória")
    String especie,
    
    String raca,
    
    String porte,
    
    @NotBlank(message = "O sexo é obrigatório")
    String sexo,
    
    String idadeEstimada,
    
    String pelagemCor,
    
    String pelagemTipo,
    
    String marcasCicatrizes,

    @NotNull(message = "A data de resgate é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dataResgate,

    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime horaResgate,

    String localResgateBairro,
    
    String localResgateRua,
    
    String localResgateReferencia,
    
    String origem,
    
    String resgatadorNome,
    
    String resgatadorContato,

    @Positive(message = "O peso deve ser um valor positivo")
    Double pesoEntrada,

    String condicaoEntrada,
    
    String microchip,

    Boolean castrado,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dataCastracao,
    Boolean dataCastracaoDesconhecida,

    @NotNull(message = "O status é obrigatório")
    Long statusId,

    Boolean possivelAdocao,
    
    String historia,
    
    List<String> fotosGaleria
) {}
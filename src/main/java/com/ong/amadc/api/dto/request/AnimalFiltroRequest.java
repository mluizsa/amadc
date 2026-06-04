package com.ong.amadc.api.dto.request;

import io.swagger.v3.oas.annotations.Parameter;

public record AnimalFiltroRequest(
    Long statusId,
    String porte,
    @Parameter(description = "Indica se o animal está disponível para adoção",
            example = "true")
    Boolean possivelAdocao,
    String sexo
) {}
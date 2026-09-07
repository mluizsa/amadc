package com.ong.amadc.api.dto.response;

import java.time.LocalDateTime;

public record FotoGaleriaResponseDTO(
        Long id,
        String url,
        Boolean isCapa,
        LocalDateTime dataCriacao
) {}
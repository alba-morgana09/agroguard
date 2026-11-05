package com.agroguard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CargaPortuariaRequest(
        @NotBlank(message = "id é obrigatório")
        String id,

        @NotBlank(message = "descricao é obrigatória")
        String descricao,

        @NotBlank(message = "origem é obrigatória")
        String origem,

        @NotBlank(message = "destino é obrigatório")
        String destino,

        @NotNull(message = "timestamp é obrigatório")
        LocalDateTime timestamp
) {
}

package com.agroguard.model;

import java.time.LocalDateTime;

public record Carga(
        String id,
        String descricao,
        String origem,
        String destino,
        LocalDateTime timestamp,
        String statusLacre
) {
}

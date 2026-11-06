package com.agroguard.model;

import java.time.Instant;

public record LogAuditoria(
        long id,
        Instant timestamp,
        String evento,
        String hashAnterior,
        String hash
) {
}

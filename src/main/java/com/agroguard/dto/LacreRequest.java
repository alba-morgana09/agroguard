package com.agroguard.dto;

import jakarta.validation.constraints.NotNull;

public record LacreRequest(@NotNull(message = "acao é obrigatória") AcaoLacre acao) {
    public enum AcaoLacre {
        ABRIR,
        BLOQUEAR
    }
}

package com.agroguard.dto;

public record CargaResponse(
        CargaPortuariaRequest carga,
        String hashSha256
) {
}

package com.agroguard.dto;

import java.util.Map;

public record ValidationErrorResponse(
        String mensagem,
        Map<String, String> campos
) {
}

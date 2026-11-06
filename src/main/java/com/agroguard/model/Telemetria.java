package com.agroguard.model;

import java.time.Instant;

public record Telemetria(
        Instant timestamp,
        double temperatura,
        double umidade,
        double latitude,
        double longitude
) {
}

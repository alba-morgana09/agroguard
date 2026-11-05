package com.agroguard.service;

import com.agroguard.dto.CargaPortuariaRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class IntegridadeServiceTest {

    private final IntegridadeService service = new IntegridadeService();

    @Test
    void deveGerarHashSha256Deterministico() {
        CargaPortuariaRequest carga = new CargaPortuariaRequest(
                "CARGA-001",
                "Soja em graos - 20 toneladas",
                "Porto de Santos",
                "Porto de Roterda",
                LocalDateTime.parse("2026-07-02T10:00:00")
        );

        assertThat(service.gerarHash(carga))
                .isEqualTo("f7de358deca490eb3c1a58b25cea0ff540de31a1422b53f6bb8ee8c7f72700cf");
    }
}

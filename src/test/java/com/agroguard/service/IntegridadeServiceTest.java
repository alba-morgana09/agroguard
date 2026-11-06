package com.agroguard.service;

import com.agroguard.dto.CargaPortuariaRequest;
import com.agroguard.model.LogAuditoria;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
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

    @Test
    void deveDetectarAdulteracaoNaCadeia() {
        Instant instante = Instant.parse("2026-11-06T00:00:00Z");
        String hash = serviceHash(1, instante, "CARGA_REGISTRADA", "");
        LogAuditoria original = new LogAuditoria(1, instante, "CARGA_REGISTRADA", "", hash);
        LogAuditoria adulterado = new LogAuditoria(1, instante, "CARGA_ALTERADA", "", hash);

        assertThat(new IntegridadeService().verificarCadeia(List.of(original))).isTrue();
        assertThat(new IntegridadeService().verificarCadeia(List.of(adulterado))).isFalse();
    }

    private String serviceHash(long id, Instant timestamp, String evento, String anterior) {
        try {
            var digest = java.security.MessageDigest.getInstance("SHA-256");
            return java.util.HexFormat.of().formatHex(digest.digest(
                    (id + "|" + timestamp + "|" + evento + "|" + anterior)
                            .getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }
}

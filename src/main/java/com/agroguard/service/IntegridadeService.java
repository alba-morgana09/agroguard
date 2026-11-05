package com.agroguard.service;

import com.agroguard.dto.CargaPortuariaRequest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class IntegridadeService {

    public String gerarHash(CargaPortuariaRequest carga) {
        String dadosConcatenados = carga.id()
                + carga.descricao()
                + carga.origem()
                + carga.destino()
                + carga.timestamp();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(
                    digest.digest(dadosConcatenados.getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 não está disponível na JVM.", exception);
        }
    }
}

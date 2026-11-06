package com.agroguard.service;

import com.agroguard.dto.CargaPortuariaRequest;
import com.agroguard.model.Carga;
import com.agroguard.model.LogAuditoria;
import com.agroguard.model.Telemetria;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class IntegridadeService {

    private final Map<String, Carga> cargas = new ConcurrentHashMap<>();
    private final Map<String, CopyOnWriteArrayList<LogAuditoria>> logs = new ConcurrentHashMap<>();
    private final AtomicLong proximoLogId = new AtomicLong(1);

    @PostConstruct
    void carregarExemplos() {
        registrarExemplo("CARGA-001", "Medicamentos refrigerados", "São Paulo", "Porto de Santos",
                LocalDateTime.of(2026, 11, 1, 8, 0), "BLOQUEADO");
        registrarExemplo("CARGA-002", "Alimentos perecíveis", "Campinas", "Porto de Paranaguá",
                LocalDateTime.of(2026, 11, 2, 9, 30), "BLOQUEADO");
        registrarExemplo("CARGA-003", "Carga seca", "Belo Horizonte", "Porto do Rio de Janeiro",
                LocalDateTime.of(2026, 11, 3, 14, 15), "BLOQUEADO");
    }

    public String gerarHash(CargaPortuariaRequest carga) {
        return sha256(carga.id() + carga.descricao() + carga.origem() + carga.destino() + carga.timestamp());
    }

    public Carga registrar(CargaPortuariaRequest request) {
        Carga carga = new Carga(request.id(), request.descricao(), request.origem(), request.destino(),
                request.timestamp(), "BLOQUEADO");
        cargas.put(carga.id(), carga);
        logs.put(carga.id(), new CopyOnWriteArrayList<>());
        adicionarLog(carga.id(), "CARGA_REGISTRADA");
        return carga;
    }

    public List<Carga> listarCargas() {
        return cargas.values().stream().sorted(Comparator.comparing(Carga::id)).toList();
    }

    public Carga obterCarga(String id) {
        Carga carga = cargas.get(id);
        if (carga == null) {
            throw new CargaNaoEncontradaException(id);
        }
        return carga;
    }

    public List<LogAuditoria> obterLogs(String id) {
        obterCarga(id);
        return List.copyOf(logs.get(id));
    }

    public Carga alterarLacre(String id, String acao) {
        Carga atual = obterCarga(id);
        String status = "ABRIR".equals(acao) ? "ABERTO" : "BLOQUEADO";
        Carga atualizada = new Carga(atual.id(), atual.descricao(), atual.origem(), atual.destino(),
                atual.timestamp(), status);
        cargas.put(id, atualizada);
        adicionarLog(id, "LACRE_" + acao);
        return atualizada;
    }

    public Telemetria obterTelemetria(String id) {
        Carga carga = obterCarga(id);
        int fator = Math.abs(id.hashCode() % 100);
        double temperatura = 2.0 + fator / 100.0;
        double umidade = 55.0 + fator % 20;
        double latitude = -23.55 + fator / 1000.0;
        double longitude = -46.63 + fator / 1000.0;
        return new Telemetria(Instant.now(), temperatura, umidade, latitude, longitude);
    }

    public boolean verificarCadeia(String id) {
        return verificarCadeia(obterLogs(id));
    }

    public boolean verificarCadeia(List<LogAuditoria> cadeia) {
        String hashAnterior = "";
        for (LogAuditoria log : cadeia) {
            if (!hashAnterior.equals(log.hashAnterior())
                    || !calcularHash(log.id(), log.timestamp(), log.evento(), log.hashAnterior()).equals(log.hash())) {
                return false;
            }
            hashAnterior = log.hash();
        }
        return true;
    }

    private void registrarExemplo(String id, String descricao, String origem, String destino,
                                  LocalDateTime timestamp, String status) {
        Carga carga = new Carga(id, descricao, origem, destino, timestamp, status);
        cargas.put(id, carga);
        logs.put(id, new CopyOnWriteArrayList<>());
        adicionarLog(id, "CARGA_REGISTRADA");
        adicionarLog(id, "LACRE_BLOQUEAR");
        adicionarLog(id, "TELEMETRIA_INICIAL");
    }

    private void adicionarLog(String cargaId, String evento) {
        CopyOnWriteArrayList<LogAuditoria> cadeia = logs.get(cargaId);
        String anterior = cadeia.isEmpty() ? "" : cadeia.get(cadeia.size() - 1).hash();
        Instant timestamp = Instant.now();
        long id = proximoLogId.getAndIncrement();
        cadeia.add(new LogAuditoria(id, timestamp, evento, anterior,
                calcularHash(id, timestamp, evento, anterior)));
    }

    private String calcularHash(long id, Instant timestamp, String evento, String hashAnterior) {
        return sha256(id + "|" + timestamp + "|" + evento + "|" + hashAnterior);
    }

    private String sha256(String valor) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(valor.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 não está disponível na JVM.", exception);
        }
    }

    public static class CargaNaoEncontradaException extends RuntimeException {
        public CargaNaoEncontradaException(String id) {
            super("Carga não encontrada: " + id);
        }
    }
}

package com.agroguard.controller;

import com.agroguard.dto.CargaPortuariaRequest;
import com.agroguard.dto.CargaResponse;
import com.agroguard.dto.ErroResponse;
import com.agroguard.dto.IntegridadeResponse;
import com.agroguard.dto.LacreRequest;
import com.agroguard.model.Carga;
import com.agroguard.service.IntegridadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CargaController {

    private final IntegridadeService service;

    public CargaController(IntegridadeService service) {
        this.service = service;
    }

    @GetMapping("/cargas")
    public List<Carga> listar() {
        return service.listarCargas();
    }

    @GetMapping("/cargas/{id}")
    public Carga obter(@PathVariable String id) {
        return service.obterCarga(id);
    }

    @GetMapping("/cargas/{id}/logs")
    public ResponseEntity<List<com.agroguard.model.LogAuditoria>> logs(@PathVariable String id) {
        return ResponseEntity.ok(service.obterLogs(id));
    }

    @GetMapping("/cargas/{id}/logs/verificar")
    public IntegridadeResponse verificar(@PathVariable String id) {
        List<?> logs = service.obterLogs(id);
        return new IntegridadeResponse(id, service.verificarCadeia(id), logs.size());
    }

    @GetMapping("/cargas/{id}/telemetria")
    public Object telemetria(@PathVariable String id) {
        return service.obterTelemetria(id);
    }

    @PostMapping("/cargas/{id}/lacre")
    public Carga lacre(@PathVariable String id, @Valid @RequestBody LacreRequest request) {
        return service.alterarLacre(id, request.acao().name());
    }

    @PostMapping("/registrar-carga")
    public ResponseEntity<CargaResponse> registrarCarga(@Valid @RequestBody CargaPortuariaRequest carga) {
        service.registrar(carga);
        return ResponseEntity.ok(new CargaResponse(carga, service.gerarHash(carga)));
    }
}

package com.agroguard.controller;

import com.agroguard.dto.CargaPortuariaRequest;
import com.agroguard.dto.CargaResponse;
import com.agroguard.service.IntegridadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CargaController {

    private final IntegridadeService integridadeService;

    public CargaController(IntegridadeService integridadeService) {
        this.integridadeService = integridadeService;
    }

    @PostMapping("/registrar-carga")
    public ResponseEntity<CargaResponse> registrarCarga(
            @Valid @RequestBody CargaPortuariaRequest carga
    ) {
        return ResponseEntity.ok(new CargaResponse(carga, integridadeService.gerarHash(carga)));
    }
}

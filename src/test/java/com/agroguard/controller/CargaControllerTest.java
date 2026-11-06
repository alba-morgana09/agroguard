package com.agroguard.controller;

import com.agroguard.service.IntegridadeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CargaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IntegridadeService integridadeService;

    @Test
    void deveRegistrarCargaERetornarHash() throws Exception {
        mockMvc.perform(post("/registrar-carga")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "CARGA-001",
                                  "descricao": "Soja em graos",
                                  "origem": "Porto de Santos",
                                  "destino": "Porto de Roterda",
                                  "timestamp": "2026-07-02T10:00:00"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carga.id").value("CARGA-001"))
                .andExpect(jsonPath("$.hashSha256").value(
                        integridadeService.gerarHash(new com.agroguard.dto.CargaPortuariaRequest(
                                "CARGA-001",
                                "Soja em graos",
                                "Porto de Santos",
                                "Porto de Roterda",
                                java.time.LocalDateTime.parse("2026-07-02T10:00:00")
                        ))
                ));
    }

    @Test
    void deveRetornar400QuandoCampoObrigatorioNaoForInformado() throws Exception {
        mockMvc.perform(post("/registrar-carga")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "",
                                  "descricao": "Soja em graos",
                                  "origem": "Porto de Santos",
                                  "destino": "Porto de Roterda"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Dados inválidos."))
                .andExpect(jsonPath("$.campos.id").value("id é obrigatório"))
                .andExpect(jsonPath("$.campos.timestamp").value("timestamp é obrigatório"));
    }

    @Test
    void deveListarCargaExemploEConsultarLogsTelemetriaELacre() throws Exception {
        mockMvc.perform(get("/cargas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
        mockMvc.perform(get("/cargas/CARGA-001/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hashAnterior").exists())
                .andExpect(jsonPath("$[0].hash").exists());
        mockMvc.perform(get("/cargas/CARGA-001/telemetria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperatura").exists())
                .andExpect(jsonPath("$.latitude").exists());
        mockMvc.perform(post("/cargas/CARGA-001/lacre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"acao\":\"ABRIR\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusLacre").value("ABERTO"));
        mockMvc.perform(get("/cargas/CARGA-001/logs/verificar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.integra").value(true));
    }

    @Test
    void deveRetornar404ParaCargaInexistente() throws Exception {
        mockMvc.perform(get("/cargas/INEXISTENTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Carga não encontrada: INEXISTENTE"));
    }
}

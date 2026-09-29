/**
 * Software: AgroGuard
 * Autoria: Alba Morgana
 * Finalidade: projeto como um software autoral em constante evolução e aberto a contribuições/demonstrações de portfólio
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;

@SpringBootApplication
public class AgroGuardApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgroGuardApplication.class, args);
    }
}

record CargaPortuaria(
        String id,
        String descricao,
        String origem,
        String destino,
        LocalDateTime timestamp
) {}

record CargaResponse(
        CargaPortuaria carga,
        String hashSha256
) {}

@Service
class IntegridadeService {
    public String gerarHash(CargaPortuaria carga) {
        try {
            // Concatenando os dados para formar o payload do hash
            String dadosConcatenados = carga.id() +
                    carga.descricao() +
                    carga.origem() +
                    carga.destino() +
                    carga.timestamp().toString();

            // Instanciando o algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Gerando os bytes do hash
            byte[] hashBytes = digest.digest(dadosConcatenados.getBytes(StandardCharsets.UTF_8));

            // Utilizando HexFormat para converter os bytes em String Hexadecimal
            return HexFormat.of().formatHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao inicializar o algoritmo SHA-256 para integridade da carga.", e);
        }
    }
}

@RestController
class CargaController {

    private final IntegridadeService integridadeService;

    public CargaController(IntegridadeService integridadeService) {
        this.integridadeService = integridadeService;
    }

    @PostMapping("/registrar-carga")
    public ResponseEntity<CargaResponse> registrarCarga(@RequestBody CargaPortuaria carga) {
        String hashGerado = integridadeService.gerarHash(carga);
        CargaResponse response = new CargaResponse(carga, hashGerado);
        return ResponseEntity.ok(response);
    }
}

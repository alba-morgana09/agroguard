# AgroGuard

API REST para registrar cargas portuárias e gerar um hash SHA-256 dos dados recebidos. O hash permite verificar se os dados usados no registro foram alterados posteriormente.

## Tecnologias

- Java 17
- Spring Boot 3.5.6
- Spring Web
- Spring Validation
- Maven
- JUnit 5 e Spring Boot Test

## Como executar

Clone o repositório e execute:

```bash
./mvnw spring-boot:run
```

No Windows, use:

```bat
mvnw.cmd spring-boot:run
```

A aplicação inicia na porta `8080`.

## Endpoint

### `POST /registrar-carga`

Registra uma carga e retorna a carga recebida com seu hash SHA-256.

Exemplo:

```bash
curl -X POST http://localhost:8080/registrar-carga \
  -H "Content-Type: application/json" \
  -d '{
    "id": "CARGA-001",
    "descricao": "Soja em grãos - 20 toneladas",
    "origem": "Porto de Santos",
    "destino": "Porto de Roterdã",
    "timestamp": "2026-07-02T10:00:00"
  }'
```

Resposta esperada:

```json
{
  "carga": {
    "id": "CARGA-001",
    "descricao": "Soja em grãos - 20 toneladas",
    "origem": "Porto de Santos",
    "destino": "Porto de Roterdã",
    "timestamp": "2026-07-02T10:00:00"
  },
  "hashSha256": "528d97d8b0d424b101c1dd4bb0245f4adb4758be81f08a65f53521e96cc90237"
}
```

Campos `id`, `descricao`, `origem`, `destino` e `timestamp` são obrigatórios. Quando algum deles é inválido ou ausente, a API responde `400 Bad Request` com os campos que precisam ser corrigidos.

## Implementado

- API Spring Boot executável com Maven Wrapper.
- Registro de cargas pelo endpoint `POST /registrar-carga`.
- Validação dos campos obrigatórios.
- Geração determinística de hash SHA-256.
- Resposta de erro `400` com mensagens por campo.
- Testes automatizados para o serviço de hash e para o endpoint.

## Planejado

- Persistência das cargas em banco de dados.
- Autenticação e controle de acesso.
- Consulta de cargas por hash.

## Autoria

Desenvolvido por **Alba Morgana**.

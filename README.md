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

## Endpoints

### `GET /cargas`

Lista as cargas em memória.

### `GET /cargas/{id}`

Consulta uma carga por ID.

### `GET /cargas/{id}/logs`

Retorna a trilha de auditoria com `id`, `timestamp`, `evento`, `hashAnterior` e `hash`.

### `GET /cargas/{id}/logs/verificar`

Recalcula a cadeia SHA-256 e retorna se ela está íntegra.

### `GET /cargas/{id}/telemetria`

Retorna temperatura, umidade, latitude e longitude simuladas.

### `POST /cargas/{id}/lacre`

Atualiza o lacre e registra o evento:

```bash
curl -X POST http://localhost:8080/cargas/CARGA-001/lacre \
  -H "Content-Type: application/json" \
  -d '{"acao":"ABRIR"}'
```

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
- Três cargas de exemplo são carregadas na inicialização.
- CORS configurável em `app.cors.allowed-origins`, limitado por padrão a
  `http://localhost:5173` e `https://alba-morgana09.github.io`.

## Simulado versus real

Esta versão é totalmente em memória e não se conecta a banco, rastreador ou sensores.
A telemetria é gerada pelo backend com valores simulados e coerentes; ela não vem de
sensores físicos. A trilha de auditoria também é mantida apenas durante a execução da
aplicação.

## Planejado

- Persistência das cargas em banco de dados.
- Autenticação e controle de acesso.
- Consulta de cargas por hash.

## Autoria

Desenvolvido por **Alba Morgana**.

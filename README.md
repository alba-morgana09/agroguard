# AgroGuard

Sistema de registro e verificação de integridade de cargas portuárias para operações de comércio exterior.

## 📋 Sobre o projeto

O AgroGuard nasceu da combinação entre experiência prática em comércio exterior e desenvolvimento de software, com o objetivo de trazer mais rastreabilidade e confiabilidade para o registro de cargas em operações portuárias.

O sistema gera um hash criptográfico (SHA-256) para cada carga registrada, garantindo que os dados não possam ser alterados sem que a violação seja detectável — um princípio de integridade de dados aplicado ao contexto logístico e aduaneiro.

## ⚙️ Funcionalidades

- Registro de cargas portuárias via API REST
- Geração automática de hash SHA-256 para verificação de integridade
- Estrutura baseada em `records` (Java 17+) para modelagem limpa dos dados

## 🛠️ Tecnologias utilizadas

- Java 17+
- Spring Boot
- Spring Web (REST)
- Java Security (MessageDigest / SHA-256)

## 🚀 Como executar

```bash
./mvnw spring-boot:run
```

A aplicação sobe por padrão na porta `8080`.

## 📡 Endpoints

### `POST /registrar-carga`

Registra uma nova carga e retorna o hash de integridade gerado.

**Corpo da requisição (exemplo):**
```json
{
  "id": "CARGA-001",
  "descricao": "Soja em grãos - 20 toneladas",
  "origem": "Porto de Santos",
  "destino": "Porto de Roterdã",
  "timestamp": "2026-07-02T10:00:00"
}
```

**Resposta (exemplo):**
```json
{
  "carga": { ... },
  "hashSha256": "a1b2c3..."
}
```

## 🗺️ Roadmap

- [ ] Validação de dados de entrada (`@Valid`)
- [ ] Persistência em banco de dados
- [ ] Autenticação e controle de acesso
- [ ] Consulta de cargas registradas por hash

## ✍️ Autoria

Desenvolvido por **Alba Morgana** — estudante de Análise e Desenvolvimento de Sistemas e formada em Comércio Exterior.

Projeto em desenvolvimento, submetido ao Edital NITRO 002/2026 (Porto Digital / Prefeitura do Recife) para registro de software no INPI.

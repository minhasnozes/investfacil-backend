# InvestFácil — Backend

API REST em Java 25 + Spring Boot 4 (webmvc, data-jpa, flyway, actuator, lombok) com PostgreSQL 17.

## Comandos

```bash
docker compose up -d          # sobe o Postgres (necessário para rodar e para os testes)
./mvnw spring-boot:run        # sobe a API em http://localhost:8080 (Windows: .\mvnw.cmd)
./mvnw verify                 # compila + testes (o mesmo que o CI roda)
./mvnw test -Dtest=NomeDaClasseTest
```

## Estrutura e convenções

- Pacote base: `investfacil.demo` (configurações em `config/`).
- Schema do banco **somente via Flyway**: `src/main/resources/db/migration/V<n>__descricao.sql`. Nunca editar uma migration que já foi mergeada; crie uma nova. `ddl-auto=validate`, então as entidades precisam bater com o schema.
- Configuração em `application.properties`. Valores sensíveis e que variam por ambiente vêm de variáveis de ambiente com default local.
- Actuator expõe apenas `/actuator/health`.
- JSON em camelCase. Mudanças de contrato afetam o front (`investfacil-frontend/src/types/`) e os testes.
- Testes: JUnit + Mockito + AssertJ (unitário), Spring Boot Test + MockMvc (integração). Regras de negócio (rentabilidade, IR, arredondamento) precisam de teste unitário. Veja `investfacil-tests/docs/estrategia-de-testes.md`.

## Git / CI

- `feature/<nome>` → PR `develop` → PR `master`.
- O CI (`.github/workflows/ci.yml`) roda `./mvnw -B verify` com Postgres a cada push. Rode `verify` localmente antes de abrir um PR.

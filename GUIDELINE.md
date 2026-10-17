# Compra Direta — Desafio Técnico

Bem-vindo(a)! Este repositório contém a base de um sistema de cadastro de **Compra Direta**
(processo de compras públicas). A base já está funcional — cadastro, listagem, edição,
fornecedores, propostas — e sua tarefa é completar as duas features descritas em
[`DESAFIO.md`](./DESAFIO.md).

## Pré-requisitos

- Java 17
- Maven 3.8+
- Node 18+ e npm
- Docker e Docker Compose

## Subindo a infraestrutura (Postgres + RabbitMQ)

```bash
docker-compose up -d
```

- Postgres: `localhost:5432` (db/user/senha: `compra_direta`)
- RabbitMQ: `localhost:5672` (amqp) e painel de administração em `http://localhost:15672` (guest/guest)

Se essas portas já estiverem em uso na sua máquina, ajuste o mapeamento de portas no
`docker-compose.yml` e as propriedades correspondentes em `backend/src/main/resources/application.yml`.

## Subindo o backend

```bash
cd backend
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. As migrations do Liquibase rodam automaticamente na
inicialização, criando o schema e uma massa de dados inicial (unidades compradoras, categorias,
fornecedores e duas compras diretas de exemplo em RASCUNHO).

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Para rodar os testes unitários:

```bash
mvn test
```

Os testes existentes em `backend/src/test/java/...` são o **exemplo a seguir** para os testes que
você vai escrever nas features que implementar: JUnit 5 + Mockito, padrão AAA, mocks apenas para
dependências externas (DataProviders/Integrations), nomenclatura `deve[Comportamento]Quando[Cenario]Test`.

## Subindo o frontend

```bash
cd frontend
npm install
npm run serve
```

A aplicação sobe em `http://localhost:8081` e já aponta para `http://localhost:8080/api` por
padrão. Para apontar para outra URL, defina a variável de ambiente `VUE_APP_API_URL` antes de rodar.

O frontend já tem as telas e os botões necessários para testar manualmente o que falta (Publicar e
Apurar Vencedor na tela de detalhe de uma compra direta) — você não precisa mexer nele para
resolver o desafio, só usá-lo para validar o que implementar no backend.

## Estrutura do backend

```
domain/
  entity/entities/        entidades JPA
  constant/                enums (Modalidade, CriterioJulgamento, StatusCompraDireta, ...)
  usecase/                 casos de uso, um pacote por ação (Input/Output/converter)
  interfaces/dataprovider/ portas de acesso a persistência
  exception/               exceções de domínio
  validation/              utilitário de validação
adapter/
  entrypoint/controller/   controllers REST + exception handler
  gateway/dataprovider/    implementações JPA das portas do domínio
application/
  factory/                 montagem (DI) dos use cases e beans, via builder
  config/                  Swagger, CORS, etc.
```

Agora veja [`DESAFIO.md`](./DESAFIO.md) para o que precisa ser implementado.

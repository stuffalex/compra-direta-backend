# Compra Direta — Backend API

Backend da aplicação de gerenciamento de **Compra Direta** (processo de compras públicas por Dispensa de Licitação e Inexigibilidade), desenvolvido com **Java 17**, **Spring Boot 3.2.5**, **PostgreSQL** e **RabbitMQ**.

---

## 🚀 Quick Start

### 1. Subir infraestrutura (Docker)
Inicia o banco de dados PostgreSQL e o broker RabbitMQ:
```bash
docker compose up -d
```
* **PostgreSQL:** `localhost:5432`
* **RabbitMQ Console:** `http://localhost:15672` (guest/guest)

### 2. Iniciar o Backend
```bash
mvn spring-boot:run
```
* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI Specs:** `http://localhost:8080/v3/api-docs`

As migrações de banco e seeds de dados são aplicadas automaticamente via Liquibase.

### 3. Reset do Banco e Filas (Ambiente Local)
Para resetar o banco de dados e as filas de volta ao estado inicial das seeds:
```bash
./reset-database.sh
```

### 4. Executar os Testes
```bash
mvn clean test
```

---

## 📖 Arquitetura e Decisões Técnicas

Para detalhes aprofundados sobre a arquitetura do projeto, consulte a documentação dedicada:

👉 [**docs/arquitetura-e-decisoes-tecnicas.md**](docs/arquitetura-e-decisoes-tecnicas.md)

Nela estão detalhados:
* Isolamento de domínio com **Clean / Hexagonal Architecture** (Use Cases puros e desacoplados do Spring);
* Comunicação assíncrona com **RabbitMQ** (Topologia, DLQ, prevenção de *poison pill* e idempotência);
* Apuração polimórfica de compras utilizando **Strategy Pattern**;
* Melhorias de resiliência e observabilidade (**Transactional Outbox Pattern**, **Distributed Tracing com Correlation ID** e **Paginação**);
* Catálogo de seeds para testes funcionais dos cenários;
* Referências técnicas para estudo e boas práticas.

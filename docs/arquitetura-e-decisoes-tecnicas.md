# Arquitetura e Decisões Técnicas — Sistema de Compra Direta

Documentação técnica do backend do sistema de Compra Direta, detalhando a stack utilizada, instruções para execução local, decisões arquiteturais tomadas e melhorias implementadas.

---

## 1. Stack e Tecnologias

* **Linguagem & Plataforma:** Java 17
* **Framework:** Spring Boot 3.2.5 (Spring Web, Spring AMQP, Spring Data JPA, Validation)
* **Banco de Dados:** PostgreSQL 15
* **Versionamento de Banco:** Liquibase (migrações estruturais e seeds de teste)
* **Mensageria:** RabbitMQ 3.13 (com suporte a Dead Letter Exchange / DLQ)
* **Testes Automatizados:** JUnit 5, Mockito, AssertJ
* **Documentação de API:** OpenAPI 3 / Swagger UI (springdoc-openapi)
* **Frontend Integrado:** Vue 2 / Vuetify / Vuex (em repositório adjacente `frontend/`)

---

## 2. Como Executar o Projeto

A inicialização é composta por três etapas: subir os serviços de apoio (Docker), iniciar o backend e iniciar o frontend.

### 2.1. Infraestrutura (Docker)
No diretório `backend/`:
```bash
docker compose up -d
```
* **PostgreSQL:** `localhost:5432` (database: `compra_direta`, user/password: `compra_direta`)
* **RabbitMQ AMQP:** `localhost:5672` (guest/guest)
* **RabbitMQ Management Console:** `http://localhost:15672` (guest/guest)

### 2.2. Backend (Spring Boot)
No diretório `backend/`:
```bash
mvn spring-boot:run
```
* O Liquibase aplica as migrações estruturais e os seeds automaticamente na inicialização.
* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI Docs:** `http://localhost:8080/v3/api-docs`

### 2.3. Frontend (Vue)
No diretório `frontend/`:
```bash
npm install
npm run serve
```
* **Aplicação Web:** `http://localhost:8081`

### 2.4. Reset do Ambiente Local
Caso queira restaurar o banco de dados e as filas para o estado inicial das seeds a qualquer momento:
```bash
./reset-database.sh
```
O script derruba os containers limpando os volumes (`docker compose down -v`) e recria os serviços limpos.

---

## 3. Carga Inicial de Dados (Seeds para Validação)

Para facilitar os testes funcionais e a verificação das regras de negócio, o Liquibase inicializa compras em diferentes estados e modalidades:

| ID | Número Compra | Modalidade | Critério | Status Inicial | Cenário Coberto |
|:---|:---|:---|:---|:---|:---|
| **3** | `CD-0003/2026` | Dispensa de Licitação | `MENOR_PRECO` | `PUBLICADA` | Apuração por Menor Preço (4 propostas). Vencedor esperado: ID 3 (*Gamma Tecnologia*, menor valor). |
| **4** | `CD-0004/2026` | Dispensa de Licitação | `MAIOR_DESCONTO` | `PUBLICADA` | Apuração por Maior Desconto (4 propostas). Vencedor esperado: ID 6 (*Pedro Santos ME*, 15% de desconto). |
| **5** | `CD-0005/2026` | Inexigibilidade | `NAO_SE_APLICA` | `PUBLICADA` | Apuração por Inexigibilidade (fornecedor exclusivo, 1 proposta sem valor/desconto). Homologação direta. |
| **6** | `CD-0006/2026` | Dispensa de Licitação | `MENOR_PRECO` | `RASCUNHO` | Publicação de compra direta com despacho de eventos assíncronos e geração de 3 notificações via RabbitMQ. |

---

## 4. Decisões Arquiteturais

### 4.1. Domínio Desacoplado (Clean / Hexagonal)
* **Use Cases Puros:** As classes dentro de `domain/usecase/` contêm apenas lógica de negócio e regras de validação. Não há acoplamento com o ecossistema Spring (sem `@Service`, `@Component` ou `@Autowired`) e nenhuma dependência de infraestrutura.
* **Portas de Saída:** O caso de uso `PublicarCompraDiretaUseCase` comunica-se com a mensageria por meio da interface `INotificacaoPublicacaoProducer` (porta definida no domínio). A implementação real fica no adapter `adapter/gateway/`.
* **Inversão de Dependências (DIP):** A instanciação e ligação dos Use Cases e seus adaptadores é centralizada na camada de aplicação via classes `@Configuration` (`application/factory/`).

### 4.2. Comunicação Assíncrona e Resiliência (RabbitMQ)
* **Desacoplamento Temporal:** A publicação da compra não trava a requisição HTTP aguardando a geração e o envio individual das notificações para cada fornecedor. O evento é publicado na fila e processado de forma assíncrona.
* **Topologia com DLQ:** A fila principal (`compra-direta.publicada.notificacao.queue`) é vinculada a uma Dead Letter Exchange (`compra-direta.dlx`) e Dead Letter Queue (`compra-direta.publicada.notificacao.dlq`).
* **Prevenção de Loop de Erro (*Poison Pill*):** O listener está configurado com `default-requeue-rejected: false`. Se uma mensagem sofrer um erro não recuperável no processamento, ela é roteada para a DLQ para posterior análise, evitando consumo infinito e esgotamento de recursos.
* **Idempotência no Consumidor:** Em redes distribuídas com entrega *at-least-once*, mensagens podem ser reenviadas em falhas transitórias de conexão. O consumidor valida `existePorCompraDiretaEFornecedor` antes de persistir a notificação, prevenindo registros duplicados no banco.

### 4.3. Apuração de Vencedor com Strategy Pattern
* O `ApurarVencedorCompraDiretaUseCase` não utiliza condicionais encadeadas (`if`/`switch`) para avaliar o critério de julgamento.
* Cada critério possui uma implementação isolada da interface `IApuracaoVencedorStrategy` (`MenorPrecoStrategy`, `MaiorDescontoStrategy`, `InexigibilidadeStrategy`).
* A resolução da estratégia é polimórfica, gerenciada pela `ApuracaoStrategyFactory`. Adicionar uma nova modalidade ou critério de apuração no futuro exige apenas a criação de uma nova classe de estratégia, respeitando o Princípio Aberto/Fechado (OCP).

---

## 5. Melhorias de Engenharia Implementadas

Além dos requisitos centrais do desafio, foram implementados três padrões complementares de confiabilidade e observabilidade:

### 5.1. Transactional Outbox Pattern
* **Problema:** Mitigar o problema de escrita dupla (*dual-write*), onde a compra é atualizada no banco, mas a publicação no RabbitMQ falha (ou vice-versa) por instabilidade de rede.
* **Solução:** Na publicação da compra, o evento é persistido na tabela `outbox_evento` na mesma transação relacional da compra. O componente `OutboxRelayWorker` varre eventos pendentes de forma assíncrona com intervalo configurado, despacha para a exchange e marca como processado, garantindo consistência eventual e tolerância a quedas temporárias do broker.

### 5.2. Rastreabilidade com Correlation ID
* O filtro `CorrelationIdFilter` captura ou gera um `X-Correlation-ID` no cabeçalho HTTP de cada requisição e o propaga para o `MDC` do SLF4J/Logback.
* Ao despachar a mensagem no RabbitMQ, o correlation ID é incluído nas propriedades/headers da mensagem.
* O consumidor do RabbitMQ extrai o cabeçalho e injeta no MDC local, garantindo que o rastreamento da operação permaneça consistente em todos os logs, desde o clique no frontend até a execução do listener assíncrono.

### 5.3. Paginação com Pageable no Domínio
* Implementada paginação estruturada nos endpoints de listagem de compras (`/api/compras-diretas`) e propostas (`/api/compras-diretas/{id}/propostas`).
* A paginação foi modelada no domínio através dos objetos de valor `Paginacao` e `Pagina<T>`, mantendo o contrato desacoplado de classes específicas de frameworks (como `org.springframework.data.domain.Page`).

---

## 6. Testes Automatizados

A suíte de testes automatizados conta com testes unitários focados nas regras de negócio, estratégias e componentes de integração:
* **Padrão:** AAA (Arrange, Act, Assert) com JUnit 5 e Mockito.
* **Escopo:** Use Cases de publicação e apuração, estratégias polimórficas de apuração, listeners e produtores RabbitMQ, `OutboxRelayWorker`, filtros de rastreabilidade e estrutura de paginação.

Para executar toda a suíte de testes:
```bash
mvn clean test
```

---

## 7. Referências Técnicas para Estudo e Aprofundamento

Material de referência e boas práticas adotadas como base arquitetural neste projeto para consulta futura da equipe:

* **Arquitetura Limpa:** *Clean Architecture: A Craftsman's Guide to Software Structure and Design* — Robert C. Martin (Uncle Bob). Especial atenção aos capítulos sobre Regra de Dependência e Use Cases puros.
* **Arquitetura Hexagonal:** *Hexagonal Architecture (Ports and Adapters)* — Alistair Cockburn (2005). Princípios de isolamento do núcleo da aplicação por meio de portas e adaptadores.
* **Padrões de Integração Corporativa:** *Enterprise Integration Patterns* — Gregor Hohpe e Bobby Woolf. Padrões *Dead Letter Channel*, *Idempotent Receiver* e *Event-Driven Architecture*.
* **Sistemas Distribuídos e Confiabilidade:** *Designing Data-Intensive Applications (DDIA)* — Martin Kleppmann. Capítulo sobre mensageria, tolerância a falhas e garantias de entrega (*at-least-once* vs *exactly-once*).
* **Design Patterns:** *Design Patterns: Elements of Reusable Object-Oriented Software* — Gang of Four (GoF). Aplicação prática dos padrões *Strategy Pattern* e *Factory Method*.

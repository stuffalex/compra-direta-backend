# Guia de Demonstração Técnica e Decisões de Arquitetura
**Desafio Técnico — Sistema de Compra Direta**  
**Autora:** Alexya  
**Branch:** `alexya/notificacao-rabbitmq`

---

## 📑 Sumário
1. [Instruções de Deploy e Execução](#1-instruções-de-deploy-e-execução)
2. [Reset do Ambiente de Demonstração](#2-reset-do-ambiente-de-demonstração)
3. [Catálogo de Dados das Seeds](#3-catálogo-de-dados-das-seeds)
4. [Roteiro Passo a Passo da Apresentação Técnica](#4-roteiro-passo-a-passo-da-apresentação-técnica)
5. [Decisões Arquiteturais e Fundamentos de Engenharia](#5-decisões-arquiteturais-e-fundamentos-de-engenharia)
6. [Oportunidades de Melhoria e Diferenciais (Pitch Técnico)](#6-oportunidades-de-melhoria-e-diferenciais-pitch-técnico)
7. [Referências Bibliográficas e Leituras Recomendadas](#7-referências-bibliográficas-e-leituras-recomendadas)

---

## 1. Instruções de Deploy e Execução

A aplicação é dividida em três pilares: infraestrutura (Docker), backend (Spring Boot 3.2.5 / Java 17) e frontend (Vue 2 / Vuetify).

### Passo 1: Subir a Infraestrutura (Docker)
No diretório `backend/`, inicie os containers do PostgreSQL e do RabbitMQ:
```bash
cd backend
docker compose up -d
```
* **PostgreSQL:** `localhost:5432` (database: `compra_direta`, user/password: `compra_direta`)
* **RabbitMQ AMQP:** `localhost:5672` (guest/guest)
* **RabbitMQ Management Console:** `http://localhost:15672` (guest/guest)

### Passo 2: Subir a API Backend
No diretório `backend/`:
```bash
mvn spring-boot:run
```
* O Liquibase executará as migrações estruturais e carregará as cargas de dados e seeds automaticamente (`changelog-001`, `changelog-002`, `changelog-003`).
* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **API Docs OpenAPI:** `http://localhost:8080/v3/api-docs`

### Passo 3: Subir o Frontend
No diretório `frontend/`:
```bash
npm install
npm run serve
```
* **Interface Web:** `http://localhost:8081` (ou a porta informada pelo CLI do Vue).

---

## 2. Reset do Ambiente de Demonstração

Para reiniciar o banco de dados e as filas do RabbitMQ de volta ao estado inicial das seeds a qualquer momento (antes ou depois dos testes):

```bash
cd backend
./reset-database.sh
```
O script destrói os containers com os volumes (`docker compose down -v`) e recria os serviços limpos. Na próxima subida do backend, o Liquibase repopula todos os registros e propostas.

---

## 3. Catálogo de Dados das Seeds

Foram configurados dados realistas para garantir a demonstração dos 3 critérios de julgamento e do fluxo assíncrono de mensageria:

| ID | Número Compra | Modalidade | Critério | Status Inicial | Convidados | Propostas | Finalidade na Demo |
|:---|:---|:---|:---|:---|:---|:---|:---|
| **3** | `CD-0003/2026` | Dispensa de Licitação | `MENOR_PRECO` | `PUBLICADA` | 4 fornecedores | 4 propostas | **Strategy Menor Preço**: Vencedor esperado: *Gamma Tecnologia* (R$ 13.900,00) |
| **4** | `CD-0004/2026` | Dispensa de Licitação | `MAIOR_DESCONTO` | `PUBLICADA` | 4 fornecedores | 4 propostas | **Strategy Maior Desconto**: Vencedor esperado: *Pedro Santos ME* (15.00% de desconto) |
| **5** | `CD-0005/2026` | Inexigibilidade | `NAO_SE_APLICA` | `PUBLICADA` | 1 fornecedor | 1 proposta | **Strategy Inexigibilidade**: Validação de exclusividade e homologação direta do *CyberShield Brasil* |
| **6** | `CD-0006/2026` | Dispensa de Licitação | `MENOR_PRECO` | `RASCUNHO` | 3 fornecedores | 0 propostas | **RabbitMQ Live**: Clicar em *Publicar*, verificar evento na fila e geração das 3 notificações |

*(Além destas, as compras 1 e 2 originais do projeto permanecem intactas).*

---

## 4. Roteiro Passo a Passo da Apresentação Técnica

Siga a estrutura em 3 etapas exigida no desafio:

```
[1] Deploy e Setup ──> [2] Execução dos Fluxos ──> [3] Processo de Engenharia
```

### Etapa 1: Apresentação do Setup
* Mostre os containers rodando no Docker (`docker ps`).
* Mostre a inicialização do backend com logs do Liquibase executando os changelogs de carga de dados.
* Abra o painel do RabbitMQ (`http://localhost:15672`) na aba **Queues** para mostrar as filas criadas:
  * `compra-direta.publicada.notificacao.queue`
  * `compra-direta.publicada.notificacao.dlq`

---

### Etapa 2: Demonstração Funcional das Features

#### A) Feature 1 — Notificação via RabbitMQ
1. Abra a lista de compras diretas no Swagger ou no Frontend e localize a **`CD-0006/2026`** (ID 6, status `RASCUNHO`).
2. Dispare a publicação da compra:
   * **Endpoint:** `POST /api/compras-diretas/6/publicar`
3. Aponte no log da aplicação:
   * O produtor disparando o evento: `Publicando evento de compra direta publicada: compraId=6, numero=CD-0006/2026`.
   * A mensagem transitando pela exchange e caindo na fila.
   * O listener consumindo: `Recebido evento de publicação para compra: compraId=6`.
4. Consulte as notificações geradas:
   * **Endpoint:** `GET /api/compras-diretas/6/notificacoes`
   * Comprove que os 3 fornecedores convidados receberam a notificação com status `ENVIADA` e data/hora registrada.
5. *(Opcional)* Mostre a **Idempotência**:
   * Explique que, caso a mensagem seja reentregue pelo broker, o consumidor verifica `existePorCompraDiretaEFornecedor`, garantindo que nenhuma notificação duplicada seja gravada no banco.

#### B) Feature 2 — Apuração de Vencedor com Strategy Pattern
1. **Cenário 1 — Menor Preço (`CD-0003/2026`):**
   * Consulte as propostas: `GET /api/compras-diretas/3/propostas` (valores: R$ 15.500, R$ 14.200, R$ 13.900 e R$ 16.000).
   * Execute: `POST /api/compras-diretas/3/apurar-vencedor`.
   * Demonstre o resultado: Status foi para `HOMOLOGADA` e o fornecedor vencedor foi o ID 3 (*Gamma Tecnologia*, menor valor: R$ 13.900,00).
2. **Cenário 2 — Maior Desconto (`CD-0004/2026`):**
   * Consulte as propostas: `GET /api/compras-diretas/4/propostas` (descontos: 8.5%, 15.0%, 12.3% e 5.0%).
   * Execute: `POST /api/compras-diretas/4/apurar-vencedor`.
   * Demonstre o resultado: Status `HOMOLOGADA` e vencedor foi o ID 6 (*Pedro Santos ME*, maior desconto: 15.00%).
3. **Cenário 3 — Inexigibilidade / Não Se Aplica (`CD-0005/2026`):**
   * Consulte a proposta: `GET /api/compras-diretas/5/propostas` (apenas 1 proposta exclusiva sem valor nem desconto).
   * Execute: `POST /api/compras-diretas/5/apurar-vencedor`.
   * Demonstre o resultado: A estratégia validou a exclusividade (falharia caso houvesse 0 ou mais de 1 proposta) e homologou diretamente o ID 11 (*CyberShield Brasil*).

---

### Etapa 3: Explicação do Processo de Desenvolvimento

Nesta fase, destaque a maturidade de engenharia e as escolhas de design:

1. **Clean Architecture e Domínio Puro:**
   * O pacote `domain` não possui nenhuma importação de frameworks (sem anotações Spring como `@Service`, `@Component` ou `@Autowired`, e sem classes do RabbitMQ).
   * O use case `PublicarCompraDiretaUseCase` comunica-se com o mundo externo através de uma **Porta de Saída** (interface `INotificacaoPublicacaoProducer`).
2. **Inversão de Dependências (DIP) e Composition Root:**
   * A montagem e injeção dos componentes ocorre estritamente na camada de aplicação através das factories em `application/factory/` (`CompraDiretaUseCaseFactory`, `AmqpFactory`, `ApuracaoStrategyFactory`).
3. **Strategy Pattern e Princípio Aberto-Fechado (OCP):**
   * O `ApurarVencedorCompraDiretaUseCase` não possui `if` nem `switch` para inspecionar critérios. A resolução é polimórfica via `Map<CriterioJulgamento, IApuracaoVencedorStrategy>`. Adicionar um novo critério no futuro significa apenas criar uma nova classe sem tocar no use case.
4. **Resiliência em Mensageria (DLQ e No-Requeue):**
   * Configuração de Direct Exchange durável conectada a uma Dead Letter Exchange (`compra-direta.dlx`) e Dead Letter Queue (`compra-direta.publicada.notificacao.dlq`).
   * Configuração de `default-requeue-rejected: false` para evitar consumo em loop (*poison pill*): mensagens com erro irrecuperável são despachadas automaticamente para a DLQ para auditoria.
5. **Qualidade e Cobertura de Testes Unitários:**
   * Suíte com **41 testes unitários** automatizados com JUnit 5 e Mockito seguindo o padrão AAA (Arrange, Act, Assert).
   * 100% de sucesso sem depender de bancos externos ou brokers para validação de regras de negócio.
6. **Fluxo de Trabalho Git e Commits Atômicos:**
   * Branches isoladas criadas a partir da branch default (`alexya/notificacao-rabbitmq`), sem commits diretos na `main`.
   * Commits atômicos no padrão Conventional Commits (`feat`, `test`, `chore`, `refactor`).

---

## 5. Decisões Arquiteturais e Fundamentos de Engenharia

### Por que usar uma Porta Hexagonal em vez de injetar o `RabbitTemplate` no Use Case?
Se injetássemos `RabbitTemplate` dentro do `PublicarCompraDiretaUseCase`, o núcleo de negócio do sistema ficaria acoplado à biblioteca do Spring AMQP. Se a organização decidisse migrar para Apache Kafka, AWS SQS ou gRPC, o caso de uso de publicação precisaria ser alterado e retestado. Com a porta `INotificacaoPublicacaoProducer`, alteramos apenas a implementação do adaptador na camada de infraestrutura, mantendo o domínio intacto.

### Por que desativar o requeue automático no listener RabbitMQ?
No Spring AMQP, o comportamento padrão ao ocorrer uma exceção não tratada é dar NACK com `requeue = true`. Isso faz a mensagem voltar imediatamente para a fila, disparando uma tentativa em loop infinito que satura CPU e logs. Configurando `default-requeue-rejected: false` e atrelando a fila a uma Dead Letter Exchange (DLX), a mensagem é roteada de forma segura para a DLQ.

### Por que validar a idempotência na persistência?
Em sistemas distribuídos de mensageria, a garantia de entrega da rede é **At-Least-Once** (pelo menos uma vez). Quedas temporárias de rede podem fazer com que o consumidor processe a mensagem com sucesso mas perca a conexão antes de emitir o ACK, fazendo o broker reenviar a mensagem. A verificação `existePorCompraDiretaEFornecedor` garante que o processamento seja idempotente.

---

## 6. Oportunidades de Melhoria e Diferenciais (Pitch Técnico)

Ao ser questionada sobre potenciais melhorias futuras para a aplicação, mencione:

1. **Transactional Outbox Pattern:**
   * *Diagnóstico:* Atualmente, a compra direta é persistida no banco e em seguida a mensagem é disparada ao RabbitMQ. Se o broker estiver fora do ar exatamente no milissegundo pós-commit do banco, a notificação não é enviada (*Dual Write Problem*).
   * *Solução:* Gravar o evento na tabela de *outbox* na mesma transação atômica do banco de dados e ter um worker (via Debezium/CDC ou polling) publicando no RabbitMQ com garantia transacional.
2. **Observabilidade e Tracing Distribuído:**
   * Implementação de correlation-id / trace-id via **Micrometer Tracing** ou **OpenTelemetry**, propagando o cabeçalho no envelope AMQP para correlacionar o log da requisição HTTP de publicação com o log de consumo do listener.
3. **Paginação nos Endpoints de Consulta:**
   * Inclusão de `Pageable` do Spring Data nos endpoints de listagem de compras e de propostas para garantir escalabilidade sob grandes volumes.

---

## 7. Referências Bibliográficas e Leituras Recomendadas

* **Arquitetura Limpa:** *Clean Architecture: A Craftsman's Guide to Software Structure and Design* — Robert C. Martin (Uncle Bob).
* **Arquitetura Hexagonal:** *Hexagonal Architecture (Ports and Adapters)* — Alistair Cockburn (2005).
* **Injeção de Dependências:** *Dependency Injection Principles, Practices, and Patterns* — Steven van Deursen e Mark Seemann.
* **Padrões de Integração:** *Enterprise Integration Patterns* — Gregor Hohpe e Bobby Woolf.
* **Sistemas Distribuídos e Idempotência:** *Designing Data-Intensive Applications (DDIA)* — Martin Kleppmann (Capítulo 11: Stream Processing).
* **Design Patterns:** *Design Patterns: Elements of Reusable Object-Oriented Software* — Gang of Four (GoF).

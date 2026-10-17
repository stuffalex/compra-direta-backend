# Desafio Técnico — Compra Direta

## Contexto

Você recebeu a base de um sistema de cadastro de **Compra Direta** — um processo de compras
públicas que dispensa ou torna inexigível a licitação tradicional. A base já cobre o cadastro
completo (criar, listar, editar, cancelar, convidar fornecedores, registrar propostas) e está
funcional de ponta a ponta (backend + frontend + banco de dados).

Faltam duas features, descritas abaixo. Ambas têm um ponto de entrada já sinalizado com `// TODO`
no código — procure por eles em `PublicarCompraDiretaUseCase` e `ApurarVencedorCompraDiretaUseCase`.

## Regras de negócio do domínio

- **Modalidade** (`DISPENSA_DE_LICITACAO` ou `INEXIGIBILIDADE`) e **critério de julgamento**
  (`NAO_SE_APLICA`, `MENOR_PRECO`, `MAIOR_DESCONTO`) são vinculados:
  - `INEXIGIBILIDADE` só aceita critério `NAO_SE_APLICA` e exige **exatamente 1** fornecedor
    convidado (fonte exclusiva, sem competição).
  - `DISPENSA_DE_LICITACAO` só aceita `MENOR_PRECO` ou `MAIOR_DESCONTO` e exige **no mínimo 3**
    fornecedores convidados.
- Uma compra direta nasce em `RASCUNHO`, pode ser editada enquanto nesse status, e segue o fluxo:
  `RASCUNHO` → `PUBLICADA` → (recebe propostas dos fornecedores convidados) → `HOMOLOGADA`.
  `CANCELADA` pode ocorrer a qualquer momento antes da homologação.
- Fornecedores convidados enviam **propostas**: um valor ofertado (quando o critério é
  `MENOR_PRECO`), um percentual de desconto (quando é `MAIOR_DESCONTO`), ou nenhum dos dois
  (quando é `NAO_SE_APLICA`) — sempre com um prazo de entrega em dias.
- Isso já está todo implementado. Olhe `domain/usecase/compradireta/**` e
  `domain/usecase/proposta/**` para entender o modelo antes de mexer nas duas features abaixo.

---

## Feature 1 — Notificar fornecedores da publicação via RabbitMQ

**Requisito de negócio:** quando uma compra direta é publicada (`PublicarCompraDiretaUseCase`),
todos os fornecedores convidados devem ser notificados da publicação. A notificação precisa ser
registrada (para consulta posterior via `GET /api/compras-diretas/{id}/notificacoes`, endpoint que
já existe e já tem a entidade `NotificacaoFornecedor` pronta em `domain/entity/entities`, com seu
data provider em `domain/interfaces/dataprovider` e a implementação em `adapter/gateway/dataprovider`).

**Requisito técnico:** a comunicação entre a publicação e o processamento da notificação deve
acontecer **estritamente via RabbitMQ** — nada de chamar um método diretamente. Isso implica, no
mínimo:
- Declarar a topologia (exchange, fila, e uma política de erro/DLQ) de alguma forma — veja como o
  projeto já configura outras infraestruturas em `application/config/`.
- Um produtor que publica uma mensagem quando a compra é publicada.
- Um consumidor que recebe a mensagem e persiste a notificação para cada fornecedor, de forma
  idempotente (reprocessar a mesma mensagem não deve duplicar notificações).

**Migalhas:**
- `PublicarCompraDiretaUseCase` está no pacote `domain`. O padrão usado neste projeto para o
  domínio acessar qualquer recurso externo (persistência, integrações) é através de uma **porta**
  (interface) definida no próprio domínio — dê uma olhada em `domain/interfaces/dataprovider/**`
  para ver esse padrão em ação antes de decidir como o use case vai disparar a notificação.

- Repare como o projeto monta a injeção de dependências: nenhum use case tem `@Autowired` nem
  anotação de componente Spring; tudo é montado em classes `@Configuration` dentro de
  `application/factory/`. Siga o mesmo padrão para o produtor e para o(s) bean(s) que o use case
  vai receber.

---

## Feature 2 — Apurar o vencedor com Strategy pattern

**Requisito de negócio:** depois que uma compra direta publicada recebe propostas, é preciso
apurar o fornecedor vencedor (`ApurarVencedorCompraDiretaUseCase`, chamado por
`POST /api/compras-diretas/{id}/apurar-vencedor`). A forma de decidir o vencedor depende do
critério de julgamento da compra:
- `MENOR_PRECO`: vence a proposta com o menor valor ofertado.
- `MAIOR_DESCONTO`: vence a proposta com o maior percentual de desconto.
- `NAO_SE_APLICA` (inexigibilidade): não há disputa — deve existir exatamente uma proposta, e ela é
  a vencedora diretamente.

**Requisito técnico:** use o padrão **Strategy**. Cada critério de julgamento deve ter sua própria
estratégia de apuração, e o use case não deve conter nenhum `if`/`switch` encadeado decidindo qual
algoritmo aplicar com base no critério — a decisão de qual estratégia usar deve ser resolvida de
forma polimórfica.

Repare que uma das estratégias (`NAO_SE_APLICA`) não é "comparar propostas e pegar a melhor" —
é uma regra qualitativamente diferente (validar exclusividade). Pense na interface de forma que
acomode as duas naturezas.

**Migalhas:**
- Veja como as regras de negócio reutilizáveis sem efeito colateral já aparecem neste projeto em
  `domain/usecase/extended/compradireta/validarregras/` — não é exatamente o mesmo padrão (aquilo
  é a convenção de "extended use case" do projeto), mas
  mostra onde esse tipo de lógica de domínio deveria morar.
- Assim como na Feature 1, pense em como as implementações concretas da estratégia vão ser
  descobertas e entregues ao use case sem anotações de framework espalhadas pelo domínio — o mesmo
  padrão de factories em `application/factory/` se aplica aqui.

---

## Critérios de avaliação

- Arquitetura limpa orientada a use case, com as camadas separadas conforme o restante do projeto
  (domínio livre de framework, adapters isolando infraestrutura, injeção de dependência via
  factories na camada de aplicação).
- Testes unitários das novas classes seguindo o padrão dos testes já existentes no projeto (JUnit 5
  + Mockito, AAA, nomenclatura `deve...Quando...Test`).
- RabbitMQ realmente usado como mecanismo de comunicação (obrigatório), com tratamento razoável de erro/reprocessamento (diferencial).
- Strategy pattern aplicado de forma que adicionar um quarto critério de julgamento no futuro não
  exigiria alterar o use case de apuração, apenas registrar uma nova implementação.
- Código consistente com as convenções já usadas no restante do projeto (nomenclatura, pacotes,
  estilo de DTOs e converters).
- Apresentar a solução na seguinte ordem: 1 - Demontrar deploy da aplicação (dependencias docker, run backend, run do frontend), 2 - Executar os fluxos das features solicitadas, em especial o strategy, devem haver compras prviamente criadas com os 3 cenários prontas para a demonstração. 3 - Explicar como foi o processo de desenvolvimento.
- Diferenciais: 1 - Reportar na apresentação possíveis oportunidades melhorias no código implementado. 2 - Implementar oportunidades de melhorias no código do desafio.
- Melhorias de arquitetura, frameworks, clean code, testes, dentre outras são bem vindas, fique a vontade, apenas não descaracterize a exessencia de domínio, modelagem de dados e regras de negócio.

## Entrega

Suba seu código em um repositório Git (pode ser privado, nos dê acesso), incluindo os testes. Inclua um parágrafo curto, se quiser, explicando decisões de design
que valham a pena destacar.

# Icompras - Ecomence

O Icompras é um sistema desenvolvido com foco em arquitetura de microservices para gerenciamento do processo de faturamento dentro de um ecossistema de e-commerce. A aplicação utiliza comunicação assíncrona entre serviços através do Apache Kafka, garantindo maior escalabilidade, desacoplamento e robustez no processamento de pedidos e pagamentos.

Com o Icompras, é possível gerenciar clientes, produtos e pedidos, além de automatizar a atualização de status de pedidos com base em eventos de pagamento recebidos via callback e mensageria.

## Funcionalidades

- Publicação e Consumo de Eventos
    * O microserviço de faturamento se conecta aos tópicos do Kafka para publicar eventos como confirmação de pagamento e também consome eventos relacionados ao status do pedido, atualizando-o automaticamente conforme necessário.

- Gerenciamento de Status de Pedido
  * Quando um pagamento é recebido, o sistema atualiza o status do pedido e publica essa informação em um tópico do Kafka, permitindo que outros microservices que escutam esse tópico reajam à atualização.

- Interação com Dados do Pedido
  * O sistema envia e processa detalhes dos pedidos, como itens e informações relevantes, permitindo consultas completas sobre o estado dos pedidos.

- Automação do Processo de Faturamento
  * Através do uso de callback de pagamentos e mensageria com Kafka, o sistema automatiza a confirmação de pagamentos e o fluxo de faturamento sem necessidade de intervenção manual.

- Arquitetura de Microservices
  * O sistema é dividido em microservices independentes (clientes, produtos e pedidos), garantindo escalabilidade, desacoplamento e facilidade de manutenção.


## Microservices do Sistema

- ms clientes
    * Responsável pelo gerenciamento de clientes, incluindo cadastro, consulta e inativação.

- ms produtos
    * Responsável pelo gerenciamento de produtos, incluindo cadastro, consulta e controle de disponibilidade.

- ms pedidos
    * Responsável pela criação de pedidos, processamento de pagamentos, callback financeiro e integração com Kafka para atualização de status.


## Pré-requisitos

- Java JDK 21 ou superior
- Docker
- Apache Kafka
- Maven
- PostgreSQL


## Dependências Utilizadas

- Spring Boot
- Spring Kafka
- Spring Data JPA
- Spring Cloud
- Lombok
- MapStruct
- PostgreSQL
- MinIO
- JasperReports


## Teste com Postman
Para realizar os testes dos endpoint foi usado [Postman](https://www.postman.com/), caso não o tenha instalado clique em seu nome para ser direcionado a pagina de download.


## Rotas

### MS Clientes (porta 8082)

| Action            | Endpoint                                | Description                                                     |
|-------------------|------------------------------------------|-----------------------------------------------------------------|
| Cadastrar Cliente | http://localhost:8082/clientes           | Realiza o cadastro de um novo cliente no sistema                |
| Buscar Cliente    | http://localhost:8082/clientes/{codigo}  | Recupera os dados de um cliente pelo código                     |
| Inativar Cliente  | http://localhost:8082/clientes/{codigo}  | Inativa o cliente, impedindo que ele realize novos pedidos      |


### MS Produtos (porta 8081)

| Action            | Endpoint                                | Description                                                       |
|-------------------|------------------------------------------|-------------------------------------------------------------------|
| Cadastrar Produto | http://localhost:8081/produtos           | Realiza o cadastro de um novo produto                             |
| Buscar Produto    | http://localhost:8081/produtos/{codigo}  | Recupera os dados de um produto pelo código                       |
| Inativar Produto  | http://localhost:8081/produtos/{codigo}  | Inativa o produto, impedindo que seja utilizado em novos pedidos  |


### MS Pedidos (porta 8080)

| Action                   | Endpoint                                          | Description                                                                 |
|--------------------------|---------------------------------------------------|-----------------------------------------------------------------------------|
| Criar Pedido             | http://localhost:8080/pedidos                     | Realiza a criação de um novo pedido                                         |
| Adicionar Novo Pagamento | http://localhost:8080/pedidos/pagamentos          | Registra um novo pagamento vinculado ao pedido                              |
| Callback de Pagamentos   | http://localhost:8080/pedidos/callback-pagamentos | Recebe notificações do provedor de pagamento e atualiza o status do pedido  |
| Detalhes do Pedido       | http://localhost:8080/pedidos/{codigo}            | Retorna os detalhes completos de um pedido                                  |


## Observação sobre o endpoint Callback de Pagamentos

O endpoint de callback de pagamentos desempenha um papel crucial na comunicação entre o sistema e o banco ou provedor de pagamentos.

- Atualização de Status
  * Este endpoint recebe notificações sobre o status do pagamento (aprovado ou não) e atualiza automaticamente o status do pedido no banco de dados.

- Automação
  * Com o callback, o processo se torna automatizado, eliminando a necessidade de intervenção manual para verificar se um pagamento foi realizado.

- Gerenciamento de Falhas
  * Caso um pagamento falhe, o sistema é notificado por meio desse endpoint, permitindo que o cliente seja orientado a tentar novamente.

Em resumo, o endpoint de callback de pagamentos é vital para a sincronização entre o sistema e as transações financeiras, assegurando que todo o fluxo de faturamento e gerenciamento de pedidos seja realizado de forma eficiente em uma arquitetura orientada a eventos.


## Configuração do Banco de Dados

O projeto utiliza o PostgreSQL como banco de dados principal para persistência das informações de clientes, produtos, pedidos e pagamentos.


## License

This project is licensed under the MIT License.

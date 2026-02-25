# Icompras - Ecomence

O Icompras é um sistema desenvolvido com foco em arquitetura de microservices para gerenciamento do processo de faturamento dentro de um ecossistema de e-commerce. A aplicação utiliza comunicação assíncrona entre serviços através do Apache Kafka, garantindo maior escalabilidade, desacoplamento e robustez no processamento de pedidos, pagamentos e faturamento.

Com o Icompras, é possível gerenciar clientes, produtos, pedidos e faturamento, além de automatizar a geração de nota fiscal e atualização de status dos pedidos com base em eventos assíncronos.

## Funcionalidades

- Publicação e Consumo de Eventos
    * Os microservices se comunicam através do Apache Kafka, publicando e consumindo eventos como pagamento aprovado e pedido faturado.

- Gerenciamento de Status de Pedido
  * Quando um pagamento é confirmado, o sistema atualiza o status do pedido e publica essa informação em um tópico do Kafka, permitindo que outros microservices reajam à atualização de forma assíncrona.

- Geração de Nota Fiscal
  * O microserviço de faturamento consome eventos de pedidos pagos, gera automaticamente a nota fiscal em PDF utilizando JasperReports e vincula ao pedido.

- Upload e Recuperação de Arquivos
  * As notas fiscais são armazenadas em um bucket (MinIO), permitindo upload seguro e geração de URLs assinadas para acesso ao arquivo.

- Automação do Processo de Faturamento
  * Todo o fluxo de faturamento é automatizado: pagamento aprovado → geração da nota fiscal → upload no bucket → publicação do evento de pedido faturado.

- Arquitetura de Microservices
  * O sistema é dividido em microservices independentes (clientes, produtos, pedidos e faturamento), garantindo escalabilidade, desacoplamento e facilidade de manutenção.


## Microservices do Sistema

- ms clientes
    * Responsável pelo gerenciamento de clientes, incluindo cadastro, consulta e inativação.

- ms produtos
    * Responsável pelo gerenciamento de produtos, incluindo cadastro, consulta e controle de disponibilidade.

- ms pedidos
    * Responsável pela criação de pedidos, processamento de pagamentos, callback financeiro e publicação de eventos no Kafka.

- ms faturamento
    * Responsável por consumir eventos de pedidos pagos, gerar notas fiscais em PDF, realizar upload no MinIO, recuperar URLs dos arquivos e publicar o status de pedido faturado no Kafka.


## Pré-requisitos

- Java JDK 21 ou superior
- Docker
- Apache Kafka
- Maven
- PostgreSQL
- MinIO


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


### MS Faturamento (porta configurável)

| Action          | Endpoint                         | Description                                                                 |
|-----------------|----------------------------------|-----------------------------------------------------------------------------|
| Upload de Arquivo | http://localhost:{porta}/bucket | Realiza o upload de arquivos (ex: nota fiscal PDF) para o bucket MinIO      |
| Obter URL do Arquivo | http://localhost:{porta}/bucket?filename=nome | Gera uma URL assinada para acesso ao arquivo armazenado no bucket           |


## Observação sobre o fluxo de Faturamento

O microserviço de faturamento é orientado a eventos e integrado ao Apache Kafka.

- Consumo de Eventos
  * O serviço escuta o tópico de pedidos pagos e inicia automaticamente o processo de faturamento ao receber a mensagem.

- Geração de Nota Fiscal
  * Após consumir o evento, o sistema gera a nota fiscal em PDF utilizando JasperReports com base nos dados do pedido.

- Armazenamento no Bucket (MinIO)
  * A nota fiscal é enviada para um bucket de armazenamento e uma URL assinada é gerada para acesso seguro ao arquivo.

- Publicação do Pedido Faturado
  * Após gerar a nota fiscal, o serviço publica um novo evento no Kafka informando que o pedido foi faturado, incluindo a URL da nota fiscal.


## Configuração do Banco de Dados

O projeto utiliza o PostgreSQL como banco de dados principal para persistência das informações dos microservices. O microserviço de faturamento utiliza armazenamento em bucket (MinIO) para arquivos de nota fiscal.


## License

This project is licensed under the MIT License.

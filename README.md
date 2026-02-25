# Icompras - Ecomence

O Icompras é um sistema desenvolvido com foco em arquitetura de microservices para gerenciamento do processo de faturamento e envio de pedidos dentro de um ecossistema de e-commerce. A aplicação utiliza Apache Kafka para implementar comunicação assíncrona entre os serviços, garantindo desacoplamento, escalabilidade e robustez no processamento de pedidos.

O sistema é composto por múltiplos microservices responsáveis por clientes, produtos, pedidos, faturamento, logística e infraestrutura, automatizando todo o fluxo desde a criação do pedido até o envio ao cliente.

## Funcionalidades

- Publicação e Consumo de Eventos
    * Os microservices se comunicam de forma assíncrona através do Apache Kafka, publicando e consumindo eventos como pagamento aprovado, pedido faturado e pedido enviado.

- Gerenciamento de Status de Pedido
  * O sistema atualiza automaticamente o status do pedido com base nos eventos recebidos (pago, faturado e enviado).

- Geração de Nota Fiscal
  * O microserviço de faturamento consome eventos de pedidos pagos, gera automaticamente a nota fiscal em PDF utilizando JasperReports e armazena o arquivo no bucket.

- Upload e Recuperação de Arquivos
  * As notas fiscais são enviadas para o MinIO e podem ser recuperadas através de URLs assinadas.

- Automação Logística
  * Após o faturamento do pedido, o microserviço de logística consome o evento, gera um código de rastreio e atualiza o status do pedido para ENVIADO.

- Infraestrutura Containerizada
  * O microserviço de serviço é responsável por subir os containers necessários, incluindo bancos de dados e serviços de infraestrutura utilizando Docker.


## Microservices do Sistema

- ms clientes
    * Responsável pelo cadastro, consulta e inativação de clientes.

- ms produtos
    * Responsável pelo cadastro, consulta e inativação de produtos.

- ms pedidos
    * Responsável pela criação de pedidos, processamento de pagamentos, callback financeiro e publicação de eventos no Kafka.

- ms faturamento
    * Responsável por consumir eventos de pedidos pagos, gerar notas fiscais em PDF, realizar upload no MinIO, recuperar URLs dos arquivos e publicar o status de pedido faturado.

- ms logistica
    * Responsável por consumir eventos de pedidos faturados, gerar código de rastreio e atualizar o status do pedido para ENVIADO.

- ms servico
    * Responsável por construir e orquestrar os containers da aplicação, incluindo Kafka, bancos de dados e demais serviços de infraestrutura.


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

| Action            | Endpoint                               | Description                                                     |
|-------------------|----------------------------------------|-----------------------------------------------------------------|
| Cadastrar Cliente | http://localhost:8082/clientes         | Realiza o cadastro de um novo cliente no sistema                |
| Buscar Cliente    | http://localhost:8082/clientes/{codigo}| Recupera os dados de um cliente pelo código                     |
| Inativar Cliente  | http://localhost:8082/clientes/{codigo}| Inativa o cliente, impedindo que ele realize novos pedidos      |


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


### MS Faturamento

| Action             | Endpoint                          | Description                                                                 |
|--------------------|-----------------------------------|-----------------------------------------------------------------------------|
| Upload de Arquivo  | http://localhost:{porta}/bucket   | Realiza upload da nota fiscal (PDF) para o bucket MinIO                     |
| Obter URL do Arquivo | http://localhost:{porta}/bucket?filename=nome | Gera uma URL assinada para acesso ao arquivo armazenado                     |


## Observação sobre o Endpoint de Callback de Pagamentos

O endpoint de callback de pagamentos desempenha um papel crucial na comunicação entre o sistema e o provedor de pagamentos.

- Atualização de Status
  * Recebe notificações do banco sobre o status do pagamento e atualiza automaticamente o status do pedido.

- Automação
  * Elimina a necessidade de verificação manual de pagamentos, tornando o processo mais rápido e eficiente.

- Gerenciamento de Falhas
  * Em caso de falha no pagamento, o sistema pode notificar o cliente para tentar novamente, melhorando a experiência do usuário.


## Fluxo Assíncrono do Sistema (Kafka)

1. O pedido é criado no ms-pedidos  
2. O pagamento é confirmado via callback  
3. Um evento de pedido pago é publicado no Kafka  
4. O ms-faturamento consome o evento e gera a nota fiscal  
5. A nota fiscal é armazenada no MinIO e a URL é gerada  
6. Um evento de pedido faturado é publicado no Kafka  
7. O ms-logistica consome o evento de faturamento  
8. O sistema gera um código de rastreio e atualiza o pedido para ENVIADO  


## Configuração do Banco de Dados

O projeto utiliza PostgreSQL como banco de dados principal para persistência dos dados dos microservices.  
O armazenamento de arquivos (notas fiscais) é realizado através do MinIO utilizando buckets.


## License

This project is licensed under the MIT License.

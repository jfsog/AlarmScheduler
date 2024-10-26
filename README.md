# AlarmScheduler

AlarmScheduler é uma API em Java para agendar alarmes que disparam ações em datas futuras, incluindo o envio de mensagens para o Discord via webhook. A API foi construída usando Spring Boot e integra MQTT, WebSocket, H2 Database e GraphQL para fornecer funcionalidades de agendamento, atualização e disparo de alarmes.

## Tecnologias
-   **Java 21**
-   **Spring Boot**
-   **GraphQL**
-   **H2 Database**
-   **MQTT (Paho MQTT Client)**
-   **WebSocket**
-   **Discord Webhook**

## Estrutura do Projeto
```src
src/
├── main/
│   ├── java/com/jfsog/AlarmScheduler/
│   │   ├── Services/
│   │   │   ├── WebhookService.java       # Envia mensagens via Webhook para o Discord
│   │   │   ├── MqttClientService.java    # Configurações e conexão MQTT
│   │   │   └── AlarmService.java         # Lógica principal de agendamento e disparo de alarmes
│   │   ├── Configuration/
│   │   │   └── GraphQlConfig.java        # Configurações do GraphQL, especificamente o Scalar DateTime
│   │   ├── Controller/
│   │   │   └── AlarmController.java      # Endpoints de API para manipulação de alarmes
│   │   ├── Data/
│   │   │   └── Alarm.java                # Classe de dados do Alarme
│   │   ├── repository/
│   │   │   └── AlarmRepository.java      # Repositório JPA para gerenciar alarmes no banco H2
│   │   └── AlarmSchedulerApplication.java # Classe principal da aplicação
│   └── resources/
│       ├── graphql/
│       │   └── Alarm.graphqls            # Schema do GraphQL para alarmes
│       └── application.yaml              # Configurações da aplicação
```

## Funcionalidades

1.  **Criação de Alarmes**: Permite ao usuário criar alarmes para uma data e hora específicas, associando uma ação (ex.: enviar mensagem para o Discord).
2.  **Disparo de Alarmes**: Alarme é disparado automaticamente na data configurada e uma mensagem é enviada via webhook.
3.  **Modificação de Alarmes**: Altera a data e hora dos alarmes futuros, bem como outras especificações.
4.  **Suporte a MQTT**: Integração com MQTT para monitorar e comunicar o status dos alarmes.
5.  **WebSocket**: Comunicação em tempo real.
6.  **Banco de Dados em Memória (H2)**: Persiste alarmes temporariamente para fácil gerenciamento.

## Configuração

1.  **Clone o repositório**:

```bash
git clone https://github.com/seu-usuario/AlarmScheduler.git
cd AlarmScheduler`
```
2.  **Configuração de Variáveis Sensíveis**:

-   Crie um arquivo `.env` na pasta raiz do projeto para armazenar o `DISCORD_WEBHOOK_URL`, que é a url obtida ao criar um webhook no seu servidor de Discord desejado.

4. **Broker MQTT**
   Este projeto usa MQTT para comunicação assíncrona entre os serviços. Um broker MQTT como o [**Mosquitto**](https://mosquitto.org/download/) é necessário para essa funcionalidade.
5.  **Execução da aplicação:**
```bash
./mvnw spring-boot:run
```
6.  **Acessando o GraphQL**:

-   A interface do GraphQL pode ser acessada em `http://localhost:8080/graphql`.

## Exemplos de Uso

### Criação de um Alarme

Exemplo de query para criar um alarme:
```bash
mutation {
  createAlarm(dateTime: "2024-10-30T12:00:00Z", action: "Enviar mensagem para o Discord") {
    id
    dateTime
    action
  }
}
```
### Consulta de Alarmes
```bash
query {
  getAllAlarms {
    id
    dateTime
    action
    ringed
  }
}
```
## Conclusões
### Resultados Esperados

Ao executar o projeto **AlarmScheduler**, espera-se que a aplicação:

-   **Dispare alarmes automaticamente** nas datas e horas configuradas, enviando mensagens ao Discord por meio de webhooks.
-   **Permita a criação, consulta, atualização e exclusão de alarmes** através de uma interface GraphQL.
-   **Notifique sobre o disparo de alarmes em tempo real** usando WebSocket, com a possibilidade de expandir a aplicação para outros tipos de notificações.
-   **Mantenha uma conexão MQTT para sinalizar e monitorar os alarmes**, com integração a um broker configurado via `application.yaml`.

### Possibilidades de Expansão

Este projeto tem várias possibilidades de expansão, incluindo:

1.  **Integração com Serviços de Notificação Adicionais**: além do Discord, é possível configurar notificações para Slack, Telegram, whatsapp ou email.
2.  **Persistência em Banco de Dados Permanente**: atualmente, o projeto usa H2 (em memória). Pode ser facilmente adaptado para bancos de dados como PostgreSQL ou MySQL, caso haja necessidade de persistência a longo prazo.
3.  **Aprimoramento do MQTT**: com o MQTT, é possível criar tópicos específicos para diferentes tipos de alarmes, permitindo maior flexibilidade e controle sobre notificações.
4.  **Escalonamento e Distribuição**: o projeto pode ser escalado e distribuído para rodar em múltiplos servidores, permitindo alta disponibilidade e resiliência em ambientes de produção.

### Tecnologias Utilizadas

-   **Java 21**: versão de linguagem para aproveitar os recursos mais modernos.
-   **Spring Boot**: Framework para configuração e gerenciamento da aplicação, incluindo suporte a tarefas agendadas e integração com MQTT e GraphQL.
-   **GraphQL**: Gerenciamento de operações CRUD e consultas personalizadas para os alarmes.
-   **H2 Database**: banco de dados em memória para persistir alarmes temporariamente.
-   **MQTT (Paho MQTT Client)**: protocolo para comunicação em tempo real com outros serviços e dispositivos.
-   **WebSocket**: para notificações em tempo real.
-   **Discord Webhook**: integração para envio de mensagens ao Discord.
-   **dotenv**: gerenciamento de variáveis de ambiente para proteger dados sensíveis, como URLs de webhook.

### Aprendizado e Experiência

Trabalhar no **AlarmScheduler** trouxe diversos aprendizados importantes:

-   **Integração com Múltiplos Protocolos**: A aplicação exigiu a compreensão e configuração de vários protocolos de comunicação (HTTP, MQTT, WebSocket), proporcionando uma visão mais ampla de como conectar diferentes sistemas.
-   **Gerenciamento de Variáveis Sensíveis**: Implementar a proteção de informações sensíveis com variáveis de ambiente e `dotenv` foi uma experiência valiosa para práticas seguras em aplicações.
-   **GraphQL**: A implementação de GraphQL permitiu um entendimento prático de como fornecer APIs flexíveis, possibilitando consultas específicas conforme a necessidade do cliente.
-   **Agendamento com Spring**: Aprendi a usar a anotação `@Scheduled` para agendar tarefas no Spring Boot, útil para a criação de cron jobs e tarefas automáticas.
-   **MQTT e Comunicação Assíncrona**: Com o MQTT, foi possível entender melhor o funcionamento da comunicação assíncrona e as vantagens de utilizar esse protocolo em aplicações distribuídas.
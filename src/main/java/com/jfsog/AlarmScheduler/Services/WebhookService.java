package com.jfsog.AlarmScheduler.Services;

import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WebhookService {
    private final MqttClientService mqttClientService;
    @Value("${DISCORD_WEBHOOK_URL}")
    private String DISCORD_WEBHOOK_URL;
    @Autowired
    public WebhookService(MqttClientService mqttClientService) {this.mqttClientService = mqttClientService;}
    @PostConstruct
    public void init() throws MqttException {
        mqttClientService.getMqttClient().subscribe("alarm/trigger", (t, m) -> sendWebhook(new String(m.getPayload())));
    }
    private void sendWebhook(String content) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            var req = HttpRequest.newBuilder()
                                 .uri(URI.create(DISCORD_WEBHOOK_URL))
                                 .POST(HttpRequest.BodyPublishers.ofString("{\"content\":\"" + content + "\"}"))
                                 .header("Content-Type", "application/json")
                                 .build();
            client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                  .thenAccept(res -> System.out.printf("Mensagem enviada para o discord: %d%n", res.statusCode()));
        }
    }
}

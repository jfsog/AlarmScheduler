package com.jfsog.AlarmScheduler.Services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqttClientService {
    @Getter
    private MqttClient mqttClient;
    @Value("${mqtt.broker-url}")
    private String brokerUrl;
    @PostConstruct
    public void init() {
        try {
            mqttClient = new MqttClient(brokerUrl, MqttClient.generateClientId());
            var options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            mqttClient.connect(options);
            System.out.println("MQTT Client connected to broker at " + brokerUrl);
        } catch (MqttException e) {
            throw new RuntimeException("Error connecting to broker", e);
        }
    }
    @PreDestroy
    public void disconnect() {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
                mqttClient.close();
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

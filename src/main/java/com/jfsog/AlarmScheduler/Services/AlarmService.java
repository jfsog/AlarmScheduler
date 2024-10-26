package com.jfsog.AlarmScheduler.Services;

import com.jfsog.AlarmScheduler.Data.Alarm;
import com.jfsog.AlarmScheduler.repository.AlarmRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AlarmService {
    public final MqttClientService mqttClientService;
    private final AlarmRepository alarmRepository;
    private final DateTimeFormatter formatter
            = new DateTimeFormatterFactory("dd-MM-yyyy HH:mm:ss").createDateTimeFormatter();
    @Autowired
    public AlarmService(AlarmRepository alarmRepository, MqttClientService mqttClientService) {
        this.alarmRepository = alarmRepository;
        this.mqttClientService = mqttClientService;
    }
    @Scheduled(fixedRate = 5000)
    private void triggerAlarm() {
        log.info("Disparo de alarme executado em {}", OffsetDateTime.now().format(formatter));
        alarmRepository.findAll()
                       .stream()
                       .filter(a -> a.getDateTime().isBefore(OffsetDateTime.now()))
                       .filter(a -> !a.getRinged())
                       .forEach(a -> {
                           try {
                               var str = a.getAction() + ": " + a.getDateTime().format(formatter);
                               MqttMessage message = new MqttMessage(str.getBytes());
                               mqttClientService.getMqttClient().publish("alarm/trigger", message);
                               a.setRinged(true);
                               alarmRepository.save(a);
                           } catch (MqttException e) {
                               throw new RuntimeException(e);
                           }
                       });
    }
    public Alarm getAlarm(@Argument UUID id) {
        return alarmRepository.findById(id).orElseThrow(() -> new RuntimeException("Alarm not found"));
    }
    public String deleteAlarm(@Argument UUID id) {
        var alarm = alarmRepository.findById(id).orElseThrow(() -> new RuntimeException("Alarm not found"));
        alarmRepository.delete(alarm);
        return alarm.getAction();
    }
    public List<Alarm> GetAllAlamrs() {
        return alarmRepository.findAll().stream().sorted(Comparator.comparing(Alarm::getDateTime)).toList();
    }
    public Alarm CreateAlarm(@Argument OffsetDateTime dateTime, @Argument String action) {
        return alarmRepository.saveAndFlush(Alarm.builder().action(action).dateTime(dateTime).ringed(false).build());
    }
    public Alarm updateAlarm(@Argument UUID id,
                             @Argument OffsetDateTime dateTime,
                             @Argument String action,
                             @Argument Boolean ringed) {
        var a = alarmRepository.findById(id).orElseThrow(() -> new RuntimeException("Alarm not found"));
        if (dateTime != null) a.setDateTime(dateTime);
        if (action != null) a.setAction(action);
        if (ringed != null) a.setRinged(ringed);
        return alarmRepository.save(a);
    }
}

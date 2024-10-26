package com.jfsog.AlarmScheduler.Services;

import com.jfsog.AlarmScheduler.Data.Alarm;
import com.jfsog.AlarmScheduler.repository.AlarmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;
    @Value("${DISCORD_WEBHOOK_URL}")
    private String DISCORD_WEBHOOK_URL;
    @Autowired
    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }
    @Scheduled(fixedRate = 5000)
    private void triggerAlarm() {
        log.info("Disparo de alarme executado em {}", OffsetDateTime.now());
        alarmRepository.findAll()
                       .stream()
                       .filter(a -> a.getDateTime().isBefore(OffsetDateTime.now()))
                       .filter(a -> !a.getRinged())
                       .forEach(a -> {
                           sendWebhook(a.getAction() + ": " + a.getDateTime().toZonedDateTime());
                           a.setRinged(true);
//                           a.setDateTime(OffsetDateTime.now().plusSeconds(3));
//                           a.setAction(a.getAction() + a.getAction().length());
                           alarmRepository.save(a);
                       });
    }
    private void sendWebhook(String content) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                                     .uri(URI.create(DISCORD_WEBHOOK_URL))
                                     .POST(HttpRequest.BodyPublishers.ofString("{\"content\":\"" + content + "\"}"))
                                     .header("Content-Type", "application/json")
                                     .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                  .thenAccept(response -> System.out.println(
                          "Mensagem enviada para o discord: " + response.statusCode()));
        }
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

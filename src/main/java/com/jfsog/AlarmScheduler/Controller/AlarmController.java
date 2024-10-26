package com.jfsog.AlarmScheduler.Controller;

import com.jfsog.AlarmScheduler.Services.Alarm;
import com.jfsog.AlarmScheduler.repository.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
public class AlarmController {
    private final AlarmRepository alarmRepository;
    @Autowired
    public AlarmController(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }
    @QueryMapping
    public Alarm getAlarm(@Argument UUID id) {
        return alarmRepository.findById(id).orElseThrow(() -> new RuntimeException("Alarm not found"));
    }
    @MutationMapping
    public String deleteAlarm(@Argument UUID id) {
        var alarm=alarmRepository.findById(id).orElseThrow(() -> new RuntimeException("Alarm not found"));
        alarmRepository.delete(alarm);
        return alarm.getAction();
    }
    @QueryMapping
    public List<Alarm> GetAllAlamrs() {
        return alarmRepository.findAll().stream().sorted(Comparator.comparing(Alarm::getDateTime)).toList();
    }
    @MutationMapping
    public Alarm CreateAlarm(@Argument OffsetDateTime dateTime, @Argument String action) {
        return alarmRepository.saveAndFlush(Alarm.builder()
                                                 .id(UUID.randomUUID())
                                                 .action(action)
                                                 .dateTime(dateTime)
                                                 .build());
    }
    @MutationMapping
    public Alarm updateAlarm(@Argument UUID id, @Argument OffsetDateTime dateTime, @Argument String action) {
        var a = alarmRepository.findById(id).orElseThrow(() -> new RuntimeException("Alarm not found"));
        if (dateTime != null) a.setDateTime(dateTime);
        if (action != null) a.setAction(action);
        return alarmRepository.save(a);
    }
}

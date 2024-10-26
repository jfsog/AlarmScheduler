package com.jfsog.AlarmScheduler.Controller;

import com.jfsog.AlarmScheduler.Data.Alarm;
import com.jfsog.AlarmScheduler.Services.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class AlarmController {
    private final AlarmService alarmService;
    @Autowired
    public AlarmController(AlarmService alarmService) {this.alarmService = alarmService;}
    @QueryMapping
    public Alarm getAlarm(@Argument UUID id) {
        return alarmService.getAlarm(id);
    }
    @MutationMapping
    public String deleteAlarm(@Argument UUID id) {
        return alarmService.deleteAlarm(id);
    }
    @QueryMapping
    public List<Alarm> GetAllAlamrs() {
        return alarmService.GetAllAlamrs();
    }
    @MutationMapping
    public Alarm CreateAlarm(@Argument OffsetDateTime dateTime, @Argument String action) {
        return alarmService.CreateAlarm(dateTime, action);
    }
    @MutationMapping
    public Alarm updateAlarm(@Argument UUID id,
                             @Argument OffsetDateTime dateTime,
                             @Argument String action,
                             @Argument Boolean ringed) {
        return alarmService.updateAlarm(id, dateTime, action, ringed);
    }
}
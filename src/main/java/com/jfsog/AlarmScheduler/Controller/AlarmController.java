package com.jfsog.AlarmScheduler.Controller;

import com.jfsog.AlarmScheduler.Services.Alarm;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class AlarmController {
    @QueryMapping
    public Alarm getAlarm(@Argument String id) {
        var time = OffsetDateTime.now();
        var val = time.plusMinutes(5);
        return Alarm.builder().id(UUID.randomUUID()).action("Teste action").time(val).build();
    }
    @QueryMapping
    public List<Alarm> GetAllAlamrs() {
        var time = OffsetDateTime.now();
        var val = time.plusMinutes(5);
        var alarm = Alarm.builder().id(UUID.randomUUID()).action("Teste action all").time(val).build();
        return List.of(alarm);
    }
}

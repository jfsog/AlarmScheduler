package com.jfsog.AlarmScheduler.Services;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
public class Alarm {
    private UUID id;
    private String action;
    private OffsetDateTime time;
}

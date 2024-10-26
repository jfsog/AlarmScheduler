package com.jfsog.AlarmScheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlarmSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlarmSchedulerApplication.class, args);
    }
}

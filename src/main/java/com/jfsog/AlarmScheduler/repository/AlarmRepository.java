package com.jfsog.AlarmScheduler.repository;

import com.jfsog.AlarmScheduler.Data.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, UUID> {
}

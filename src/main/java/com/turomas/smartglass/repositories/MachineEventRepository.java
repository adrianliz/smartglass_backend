package com.turomas.smartglass.repositories;

import com.turomas.smartglass.domain.MachineEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MachineEventRepository extends MongoRepository<MachineEvent, String> {
  @Query(value = "{}", sort = "{timestamp: 1}")
  List<MachineEvent> findAll();

  @Query(value = "{machine: ?0, timestamp: {$gte: ?1, $lt: ?2}}", sort = "{timestamp: 1}")
  List<MachineEvent> searchBy(String machineName, LocalDateTime startDate, LocalDateTime endDate);
}

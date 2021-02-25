package com.turomas.smartglass.machineEvent.repositories;

import com.turomas.smartglass.machineEvent.domain.MachineEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.SortedSet;

public interface MachineEventRepository extends MongoRepository<MachineEvent, String> {
  @Query(value = "{}", sort = "{timestamp: 1}")
  SortedSet<MachineEvent> getMachineEvents();

  @Query(value = "{machine: ?0, timestamp: {$gte: ?1, $lt: ?2}}", sort = "{timestamp: 1}")
  SortedSet<MachineEvent> searchBy(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);
}

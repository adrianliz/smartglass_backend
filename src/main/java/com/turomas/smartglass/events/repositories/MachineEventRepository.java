package com.turomas.smartglass.events.repositories;

import com.turomas.smartglass.events.domain.MachineEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.SortedSet;

public interface MachineEventRepository extends MongoRepository<MachineEvent, String> {
  @Query(value = "{machine: ?0}", sort = "{timestamp: 1}")
  SortedSet<MachineEvent> getMachineEvents(String machineName);

  @Query(value = "{machine: ?0, timestamp: {$gt: ?1}}", sort = "{timestamp: 1}")
  SortedSet<MachineEvent> getNextMachineEvents(String machineName, LocalDateTime startDate);
}

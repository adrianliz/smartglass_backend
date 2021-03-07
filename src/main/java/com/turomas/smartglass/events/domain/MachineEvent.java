package com.turomas.smartglass.events.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "machineEvent")
@Getter
@AllArgsConstructor
public class MachineEvent implements Comparable<MachineEvent> {
  @Id @EqualsAndHashCode.Include private final String id;

  @Field("type")
  private final EventType type;

  @Field("machine")
  private final String machineName;

  @Field("params")
  private final EventParams params;

  @Field("error_name")
  private final String errorName;

  private final LocalDateTime timestamp;

  public boolean machineIsInBreakdown() {
    return type.equals(EventType.ERROR);
  }

  public boolean machineIsRearmed() {
    return type.equals(EventType.OK);
  }

  public boolean machineStartsProcess() {
    return (type.equals(EventType.START_PROCESS));
  }

  public boolean machineCompletesProcess(MachineEvent machineEvent) {
    return ((machineStartsProcess()) || (machineEvent != null
        && machineEvent.machineStartsProcess()
        && type.equals(EventType.END_PROCESS)
        && params != null
        && machineEvent.params != null
        && params.equals(machineEvent.params)));
  }

  @Override
  public int compareTo(MachineEvent machineEvent) {
    return timestamp.compareTo(machineEvent.timestamp);
  }
}

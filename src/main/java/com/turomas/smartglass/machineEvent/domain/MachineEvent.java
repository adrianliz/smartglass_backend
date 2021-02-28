package com.turomas.smartglass.machineEvent.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "machineEvent")
@Getter
@AllArgsConstructor
public class MachineEvent implements Comparable<MachineEvent> {
  @Id private final String id;

  @Field("class")
  private final EventClassification classification;

  @Field("type")
  private final EventType type;

  @Field("machine")
  private final String machineName;

  @Field("params")
  private final EventParams params;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private final LocalDateTime timestamp;

  @Override
  public int compareTo(MachineEvent machineEvent) {
    return this.timestamp.compareTo(machineEvent.timestamp);
  }

  public boolean machineIsInBreakdown() {
    return (type.equals(EventType.ERROR)
        || type.equals(EventType.RESETTING)
        || type.equals(EventType.POWER_OFF));
  }

  public boolean machineStartsProcess() {
    return (type.equals(EventType.START_PROCESS));
  }

  public boolean machineCompletesProcess(MachineEvent machineEvent) {
    return (type.equals(EventType.END_PROCESS)
        && params != null
        && machineEvent.params != null
        && params.equals(machineEvent.params));
  }

  public boolean machineIsAvailable() {
    return (type.equals(EventType.END_PROCESS)
        || type.equals(EventType.OK)
        || type.equals(EventType.POWER_ON));
  }
}

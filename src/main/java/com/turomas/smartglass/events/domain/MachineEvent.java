package com.turomas.smartglass.events.domain;

import com.turomas.smartglass.twins.domain.DateRange;
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

  @Field("class")
  private final EventClassification classification;

  @Field("type")
  private final EventType type;

  @Field("machine")
  private final String machineName;

  @Field("params")
  private final EventParams params;

  private final LocalDateTime timestamp;

  public boolean happenedBetween(DateRange dateRange) {
    return ((timestamp.compareTo(dateRange.getStartDate()) > 0)
        && (timestamp.compareTo(dateRange.getEndDate()) < 0));
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
    return (machineEvent != null
        && machineEvent.machineStartsProcess()
        && type.equals(EventType.END_PROCESS)
        && params != null
        && machineEvent.params != null
        && params.equals(machineEvent.params));
  }

  @Override
  public int compareTo(MachineEvent machineEvent) {
    return timestamp.compareTo(machineEvent.timestamp);
  }
}

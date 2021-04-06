package com.turomas.smartglass.events.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "events")
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event implements Comparable<Event> {
  @Id
  @EqualsAndHashCode.Include
  private String id;

  @Field("type")
  private final EventType type;

  @Field("machine")
  private final String machineName;

  @Field("params")
  private EventParams params;

  @Field("error_name")
  private final String errorName;

  private LocalDateTime timestamp;

  public boolean typeIs(EventType type) {
    if (type != null) {
      return this.type.equals(type);
    }

    return false;
  }

  public boolean machineStartsProcess() {
    return typeIs(EventType.START_PROCESS);
  }

  public boolean isFinalizedBy(Event event) {
    return (machineStartsProcess()
            && (event.type.equals(EventType.END_PROCESS))
            && (params != null)
            && (event.params != null)
            && (params.equals(event.params)));
  }

  public void update(Event event) {
    if (event != null) {
      id = event.id;
      params = event.params;
      timestamp = event.timestamp;
    }
  }

  public boolean finalizesProcess(ProcessName processName) {
    if (typeIs(EventType.END_PROCESS)) {
      return params.getProcessName().equals(processName);
    }

    return false;
  }

  @Override
  public int compareTo(Event event) {
    return timestamp.compareTo(event.timestamp);
  }
}

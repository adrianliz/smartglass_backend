package com.turomas.smartglass.machineEvent.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "machineEvent")
@Data
@AllArgsConstructor
public class MachineEvent implements Comparable<MachineEvent> {
  @Id private String eventId;

  @Field("class")
  private EventClass eventClass;

  @Field("type")
  private EventType eventType;

  @Field("machine")
  private String machineName;

  @Field("params")
  private EventParams eventParams;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;

  @Override
  public int compareTo(MachineEvent machineEvent) {
    return this.timestamp.compareTo(machineEvent.timestamp);
  }
}

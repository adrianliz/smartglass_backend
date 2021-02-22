package com.turomas.smartglass.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document(collection = "machineEvent")
@Data
@AllArgsConstructor
public class MachineEvent {
  @Id private ObjectId eventId;

  @Field("class")
  private EventClass eventClass;

  @Field("type")
  private EventType eventType;

  @Field("machine")
  private String machineName;

  @Field("params")
  private EventParams eventParams;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime timestamp;
}

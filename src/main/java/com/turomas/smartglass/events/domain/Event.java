package com.turomas.smartglass.events.domain;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "events")
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event implements Comparable<Event> {
  @Id
  @EqualsAndHashCode.Include
  @NonNull
  private final String id;

  @Field("type")
  @NonNull
  @Getter
  private final EventType type;

  @Field("params")
  @Getter
  private final EventParams params;

  @Field("error_name")
  @Getter
  private final String errorName;

  @Field("timestamp")
  @NonNull
  @Getter
  private final LocalDateTime timestamp;

  public boolean typeIs(EventType type) {
    return this.type.equals(type);
  }

  public boolean hasSameParams(Event event) {
    return ((params != null) && params.equals(event.params));
  }

  public boolean processIs(ProcessName processName) {
    return ((params != null)
            && params.processIs(processName));
  }

  @Override
  public int compareTo(Event event) {
    return timestamp.compareTo(event.timestamp);
  }
}

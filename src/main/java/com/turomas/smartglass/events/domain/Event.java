package com.turomas.smartglass.events.domain;

import com.mongodb.lang.NonNull;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.DateRange;
import com.turomas.smartglass.twins.domain.dtos.statistics.ToolsDTO;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
  private final EventType type;

  @Field("params")
  private final EventParams params;

  @Field("error_name")
  @Getter
  private final String errorName;

  @Field("timestamp")
  @NonNull
  private final LocalDateTime timestamp;

  public boolean typeIs(EventType type) {
    return this.type.equals(type);
  }

  public boolean paramsMatchWith(Event event) {
    return ((params != null) && params.equals(event.params));
  }

  public boolean processIs(ProcessName processName) {
    return ((params != null)
            && params.processIs(processName));
  }

  public Optional<CutResultDTO> getCutResult() {
    if (typeIs(EventType.END_PROCESS) && (params != null)) {
      return params.getCutResult();
    }

    return Optional.empty();
  }

  public Optional<ToolsDTO> getToolsInfoAfter(ProcessName processName) {
    if (typeIs(EventType.END_PROCESS) && (params != null)) {
      return params.getToolsInfoIf(processName);
    }

    return Optional.empty();
  }

  public Optional<DateRange> dateRangeUntil(Event event) {
    if ((event != null) && (timestamp.isBefore(event.timestamp))) {
      return Optional.of(new DateRange(timestamp, event.timestamp));
    }

    return Optional.empty();
  }

  public Collection<Event> getSubsequentEvents(String twinName, EventsService eventsService) {
    if (eventsService != null) {
      return eventsService.getSubsequentEvents(twinName, timestamp);
    }

    return Collections.emptyList();
  }

  public Optional<Pair<TwinStateType, EventType>> createTransitionTriggerFor(TwinStateType stateId) {
    if (stateId != null) {
      return Optional.of(Pair.of(stateId, type));
    }

    return Optional.empty();
  }

  @Override
  public int compareTo(Event event) {
    return timestamp.compareTo(event.timestamp);
  }
}

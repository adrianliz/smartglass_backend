package com.turomas.smartglass.twins.domain.statesmachine;

import com.mongodb.lang.NonNull;
import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.DateRange;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Document(collection = "states")
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TwinState implements Comparable<TwinState> {
  @Id
  @EqualsAndHashCode.Include
  @NonNull
  private final String id;

  @Field("twinStateId")
  @NonNull
  @Getter
  private final TwinStateType type;

  @Field("twinName")
  @NonNull
  @Getter
  private final String twinName;

  @Field("enterEvent")
  private final Event enterEvent;

  @Field("lastEventEvaluated")
  private Event lastEventEvaluated;

  public static TwinState of(TwinStateType twinStateType, String twinName) {
    return new TwinState(new ObjectId().toString(), twinStateType, twinName, null, null);
  }

  public static TwinState of(TwinStateType twinStateType, String twinName, Event enterEvent) {
    return new TwinState(new ObjectId().toString(), twinStateType, twinName, enterEvent, enterEvent);
  }

  public Collection<Event> getSubsequentEvents(EventsService eventsService) {
    if (eventsService != null) {
      if (lastEventEvaluated != null) {
        return lastEventEvaluated.getSubsequentEvents(twinName, eventsService);
      }

      return eventsService.getEvents(twinName);
    }

    return Collections.emptyList();
  }

  public boolean eventsParamsMatch() {
    return ((enterEvent != null) &&
            enterEvent.paramsMatchWith(lastEventEvaluated));
  }

  public Optional<Pair<TwinStateType, EventType>> createTransitionTriggerFor(Event event) {
    if (event != null) {
      lastEventEvaluated = event;
      return lastEventEvaluated.createTransitionTriggerFor(type);
    }

    return Optional.empty();
  }

  public long overlapSecondsWith(DateRange dateRange) {
    if ((dateRange != null) && (enterEvent != null)) {
      Optional<DateRange> stateInterval = enterEvent.dateRangeUntil(lastEventEvaluated);

      if (stateInterval.isPresent()) {
        return dateRange.overlapSecondsWith(stateInterval.get());
      }
    }

    return 0;
  }

  public boolean typeIs(TwinStateType twinStateType) {
    return this.type.equals(twinStateType);
  }

  public boolean lastEventTypeIs(EventType eventType) {
    return ((lastEventEvaluated != null)
            && (lastEventEvaluated.typeIs(eventType)));
  }

  public boolean starts(ProcessName processName) {
    return (typeIs(TwinStateType.DOING_PROCESS)
            && (enterEvent != null)
            && (enterEvent.processIs(processName)));
  }

  @Override
  public int compareTo(TwinState twinState) {
    return id.compareTo(twinState.id);
  }
}

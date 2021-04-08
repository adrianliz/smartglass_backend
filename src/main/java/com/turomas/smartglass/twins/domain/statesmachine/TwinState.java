package com.turomas.smartglass.twins.domain.statesmachine;

import com.mongodb.lang.NonNull;
import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.events.services.EventsService;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Duration;
import java.util.Collection;

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
  private final TwinStateId twinStateId;

  @Field("twinName")
  @NonNull
  @Getter
  private final String twinName;

  @Field("enterEvent")
  private final Event enterEvent;

  @Field("lastEventEvaluated")
  private Event lastEventEvaluated;

  public static TwinState of(TwinStateId twinStateId, String twinName) {
    return new TwinState(new ObjectId().toString(), twinStateId, twinName, null, null);
  }

  public static TwinState of(TwinStateId twinStateId, String twinName, Event enterEvent) {
    return new TwinState(new ObjectId().toString(), twinStateId, twinName, enterEvent, enterEvent);
  }

  public Collection<Event> getSubsequentEvents(EventsService eventsService) {
    if (lastEventEvaluated != null) {
      return eventsService.getSubsequentEvents(twinName, lastEventEvaluated.getTimestamp());
    }

    return eventsService.getEvents(twinName);
  }

  public boolean eventsHaveSameParams() {
    return ((enterEvent != null) &&
            enterEvent.hasSameParams(lastEventEvaluated));
  }

  public void updateLastEventEvaluated(Event event) {
    if (event != null) {
      this.lastEventEvaluated = event;
    }
  }

  public long durationSeconds() {
    if ((enterEvent != null) && (lastEventEvaluated != null)) {
      return (Duration.between(enterEvent.getTimestamp(), lastEventEvaluated.getTimestamp()).getSeconds());
    }
    return 0;
  }

  public boolean stateIdIs(TwinStateId twinStateId) {
    return this.twinStateId.equals(twinStateId);
  }

  public boolean lastEventTypeIs(EventType eventType) {
    return ((lastEventEvaluated != null)
            && (lastEventEvaluated.typeIs(eventType)));
  }

  public boolean stateIsDoing(ProcessName processName) {
    return (stateIdIs(TwinStateId.DOING_PROCESS)
            && (enterEvent != null)
            && (enterEvent.processIs(processName)));
  }

  @Override
  public int compareTo(TwinState twinState) {
    return id.compareTo(twinState.id);
  }
}

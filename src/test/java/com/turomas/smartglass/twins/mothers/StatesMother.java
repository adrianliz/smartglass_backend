package com.turomas.smartglass.twins.mothers;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class StatesMother {

  public static TwinState initial(TwinStateType type) {
    return TwinState.firstState(type, "Turomas1");
  }

  public static TwinState doingProcess(ProcessName processName) {
    return doingProcess(processName, LocalDateTime.now(), 0);
  }

  public static TwinState doingProcess(ProcessName processName, LocalDateTime startDate, long secondsInState) {
    Event enterEvent = EventsMother.startsProcess(processName, startDate);
    Event lastEventEvaluated = EventsMother.startsProcess(processName, startDate.plusSeconds(secondsInState));

    return of(TwinStateType.DOING_PROCESS, enterEvent, lastEventEvaluated);
  }

  public static TwinState of(TwinStateType type, LocalDateTime startDate, long secondsInState) {
    return of(type, EventType.UNDEFINED, startDate, secondsInState);
  }

  public static TwinState of(TwinStateType type, EventType eventType) {
    return of(type, eventType, LocalDateTime.now(), 0);
  }

  public static TwinState of(TwinStateType type, EventType eventType, LocalDateTime startDate, long secondsInState) {
    Event enterEvent = EventsMother.of(eventType, startDate);
    Event lastEventEvaluated = EventsMother.of(eventType, startDate.plusSeconds(secondsInState));

    return of(type, enterEvent, lastEventEvaluated);
  }

  public static TwinState of(TwinStateType type, Event enterEvent, Event lastEventEvaluated) {
    return new TwinState(new ObjectId().toString(), type, "Turomas1", enterEvent,
                         lastEventEvaluated);
  }
}

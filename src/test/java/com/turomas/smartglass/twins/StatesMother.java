package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class StatesMother {
  private static final LocalDateTime START_DATE = LocalDateTime.of(1999, 7, 5, 0, 0);

  public static TwinState initial(TwinStateType type) {
    return TwinState.of(type, "Turomas1");
  }

  public static TwinState doingProcess(ProcessName processName) {
    return doingProcess(processName, 0);
  }

  public static TwinState doingProcess(ProcessName processName, long secondsInState) {
    Event enterEvent = EventsMother.of(processName, START_DATE);
    Event lastEventEvaluated = EventsMother.of(processName, START_DATE.plusSeconds(secondsInState));

    return of(TwinStateType.DOING_PROCESS, enterEvent, lastEventEvaluated);
  }

  public static TwinState of(TwinStateType type, long secondsInState) {
    return of(type, EventType.UNDEFINED, secondsInState);
  }

  public static TwinState of(TwinStateType type, EventType eventType) {
    return of(type, eventType, 0);
  }

  public static TwinState of(TwinStateType type, EventType eventType, long secondsInState) {
    Event enterEvent = EventsMother.of(eventType, START_DATE);
    Event lastEventEvaluated = EventsMother.of(eventType, START_DATE.plusSeconds(secondsInState));

    return of(type, enterEvent, lastEventEvaluated);
  }

  public static TwinState of(TwinStateType type, Event enterEvent, Event lastEventEvaluated) {
    return new TwinState(new ObjectId().toString(), type, "Turomas1", enterEvent,
                         lastEventEvaluated);
  }
}

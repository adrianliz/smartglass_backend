package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventParams;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class EventsMother {
  public static Event of(EventType type) {
    return of(type, LocalDateTime.now());
  }

  public static Event of(EventType type, LocalDateTime localDateTime) {
    return new Event(new ObjectId().toString(), type, null, null, localDateTime);
  }

  public static Event of(EventType type, ProcessName processName) {
    return of(type, processName, LocalDateTime.now());
  }

  public static Event of(ProcessName processName, LocalDateTime localDateTime) {
    return of(EventType.START_PROCESS, processName, localDateTime);
  }

  public static Event of(EventType type, ProcessName processName, LocalDateTime localDateTime) {
    return new Event(new ObjectId().toString(), type, new EventParams(processName, "", 0, "", 0, 0, 0), null,
                     localDateTime);
  }
}

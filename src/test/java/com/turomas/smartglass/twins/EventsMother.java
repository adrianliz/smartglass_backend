package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventParams;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class EventsMother {
  private static EventParams params(ProcessName processName, String optimization, String material, long distanceCovered,
                                    int toolAngle, int wheelDiameter) {

    return new EventParams(processName, optimization, 0, material, distanceCovered, toolAngle, wheelDiameter);
  }

  public static Event initial(EventType type) {
    return of(type, LocalDateTime.now());
  }

  public static Event of(EventType type, LocalDateTime localDateTime) {
    return of(type, null, null, localDateTime);
  }

  public static Event of(EventType type, ProcessName processName) {
    return of(type, params(processName, "", "", 0, 0, 0), null, LocalDateTime.now());
  }

  public static Event startsProcess(ProcessName processName, LocalDateTime localDateTime) {
    return of(EventType.START_PROCESS, params(processName, "", "", 0, 0, 0), null, localDateTime);
  }

  public static Event endsCut(String optimization, String material) {
    return of(EventType.END_PROCESS, params(ProcessName.CUT, optimization, material, 0, 0, 0), null,
              LocalDateTime.now());
  }

  public static Event endsCut(long distanceCovered, int toolAngle) {
    return of(EventType.END_PROCESS, params(ProcessName.CUT, "", "", distanceCovered, toolAngle, 0),
              null, LocalDateTime.now());
  }

  public static Event endsLowe(int wheelDiameter) {
    return of(EventType.END_PROCESS, params(ProcessName.LOWE, "", "", 0, 0, wheelDiameter), null,
              LocalDateTime.now());
  }

  public static Event error(String errorName) {
    return of(EventType.ERROR, null, errorName, LocalDateTime.now());
  }

  public static Event of(EventType type, EventParams params, String errorName, LocalDateTime localDateTime) {
    return new Event(new ObjectId().toString(), type, params, errorName, localDateTime);
  }
}

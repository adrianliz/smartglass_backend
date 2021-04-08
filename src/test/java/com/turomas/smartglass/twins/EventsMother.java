package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventParams;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;

import java.time.LocalDateTime;

public class EventsMother {
  public static Event create(EventType type) {
    return new Event("0", type, null, null, LocalDateTime.now());
  }

  public static Event create(EventType type, ProcessName processName) {
    return new Event("0", type, new EventParams(processName, "", 0, "", 0, 0, 0), null,
                     LocalDateTime.now());
  }
}

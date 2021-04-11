package com.turomas.smartglass.events.domain;

import com.google.gson.JsonElement;

public enum EventType {
  OK,
  ERROR,
  POWER_ON,
  POWER_OFF,
  RESETTING,
  START_PROCESS,
  END_PROCESS,
  UNDEFINED;

  public static EventType of(JsonElement jsonElement) {
    for (EventType eventType : EventType.values()) {
      if (eventType.name().equals(jsonElement.getAsString().toUpperCase())) {
        return eventType;
      }
    }

    return UNDEFINED;
  }
}

package com.turomas.smartglass.twins.domain.statesmachine;

import com.google.gson.JsonElement;

public enum TwinStateType {
  OFF,
  DOING_PROCESS,
  IN_BREAKDOWN,
  IN_STANDBY,
  UNDEFINED;

  public static TwinStateType of(JsonElement jsonElement) {
    return of(jsonElement.getAsString());
  }

  public static TwinStateType of(String name) {
    for (TwinStateType twinStateType : TwinStateType.values()) {
      if (twinStateType.name().equals(name.toUpperCase())) {
        return twinStateType;
      }
    }

    return UNDEFINED;
  }
}

package com.turomas.smartglass.twins.domain.statesmachine;

import com.google.gson.JsonElement;

public enum TwinStateType {
  OFF,
  DOING_PROCESS,
  IN_BREAKDOWN,
  IN_STANDBY,
  UNDEFINED;

  public static TwinStateType of(JsonElement jsonElement) {
    for (TwinStateType twinStateType : TwinStateType.values()) {
      if (twinStateType.name().equals(jsonElement.getAsString().toUpperCase())) {
        return twinStateType;
      }
    }

    return UNDEFINED;
  }
}

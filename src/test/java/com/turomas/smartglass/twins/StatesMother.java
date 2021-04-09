package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;

public class StatesMother {
  public static TwinState create(TwinStateType id) {
    return TwinState.of(id, "Turomas1");
  }

  public static TwinState create(TwinStateType id, Event enterEvent) {
    return TwinState.of(id, "Turomas1", enterEvent);
  }
}

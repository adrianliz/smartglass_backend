package com.turomas.smartglass.twins.domain.factories;

import com.google.gson.*;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import org.springframework.data.util.Pair;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TransitionsDeserializer implements JsonDeserializer<Map<Pair<TwinStateType, EventType>, TwinStateType>> {
  private static final String TRIGGER = "trigger";
  private static final String STATE_TYPE = "stateType";
  private static final String EVENT_TYPE = "eventType";
  private static final String NEXT_STATE_TYPE = "nextStateType";

  @Override
  public Map<Pair<TwinStateType, EventType>, TwinStateType>
  deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
    throws JsonParseException {

    Map<Pair<TwinStateType, EventType>, TwinStateType> transitions = new HashMap<>();
    JsonArray jsonTransitions = jsonElement.getAsJsonArray();

    for (JsonElement jsonTransition : jsonTransitions) {
      JsonObject transition = jsonTransition.getAsJsonObject();
      JsonObject trigger = transition.get(TRIGGER).getAsJsonObject();

      TwinStateType stateType = TwinStateType.of(trigger.get(STATE_TYPE));
      EventType eventType = EventType.of(trigger.get(EVENT_TYPE));
      TwinStateType nextStateType = TwinStateType.of(transition.get(NEXT_STATE_TYPE));

      transitions.put(Pair.of(stateType, eventType), nextStateType);
    }

    return transitions;
  }
}

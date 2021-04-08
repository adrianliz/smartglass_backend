package com.turomas.smartglass.twins.domain.factories;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TransitionTrigger;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;
import com.turomas.smartglass.twins.services.StatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

@Component
public class StatesMachineJSONFactory implements StatesMachineFactory {
  private final StatesService statesService;
  private final Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> statesTransitions;

  public StatesMachineJSONFactory(StatesService statesService, @Value("classpath:transitions.json") Resource resource)
    throws IOException, JsonIOException, JsonSyntaxException {

    this.statesService = statesService;
    statesTransitions = loadTransitions(new FileReader(resource.getFile()));
  }

  private Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> loadTransitions(Reader file)
    throws JsonIOException, JsonSyntaxException {

    Type type =
      new TypeToken<Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId>>() {
      }.getType();

    return new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(file, type);
  }

  public StatesMachine createFor(String twinName) {
    Optional<TwinState> initialState = statesService.getLastState(twinName);
    return new StatesMachine(initialState.orElse(new TwinState(TwinStateId.OFF, twinName)), statesTransitions);
  }
}

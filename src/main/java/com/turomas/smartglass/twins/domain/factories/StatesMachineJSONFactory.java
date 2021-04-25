package com.turomas.smartglass.twins.domain.factories;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import com.turomas.smartglass.twins.services.StatesService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

@Component
public class StatesMachineJSONFactory implements StatesMachineFactory {
  @NonNull
  private final StatesService statesService;
  @NonNull
  private final Map<Pair<TwinStateType, EventType>, TwinStateType> statesMachineTransitions;

  public StatesMachineJSONFactory(StatesService statesService, @Value("${transitions.file}") Resource resource)
    throws IOException, JsonIOException, JsonSyntaxException {

    this.statesService = statesService;
    statesMachineTransitions = loadTransitions(new InputStreamReader(resource.getInputStream()));
  }

  private Map<Pair<TwinStateType, EventType>, TwinStateType> loadTransitions(Reader file)
    throws JsonIOException, JsonSyntaxException {

    Type type =
      new TypeToken<Map<Pair<TwinStateType, EventType>, TwinStateType>>() {
      }.getType();

    return new GsonBuilder().registerTypeAdapter(type, new TransitionsDeserializer()).create().fromJson(file, type);
  }

  public StatesMachine createFor(String twinName) {
    Optional<TwinState> initialState = statesService.getLastState(twinName);
    return new StatesMachine(initialState.orElse(TwinState.firstState(TwinStateType.OFF, twinName)),
                             statesMachineTransitions);
  }
}
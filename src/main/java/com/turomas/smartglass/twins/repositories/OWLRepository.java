package com.turomas.smartglass.twins.repositories;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.dtos.twins.TwinModelDTO;
import com.turomas.smartglass.twins.domain.statesmachine.TransitionTrigger;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import com.turomas.smartglass.twins.services.StatesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

// TODO replace with ontology model
@Repository
public class OWLRepository implements TwinsRepository {
  private final Map<String, Twin> twins;

  public OWLRepository(StatesService statesService, EventsService eventsService,
                       @Value("classpath:transitions.json") Resource resourceFile)
    throws IOException, JsonIOException, JsonSyntaxException {

    Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions =
      loadTransitions(new FileReader(resourceFile.getFile()));

    twins = new HashMap<>();
    twins.put("Turomas1", new Twin("Turomas1", statesService, eventsService, transitions));
  }

  private Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> loadTransitions(Reader file)
    throws JsonIOException, JsonSyntaxException {

    Type type =
      new TypeToken<Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId>>() {
      }.getType();

    return new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(file, type);
  }

  @Override
  public Collection<Twin> getTwins() {
    return twins.values();
  }

  @Override
  public Twin getTwin(String twinName) throws TwinNotFound {
    return Optional.ofNullable(twins.get(twinName)).orElseThrow(() -> new TwinNotFound(twinName));
  }

  @Override
  public TwinModelDTO getTwinModel(String twinName) throws TwinNotFound {
    Twin twin = getTwin(twinName);
    return new TwinModelDTO(twinName, "RUBI 300 SERIES", "RUBI 303BA", twin.getCurrentState());
  }

  @Override
  public Collection<TwinModelDTO> getTwinModels() {
    List<TwinModelDTO> twinsModels = new ArrayList<>();

    for (String twinName : twins.keySet()) {
      twinsModels.add(getTwinModel(twinName));
    }

    return twinsModels;
  }
}

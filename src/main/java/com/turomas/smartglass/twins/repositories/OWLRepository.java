package com.turomas.smartglass.twins.repositories;

import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.EventsStatistics;
import com.turomas.smartglass.twins.domain.StatesStatistics;
import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.dtos.twins.TwinModelDTO;
import com.turomas.smartglass.twins.domain.factories.StatesMachineFactory;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import com.turomas.smartglass.twins.services.StatesService;
import org.springframework.stereotype.Repository;

import java.util.*;

// TODO replace with ontology model
@Repository
public class OWLRepository implements TwinsRepository {
  private final Map<String, Twin> twins;

  public OWLRepository(StatesService statesService, EventsService eventsService,
                       StatesMachineFactory statesMachineFactory) {

    // TODO replace with query that retrieves twins from OWL graph database
    twins = new HashMap<>();
    String twinName = "Turomas1";
    twins.put(twinName, new Twin(statesMachineFactory.createFor(twinName),
                                 new StatesStatistics(twinName, statesService),
                                 new EventsStatistics(twinName, eventsService)));
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

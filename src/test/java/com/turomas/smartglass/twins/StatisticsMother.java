package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.EventsStatistics;
import com.turomas.smartglass.twins.domain.StatesStatistics;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.services.StatesService;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;

public class StatisticsMother {
  public static StatesStatistics of(StatesService statesService, Collection<TwinState> mockTransitedStates) {
    Mockito.when(statesService.getStatesBetween(any(String.class), any(LocalDateTime.class), any(LocalDateTime.class)))
           .thenReturn(mockTransitedStates);

    return new StatesStatistics("Turomas1", statesService);
  }

  public static EventsStatistics of(EventsService eventsService, Collection<Event> mockEvents) {
    Mockito.when(eventsService.getEventsBetween(any(String.class), any(LocalDateTime.class), any(LocalDateTime.class)))
           .thenReturn(mockEvents);

    return new EventsStatistics("Turomas1", eventsService);
  }
}

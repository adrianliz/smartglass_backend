package com.turomas.smartglass.twins;

import com.turomas.smartglass.twins.domain.StatesStatistics;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.services.StatesService;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;

public class StatesStatisticsMother {
  public static StatesStatistics create(StatesService statesService, Collection<TwinState> mockTransitedStates) {
    Mockito.when(statesService.getStatesBetween(any(String.class), any(LocalDateTime.class), any(LocalDateTime.class)))
           .thenReturn(mockTransitedStates);

    return new StatesStatistics("Turomas1", statesService);
  }
}

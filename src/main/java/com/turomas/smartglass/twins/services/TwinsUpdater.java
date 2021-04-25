package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import lombok.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.SortedSet;

@Service
public class TwinsUpdater {
  @NonNull
  private final TwinsService twinsService;
  @NonNull
  private final EventsService eventsService;
  @NonNull
  private final StatesService statesService;

  public TwinsUpdater(TwinsService twinsService, EventsService eventsService, StatesService statesService) {
    this.twinsService = twinsService;
    this.eventsService = eventsService;
    this.statesService = statesService;

    updateTwins();
  }

  @Scheduled(fixedDelayString = "${twins.updateDelay}")
  void updateTwins() {
    for (Twin twin : twinsService.getTwins()) {
      SortedSet<TwinState> transitedStates = twin.processEvents(eventsService);

      if (! transitedStates.isEmpty()) {
        String twinName = transitedStates.last().getTwinName();

        statesService.saveStates(transitedStates);
        twinsService.updateRatios(twinName);
        twinsService.updateLastState(twinName, statesService);
      }
    }
  }
}

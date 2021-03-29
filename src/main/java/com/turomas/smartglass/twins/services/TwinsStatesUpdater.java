package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.Twin;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TwinsStatesUpdater {
	private final TwinsService twinsService;
	private final EventsService eventsService;
	private final StatesService statesService;

	public TwinsStatesUpdater(TwinsService twinsService, EventsService eventsService, StatesService statesService) {
		this.twinsService = twinsService;
		this.eventsService = eventsService;
		this.statesService = statesService;

		updateTwinsStates();
	}

	@Scheduled(fixedDelayString = "${states.updateDelay}")
	private void updateTwinsStates() {
		for (Twin twin : twinsService.getTwins()) {
			twin.processEvents(eventsService, statesService);
		}
	}
}
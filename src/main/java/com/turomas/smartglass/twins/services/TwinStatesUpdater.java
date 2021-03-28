package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.Twin;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TwinStatesUpdater {
	private final TwinsService twinsService;
	private final EventsService eventsService;

	public TwinStatesUpdater(TwinsService twinsService, EventsService eventsService) {
		this.twinsService = twinsService;
		this.eventsService = eventsService;

		updateTwinsStates();
	}

	@Scheduled(fixedDelay = 5000)
	private void updateTwinsStates() {
		for (Twin twin : twinsService.getTwins()) {
			twin.processEvents(eventsService);
		}
	}
}

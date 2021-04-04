package com.turomas.smartglass.events.services;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.repositories.EventsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class EventsProxy implements EventsService {
	private final EventsRepository eventsRepository;

	public EventsProxy(EventsRepository eventsRepository) {
		this.eventsRepository = eventsRepository;
	}

	@Override
	public Collection<Event> getEvents(String twinName) {
		return eventsRepository.getEvents(twinName);
	}

	@Override
	public Collection<Event> getSubsequentEvents(String twinName, LocalDateTime startDate) {
		return eventsRepository.getSubsequentEvents(twinName, startDate);
	}

	@Override
	public Collection<Event> getEventsBetween(String twinName, LocalDateTime startDate, LocalDateTime endDate) {
		return eventsRepository.getEventsBetween(twinName, startDate, endDate);
	}
}

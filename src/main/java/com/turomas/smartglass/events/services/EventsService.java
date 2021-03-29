package com.turomas.smartglass.events.services;

import com.turomas.smartglass.events.domain.Event;

import java.time.LocalDateTime;
import java.util.SortedSet;

public interface EventsService {
	SortedSet<Event> getEvents(String twinName);

	SortedSet<Event> getSubsequentEvents(String twinName, LocalDateTime startDate);

	SortedSet<Event> getEventsBetween(String twinName, LocalDateTime startDate, LocalDateTime endDate);
}

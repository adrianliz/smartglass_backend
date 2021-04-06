package com.turomas.smartglass.events.services;

import com.turomas.smartglass.events.domain.Event;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventsService {
  Collection<Event> getEvents(String twinName);

  Collection<Event> getSubsequentEvents(String twinName, LocalDateTime startDate);

  Collection<Event> getEventsBetween(String twinName, LocalDateTime startDate, LocalDateTime endDate);
}

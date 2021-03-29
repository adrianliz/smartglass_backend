package com.turomas.smartglass.events.repositories;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.ProcessName;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;

public interface EventsRepository extends MongoRepository<Event, String> {
	@Query(value = "{machine: ?0}", sort = "{timestamp: 1}")
	SortedSet<Event> getEvents(String twinName);

	@Query(value = "{machine: ?0, timestamp: {$gt: ?1}}", sort = "{timestamp: 1}")
	SortedSet<Event> getSubsequentEvents(String twinName, LocalDateTime startDate);

	@Query(value = "{machine: ?0, timestamp: {$gte: ?1, $lt: ?2}}", sort = "{timestamp: 1}")
	SortedSet<Event> getEventsBetween(String twinName, LocalDateTime startDate, LocalDateTime endDate);
}

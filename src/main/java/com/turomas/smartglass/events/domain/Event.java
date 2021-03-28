package com.turomas.smartglass.events.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "events")
@Getter
@AllArgsConstructor
public class Event implements Comparable<Event> {
	@Id
	@EqualsAndHashCode.Include
	private String id;

	@Field("type")
	private final EventType type;

	@Field("machine")
	private final String machineName;

	@Field("params")
	private EventParams params;

	@Field("error_name")
	private final String errorName;

	private LocalDateTime timestamp;

	private boolean machineStartsProcess() {
		return (type.equals(EventType.START_PROCESS));
	}

	private boolean validStartProcess(Event event) {
		return ((event != null)
			&& event.machineStartsProcess()
			&& (event.machineName.equals(machineName)));
	}

	public boolean machineEndsProcess(Event event) {
		return (validStartProcess(event)
			&& (type.equals(EventType.END_PROCESS))
			&& (params != null)
			&& (event.params != null)
			&& (params.equals(event.params)));
	}

	public void updateStartEvent(Event event) {
		if (validStartProcess(event) && machineStartsProcess()) {
			id = event.id;
			params = event.params;
			timestamp = event.timestamp;
		}
	}

	@Override
	public int compareTo(Event event) {
		return timestamp.compareTo(event.timestamp);
	}
}

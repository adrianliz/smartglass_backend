package com.turomas.smartglass.twins.domain.statesmachine;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Duration;

@Document(collection = "states")
@Getter
@AllArgsConstructor
@NoArgsConstructor // Necessary for Spring Data MongoDB Repository (TODO use Mongo Template instead of Repository)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TwinState implements Comparable<TwinState> {
	@Id
	@EqualsAndHashCode.Include
	private String id;

	@Field("twinStateId")
	private TwinStateId twinStateId;

	@Field("twinName")
	private String twinName;

	@Field("enterEvent")
	private Event enterEvent;

	@Field("lastEventEvaluated")
	private Event lastEventEvaluated;

	public TwinState(TwinStateId twinStateId, String twinName) {
		this(new ObjectId().toString(), twinStateId, twinName, null, null);
	}

	public TwinState(TwinStateId twinStateId, String twinName, Event enterEvent) {
		this(new ObjectId().toString(), twinStateId, twinName, enterEvent, enterEvent);
	}

	public void updateLastEventEvaluated(Event event) {
		this.lastEventEvaluated = event;
	}

	public long durationSeconds() {
		if ((enterEvent != null) && (lastEventEvaluated != null)) {
			return (Duration.between(enterEvent.getTimestamp(), lastEventEvaluated.getTimestamp()).getSeconds());
		}
		return 0;
	}

	public boolean stateIdIs(TwinStateId twinStateId) {
		if (twinStateId != null) {
			return twinStateId.equals(this.twinStateId);
		}
		return false;
	}

	public boolean lastEventTypeIs(EventType eventType) {
		if (eventType != null) {
			return lastEventEvaluated.typeIs(eventType);
		}
		return false;
	}

	public boolean stateIsDoing(ProcessName processName) {
		if (stateIdIs(TwinStateId.DOING_PROCESS) && (processName != null)) {
			return enterEvent.getParams().getProcessName().equals(processName);
		}
		return false;
	}

	@Override
	public int compareTo(TwinState twinState) {
		return id.compareTo(twinState.id);
	}
}

package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.exceptions.RequiredCondition;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TwinState {
	private final TwinStateId id;
	private final TwinState previousState;
	private final Event enterEvent;
	private Event lastEventEvaluated;

	public TwinState(TwinStateId id, Event enterEvent) {
		this.id = id;
		this.enterEvent = enterEvent;
		lastEventEvaluated = enterEvent;
		previousState = null;
	}

	private TwinState(TwinStateId id, Event enterEvent, TwinState previousState) {
		this.id = id;
		this.enterEvent = enterEvent;
		this.previousState = previousState;
		lastEventEvaluated = enterEvent;
	}

	public void update(TwinStateId newId, Event event, List<TwinState> transitedStates) {
		if ((newId != null) && (event != null) && (transitedStates != null)) {
			if (! newId.equals(id)) {
				transitedStates.add(new TwinState(newId, event, this));
			}

			lastEventEvaluated = event;
		}
	}

	public long secondsInState() {
		return (Duration.between(enterEvent.getTimestamp(), lastEventEvaluated.getTimestamp()).getSeconds());
	}

	public boolean machineEntersBetween(DateRange dateRange) {
		if (dateRange != null) {
			LocalDateTime enterDate = enterEvent.getTimestamp();
			return ((enterDate.isAfter(dateRange.getStartDate())) && (enterDate.isBefore(dateRange.getEndDate())));
		}

		return false;
	}

	public boolean doingProcess() { return id.equals(TwinStateId.DOING_PROCESS); }

	public boolean inBreakdown() { return id.equals(TwinStateId.IN_BREAKDOWN); }

	public boolean machineFiresError() { return enterEvent.machineFiresError(); }

	public boolean processWasFinished() {
		return ((previousState != null) && previousState.doingProcess() && (id.equals(TwinStateId.IN_STANDBY)));
	}

	public boolean processWasFinished(ProcessName processName) {
		if ((processName != null) && (previousState != null)) {
			return (processWasFinished() && (previousState.getProcessName().equals(processName)));
		}

		return false;
	}

	public boolean processWasAborted() {
		return ((previousState != null) && previousState.doingProcess() && (inBreakdown() || id.equals(TwinStateId.OFF)));
	}

	public LocalDateTime getLastUpdate() {
		return lastEventEvaluated.getTimestamp();
	}

	public TwinStateId getId() {
		return id;
	}

	public ProcessName getProcessName() throws RequiredCondition {
		if (! doingProcess()) throw new RequiredCondition(TwinStateId.DOING_PROCESS);
		return Objects.requireNonNullElse(
			enterEvent.getParams().getProcessName(), ProcessName.UNDEFINED);
	}

	public String getErrorName() throws RequiredCondition {
		if (! machineFiresError()) throw new RequiredCondition(EventType.ERROR);
		return enterEvent.getErrorName();
	}

	public String getProcessedMaterial() throws RequiredCondition {
		if (! processWasFinished(ProcessName.CUT)) throw new RequiredCondition(ProcessName.CUT);
		return Objects.requireNonNullElse(enterEvent.getParams().getMaterial(), "none");
	}

	public String getProcessedOptimization() throws RequiredCondition {
		if (! processWasFinished(ProcessName.CUT)) throw new RequiredCondition(ProcessName.CUT);
		return Objects.requireNonNullElse(enterEvent.getParams().getOptimizationName(), "none");
	}

	public long getToolDistanceCovered() throws RequiredCondition {
		if (! processWasFinished(ProcessName.CUT)) throw new RequiredCondition(ProcessName.CUT);
		return Objects.requireNonNullElse(enterEvent.getParams().getDistanceCovered(), 0L);
	}

	public int getToolAngle() throws RequiredCondition {
		if (! processWasFinished(ProcessName.CUT)) throw new RequiredCondition(ProcessName.CUT);
		return Objects.requireNonNullElse(enterEvent.getParams().getToolAngle(), 0);
	}

	public int getWheelDiameter() throws RequiredCondition {
		if (! processWasFinished(ProcessName.LOWE)) throw new RequiredCondition(ProcessName.LOWE);
		return Objects.requireNonNullElse(enterEvent.getParams().getWheelDiameter(), 0);
	}
}

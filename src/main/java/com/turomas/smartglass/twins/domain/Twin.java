package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.dtos.MachineUsageDTO;
import com.turomas.smartglass.twins.domain.dtos.RatioDTO;
import com.turomas.smartglass.twins.domain.dtos.TimeDistributionDTO;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TransitionTrigger;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;
import com.turomas.smartglass.twins.services.StatesService;

import java.util.*;

public class Twin {
	private final String twinName;
	private final StatesService statesService;
	private final Map<DateRange, TwinMetrics> metrics;
	private StatesMachine statesMachine;

	public Twin(String twinName, StatesService statesService,
	            Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions) {
		this.twinName = twinName;
		this.statesService = statesService;
		metrics = new HashMap<>();

		createStatesMachine(transitions);
	}

	private void createStatesMachine(Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions) {
		TwinState initialState = statesService.getLastState(twinName);
		if (initialState == null) {
			initialState = new TwinState(TwinStateId.OFF, twinName);
		}

		statesMachine = new StatesMachine(initialState, transitions);
	}

	private SortedSet<Event> getNewEvents(EventsService eventsService) {
		Event lastEventEvaluated = statesMachine.getLastEventEvaluated();
		if (lastEventEvaluated != null) {
			return eventsService.getSubsequentEvents(twinName, lastEventEvaluated.getTimestamp());
		}

		return eventsService.getEvents(twinName);
	}

	public void processEvents(EventsService eventsService) {
		SortedSet<Event> eventsToProcess = getNewEvents(eventsService);

		if (! eventsToProcess.isEmpty()) {
			List<TwinState> transitedStates = statesMachine.processEvents(eventsToProcess);

			if (! transitedStates.isEmpty()) {
				statesService.saveStates(transitedStates);
			}
		}
	}

	private TwinMetrics getMetrics(DateRange dateRange) {
		TwinMetrics metrics = this.metrics.get(dateRange);

		if (metrics == null) {
			metrics = new TwinMetrics(dateRange);
			this.metrics.put(dateRange, metrics);
		}

		return metrics;
	}

	public Collection<RatioDTO> getRatios(DateRange dateRange) {
		return getMetrics(dateRange).calculateRatios(twinName, statesService);
	}

	public MachineUsageDTO getMachineUsage(DateRange dateRange) {
		return getMetrics(dateRange).calculateMachineUsage(twinName, statesService);
	}

	public TimeDistributionDTO getTimeDistribution(DateRange dateRange) {
		return getMetrics(dateRange).calculateTimeDistribution(twinName, statesService);
	}
}

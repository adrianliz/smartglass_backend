package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.dtos.*;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TransitionTrigger;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;
import com.turomas.smartglass.twins.services.StatesService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class Twin {
	private final String twinName;
	private final StatesMetrics statesMetrics;
	private final EventsMetrics eventsMetrics;
	private StatesMachine statesMachine;

	public Twin(String twinName, StatesService statesService, EventsService eventsService,
	            Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions) {
		this.twinName = twinName;
		statesMetrics = new StatesMetrics(twinName, statesService);
		eventsMetrics = new EventsMetrics(twinName, eventsService);

		createStatesMachine(transitions, statesService);
	}

	private void createStatesMachine(Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions,
	                                 StatesService statesService) {
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

	public void processEvents(EventsService eventsService, StatesService statesService) {
		SortedSet<Event> eventsToProcess = getNewEvents(eventsService);

		if (! eventsToProcess.isEmpty()) {
			List<TwinState> transitedStates = statesMachine.processEvents(eventsToProcess);

			if (! transitedStates.isEmpty()) {
				statesService.saveStates(transitedStates);
			}
		}
	}

	public Collection<RatioDTO> getRatios(DateRange dateRange) {
		return statesMetrics.calculateRatios(dateRange);
	}

	public MachineUsageDTO getMachineUsage(DateRange dateRange) {
		return statesMetrics.calculateMachineUsage(dateRange);
	}

	public TimeDistributionDTO getTimeDistribution(DateRange dateRange) {
		return statesMetrics.calculateTimeDistribution(dateRange);
	}

	public Collection<MaterialDTO> getMaterialsUsed(DateRange dateRange) {
		return eventsMetrics.calculateMaterialsUsage(dateRange);
	}

	public Collection<OptimizationDTO> getOptimizationsProcessed(DateRange dateRange) {
		return eventsMetrics.calculateOptimizationsProcessed(dateRange);
	}

	public ToolsDTO getToolsInfo(DateRange dateRange) {
		return eventsMetrics.calculateToolsInfo(dateRange);
	}

	public Collection<ErrorDTO> getErrorsProduced(DateRange dateRange) {
		return eventsMetrics.calculateErrorsProduced(dateRange);
	}
}

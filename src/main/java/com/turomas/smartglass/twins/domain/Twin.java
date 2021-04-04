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
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

public class Twin {
	private final String twinName;
	private final StatesStatistics statesStatistics;
	private final EventsStatistics eventsStatistics;
	private StatesMachine statesMachine;

	public Twin(String twinName, StatesService statesService, EventsService eventsService,
	            Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions) {
		this.twinName = twinName;
		statesStatistics = new StatesStatistics(twinName, statesService);
		eventsStatistics = new EventsStatistics(twinName, eventsService);

		createStatesMachine(transitions, statesService);
	}

	private void createStatesMachine(Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions,
	                                 StatesService statesService) {
		Optional<TwinState> initialState = statesService.getLastState(twinName);
		statesMachine =
			new StatesMachine(initialState.orElse(new TwinState(TwinStateId.OFF, twinName)), transitions);
	}

	private Collection<Event> getNewEvents(EventsService eventsService) {
		Event lastEventEvaluated = statesMachine.getLastEventEvaluated();
		if (lastEventEvaluated != null) {
			return eventsService.getSubsequentEvents(twinName, lastEventEvaluated.getTimestamp());
		}

		return eventsService.getEvents(twinName);
	}

	public Collection<TwinState> processEvents(EventsService eventsService) {
		Collection<Event> eventsToProcess = getNewEvents(eventsService);
		Collection<TwinState> transitedStates = new TreeSet<>();

		if (! eventsToProcess.isEmpty()) {
			transitedStates = statesMachine.processEvents(eventsToProcess);
		}

		return transitedStates;
	}

	public Collection<RatioDTO> getRatios(DateRange dateRange) {
		return statesStatistics.calculateRatios(dateRange);
	}

	public MachineUsageDTO getMachineUsage(DateRange dateRange) {
		return statesStatistics.calculateMachineUsage(dateRange);
	}

	public TimeDistributionDTO getTimeDistribution(DateRange dateRange) {
		return statesStatistics.calculateTimeDistribution(dateRange);
	}

	public Collection<MaterialDTO> getMaterialsUsed(DateRange dateRange) {
		return eventsStatistics.calculateMaterialsUsage(dateRange);
	}

	public Collection<OptimizationDTO> getOptimizationsProcessed(DateRange dateRange) {
		return eventsStatistics.calculateOptimizationsProcessed(dateRange);
	}

	public ToolsDTO getToolsInfo(DateRange dateRange) {
		return eventsStatistics.calculateToolsInfo(dateRange);
	}

	public Collection<ErrorDTO> getErrorsProduced(DateRange dateRange) {
		return eventsStatistics.calculateErrorsProduced(dateRange);
	}
}

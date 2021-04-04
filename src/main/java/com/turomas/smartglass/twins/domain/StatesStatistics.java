package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.dtos.MachineUsageDTO;
import com.turomas.smartglass.twins.domain.dtos.RatioDTO;
import com.turomas.smartglass.twins.domain.dtos.TimeDistributionDTO;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;
import com.turomas.smartglass.twins.services.StatesService;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static com.turomas.smartglass.twins.domain.dtos.RatioDTO.RatioId.*;

public class StatesStatistics {
	private final String twinName;
	private final StatesService statesService;

	public StatesStatistics(String twinName, StatesService statesService) {
		this.twinName = twinName;
		this.statesService = statesService;
	}

	private long totalSecondsIf(Collection<TwinState> states, Predicate<TwinState> condition) {
		return states.stream().filter(condition).mapToLong(TwinState::durationSeconds).sum();
	}

	private long totalStatesThat(Collection<TwinState> states, Predicate<TwinState> condition) {
		return states.stream().filter(condition).count();
	}

	public Collection<RatioDTO> calculateRatios(DateRange dateRange) {
		Collection<TwinState> states =
			statesService.getStatesBetween(twinName, dateRange.getStartDate(), dateRange.getEndDate());

		long workingSeconds = totalSecondsIf(states, state -> state.stateIdIs(TwinStateId.DOING_PROCESS));
		long standbySeconds = totalSecondsIf(states, state -> state.stateIdIs(TwinStateId.IN_STANDBY));
		long breakdownSeconds = totalSecondsIf(states, state -> state.stateIdIs(TwinStateId.IN_BREAKDOWN));
		long offSeconds = totalSecondsIf(states, state -> state.stateIdIs(TwinStateId.OFF));
		long completedProcesses = totalStatesThat(states, state ->
			state.stateIdIs(TwinStateId.DOING_PROCESS) && state.lastEventTypeIs(EventType.END_PROCESS));
		long abortedProcesses = totalStatesThat(states, state ->
			state.stateIdIs(TwinStateId.DOING_PROCESS) && state.lastEventTypeIs(EventType.ERROR));

		return List.of(
			new RatioDTO(AVAILABILITY, (workingSeconds + standbySeconds), (breakdownSeconds + offSeconds)),
			new RatioDTO(EFFICIENCY, workingSeconds, standbySeconds),
			new RatioDTO(EFFECTIVENESS, completedProcesses, (completedProcesses + abortedProcesses)));
	}

	public MachineUsageDTO calculateMachineUsage(DateRange dateRange) {
		Collection<TwinState> states =
			statesService.getStatesBetween(twinName, dateRange.getStartDate(), dateRange.getEndDate());

		long workingSeconds = totalSecondsIf(states, state -> state.stateIdIs(TwinStateId.DOING_PROCESS));
		long onSeconds = totalSecondsIf(states, state -> state.stateIdIs(TwinStateId.IN_STANDBY)) +
		                 totalSecondsIf(states, state -> state.stateIdIs(TwinStateId.IN_BREAKDOWN));

		return new MachineUsageDTO(workingSeconds, onSeconds);
	}

	public TimeDistributionDTO calculateTimeDistribution(DateRange dateRange) {
		Collection<TwinState> states =
			statesService.getStatesBetween(twinName, dateRange.getStartDate(), dateRange.getEndDate());

		long processingGlassSeconds = totalSecondsIf(states, state -> state.stateIsDoing(ProcessName.CUT)) +
		                              totalSecondsIf(states, state -> state.stateIsDoing(ProcessName.LOWE));
		long loadingGlassSeconds = totalSecondsIf(states, state -> state.stateIsDoing(ProcessName.LOAD_GLASS));
		long standbySeconds = totalSecondsIf(states, state -> state.stateIdIs(TwinStateId.IN_STANDBY));

		return new TimeDistributionDTO(processingGlassSeconds, loadingGlassSeconds, standbySeconds);
	}
}

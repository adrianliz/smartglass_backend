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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.turomas.smartglass.twins.domain.dtos.RatioDTO.RatioId.*;

public class TwinMetrics {
	private final DateRange dateRange;
	private final Map<ProcessName, Long> workingSeconds;
	private final Map<TwinStateId, Long> dontWorkingSeconds;
	private TwinState lastStateEvaluated;
	private long completedProcesses;
	private long abortedProcesses;

	public TwinMetrics(DateRange dateRange) {
		this.dateRange = dateRange;
		workingSeconds = new HashMap<>();
		dontWorkingSeconds = new HashMap<>();
		completedProcesses = 0;
		abortedProcesses = 0;
	}

	private <T> void updateSeconds(Map<T, Long> seconds, T key, long newSeconds) {
		Long previousSeconds;

		if ((previousSeconds = seconds.get(key)) != null) {
			seconds.put(key, previousSeconds + newSeconds);
		} else {
			seconds.put(key, newSeconds);
		}
	}

	private void updateSecondsInState(TwinState twinState) {
		long secondsInState = twinState.durationSeconds();

		if (twinState.stateIdIs(TwinStateId.DOING_PROCESS)) {
			updateSeconds(workingSeconds, twinState.getProcessInProgress(), secondsInState);
		} else {
			updateSeconds(dontWorkingSeconds, twinState.getTwinStateId(), secondsInState);
		}
	}

	private void updateProcessesResult(TwinState twinState) {
		if (twinState.stateIdIs(TwinStateId.DOING_PROCESS)) {
			if (twinState.lastEventTypeIs(EventType.END_PROCESS)) {
				completedProcesses++;
			} else if (twinState.lastEventTypeIs(EventType.ERROR)) {
				abortedProcesses++;
			}
		}
	}

	private void updateMetrics(Collection<TwinState> twinStates) {
		for (TwinState twinState : twinStates) {
			updateSecondsInState(twinState);
			updateProcessesResult(twinState);

			lastStateEvaluated = twinState;
		}
	}

	private synchronized void updateMetrics(String twinName, StatesService statesService) {
		if (lastStateEvaluated != null) {
			updateMetrics(
				statesService.getStatesBetween(twinName, lastStateEvaluated.getLastEventEvaluated().getTimestamp(),
					dateRange.getEndDate()));
		} else {
			updateMetrics(statesService.getStatesBetween(twinName, dateRange.getStartDate(), dateRange.getEndDate()));
		}
	}

	public Collection<RatioDTO> calculateRatios(String twinName, StatesService statesService) {
		updateMetrics(twinName, statesService);

		long workingSeconds = this.workingSeconds.values().stream().mapToLong(Long::longValue).sum();
		long standbySeconds = dontWorkingSeconds.getOrDefault(TwinStateId.IN_STANDBY, 0L);
		long breakdownSeconds = dontWorkingSeconds.getOrDefault(TwinStateId.IN_BREAKDOWN, 0L);
		long offSeconds = dontWorkingSeconds.getOrDefault(TwinStateId.OFF, 0L);

		return List.of(
			new RatioDTO(AVAILABILITY, (workingSeconds + standbySeconds), (breakdownSeconds + offSeconds)),
			new RatioDTO(EFFICIENCY, workingSeconds, standbySeconds),
			new RatioDTO(EFFECTIVENESS, completedProcesses, (completedProcesses + abortedProcesses)));
	}

	public MachineUsageDTO calculateMachineUsage(String twinName, StatesService statesService) {
		updateMetrics(twinName, statesService);

		long workingSeconds = this.workingSeconds.values().stream().mapToLong(Long::longValue).sum();
		long machineOnSeconds =
			dontWorkingSeconds.getOrDefault(TwinStateId.IN_STANDBY, 0L)
				+ dontWorkingSeconds.getOrDefault(TwinStateId.IN_BREAKDOWN, 0L);

		return new MachineUsageDTO(workingSeconds, machineOnSeconds);
	}

	public TimeDistributionDTO calculateTimeDistribution(String twinName, StatesService statesService) {
		updateMetrics(twinName, statesService);

		long processingGlassSeconds =
			workingSeconds.getOrDefault(ProcessName.CUT, 0L)
				+ workingSeconds.getOrDefault(ProcessName.LOWE, 0L)
				+ workingSeconds.getOrDefault(ProcessName.TPF, 0L)
				+ workingSeconds.getOrDefault(ProcessName.VINIL, 0L);

		long loadingGlassSeconds = workingSeconds.getOrDefault(ProcessName.LOAD_GLASS, 0L);
		long dontWorkingSeconds = this.dontWorkingSeconds.values().stream().mapToLong(Long::longValue).sum();

		return new TimeDistributionDTO(processingGlassSeconds, loadingGlassSeconds, dontWorkingSeconds);
	}
}

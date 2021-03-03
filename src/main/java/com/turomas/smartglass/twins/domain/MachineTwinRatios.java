package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.repositories.MachineEventRepository;
import com.turomas.smartglass.twins.domain.dto.RatioDTO;
import com.turomas.smartglass.twins.domain.dto.WorkingStatisticsDTO;

import java.time.Duration;
import java.util.List;
import java.util.SortedSet;

import static com.turomas.smartglass.twins.domain.dto.RatioType.*;

public class MachineTwinRatios {
  private final String machineName;
  private final DateRange dateRange;
  private MachineEvent lastEventEvaluated;
  private long machineActiveSeconds;
  private long machineBreakdownSeconds;
  private long machineWorkingSeconds;
  private long wastedSeconds;
  private long completedProcesses;
  private long abortedProcesses;

  public MachineTwinRatios(String machineName, DateRange dateRange) {
    this.machineName = machineName;
    this.dateRange = dateRange;
    machineActiveSeconds = 0;
    machineBreakdownSeconds = 0;
    machineWorkingSeconds = 0;
    wastedSeconds = 0;
    completedProcesses = 0;
    abortedProcesses = 0;
  }

  private SortedSet<MachineEvent> getNewEventsProduced(
      MachineEventRepository machineEventRepository) {
    SortedSet<MachineEvent> newEventsProduced;

    if (lastEventEvaluated != null) {
      newEventsProduced =
          machineEventRepository.searchEventsBetween(
              machineName, lastEventEvaluated.getTimestamp(), dateRange.getEndDate());
    } else {
      newEventsProduced =
          machineEventRepository.searchEventsBetween(
              machineName, dateRange.getStartDate(), dateRange.getEndDate());

      if (!newEventsProduced.isEmpty()) {
        lastEventEvaluated = newEventsProduced.first();
        newEventsProduced.remove(lastEventEvaluated);
      }
    }

    return newEventsProduced;
  }

  private void updateRatios(MachineEventRepository machineEventRepository) {
    for (MachineEvent newEventProduced : getNewEventsProduced(machineEventRepository)) {
      Duration timeSinceLastEvent =
          Duration.between(lastEventEvaluated.getTimestamp(), newEventProduced.getTimestamp());

      if (lastEventEvaluated.machineIsInBreakdown()) {
        machineBreakdownSeconds += timeSinceLastEvent.getSeconds();
      } else {
        machineActiveSeconds += timeSinceLastEvent.getSeconds();
      }

      if (lastEventEvaluated.machineStartsProcess()) {
        if (newEventProduced.machineCompletesProcess(lastEventEvaluated)) {
          completedProcesses++;
        } else if (newEventProduced.machineIsInBreakdown()) {
          abortedProcesses++;
        }

        machineWorkingSeconds += timeSinceLastEvent.getSeconds();
      } else if (lastEventEvaluated.machineIsAvailable()) {
        wastedSeconds += timeSinceLastEvent.getSeconds();
      }

      lastEventEvaluated = newEventProduced;
    }
  }

  public List<RatioDTO> calculateRatios(MachineEventRepository machineEventRepository) {
    updateRatios(machineEventRepository);
    return List.of(
        new RatioDTO(AVAILABILITY, machineActiveSeconds, machineBreakdownSeconds),
        new RatioDTO(EFFICIENCY, machineWorkingSeconds, wastedSeconds),
        new RatioDTO(EFFECTIVENESS, completedProcesses, abortedProcesses));
  }

  public WorkingStatisticsDTO calculateWorkingStatistics(
      MachineEventRepository machineEventRepository) {

    updateRatios(machineEventRepository);
    return new WorkingStatisticsDTO(machineWorkingSeconds, machineActiveSeconds);
  }
}

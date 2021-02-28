package com.turomas.smartglass.machineTwin.domain;

import com.turomas.smartglass.machineEvent.domain.MachineEvent;
import com.turomas.smartglass.machineEvent.repositories.MachineEventRepository;

import java.time.Duration;
import java.util.List;
import java.util.SortedSet;

import static com.turomas.smartglass.machineTwin.domain.RatioType.*;

public class MachineTwinRatios {
  private final String machineName;
  private final Period period;
  private MachineEvent lastEventEvaluated;
  private long machineActiveSeconds;
  private long machineBreakdownSeconds;
  private long machineWorkingSeconds;
  private long wastedSeconds;
  private long completedProcesses;
  private long abortedProcesses;

  public MachineTwinRatios(String machineTwinName, Period period) {
    this.machineName = machineTwinName;
    this.period = period;
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
          machineEventRepository.searchBy(
              machineName, lastEventEvaluated.getTimestamp(), period.getEndDate());
    } else {
      newEventsProduced =
          machineEventRepository.searchBy(machineName, period.getStartDate(), period.getEndDate());

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

  private RatioDTO calculateAvailability() {
    long totalTime = machineActiveSeconds + machineBreakdownSeconds;
    double availability = 0;

    if (totalTime > 0) {
      availability = (double) machineActiveSeconds / totalTime;
    }

    return new RatioDTO(AVAILABILITY, availability);
  }

  private RatioDTO calculateEfficiency() {
    long totalTime = machineWorkingSeconds + wastedSeconds;
    double efficiency = 0;

    if (totalTime > 0) {
      efficiency = (double) machineWorkingSeconds / totalTime;
    }

    return new RatioDTO(EFFICIENCY, efficiency);
  }

  private RatioDTO calculateEffectiveness() {
    long totalProcesses = completedProcesses + abortedProcesses;
    double effectiveness = 0;

    if (totalProcesses > 0) {
      effectiveness = (double) completedProcesses / totalProcesses;
    }

    return new RatioDTO(EFFECTIVENESS, effectiveness);
  }

  public List<RatioDTO> calculateRatios(MachineEventRepository machineEventRepository) {
    updateRatios(machineEventRepository);

    return List.of(calculateAvailability(), calculateEfficiency(), calculateEffectiveness());
  }
}

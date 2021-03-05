package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.twins.domain.dto.RatioDTO;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import static com.turomas.smartglass.twins.domain.dto.RatioType.*;

public class MachineRatios {
  private final DateRange dateRange;
  private MachineEvent lastEventEvaluated;
  private MachineProcess lastProcessEvaluated;
  private long activeSeconds;
  private long breakdownSeconds;
  private long workingSeconds;
  private long wastedSeconds;
  private long completedProcesses;
  private long abortedProcesses;

  public MachineRatios(DateRange dateRange) {
    this.dateRange = dateRange;
    activeSeconds = 0;
    breakdownSeconds = 0;
    workingSeconds = 0;
    wastedSeconds = 0;
    completedProcesses = 0;
    abortedProcesses = 0;
  }

  private void update(Collection<MachineEvent> events, Collection<MachineProcess> processes) {
    if (lastEventEvaluated != null) {
      DateRange dateRange =
          new DateRange(lastEventEvaluated.getTimestamp(), this.dateRange.getEndDate());
      events.stream().filter(event -> event.happenedBetween(dateRange)).forEach(this::update);
    } else {
      events.stream().filter(event -> event.happenedBetween(dateRange)).forEach(this::update);
    }

    if (lastProcessEvaluated != null) {
      DateRange dateRange =
          new DateRange(lastProcessEvaluated.getEndDate(), this.dateRange.getEndDate());
      processes.stream()
          .filter(process -> process.startsBetween(dateRange) && !process.inProgress())
          .forEach(this::update);
    } else {
      processes.stream()
          .filter(process -> process.startsBetween(dateRange) && !process.inProgress())
          .forEach(this::update);
    }
  }

  private void update(MachineEvent event) {
    if (lastEventEvaluated != null) {
      Duration timeSinceLastEvent =
          Duration.between(lastEventEvaluated.getTimestamp(), event.getTimestamp());

      if (lastEventEvaluated.machineIsInBreakdown()) {
        breakdownSeconds += timeSinceLastEvent.getSeconds();
      } else {
        activeSeconds += timeSinceLastEvent.getSeconds();
      }
    }

    lastEventEvaluated = event;
  }

  private void update(MachineProcess process) {
    workingSeconds += process.workingSeconds();

    if (process.completed()) {
      completedProcesses++;
    } else {
      abortedProcesses++;
    }

    if (lastProcessEvaluated != null) {
      wastedSeconds += process.wastedSeconds(lastProcessEvaluated);
    }

    lastProcessEvaluated = process;
  }

  public Collection<RatioDTO> calculate(
      Collection<MachineEvent> events, Collection<MachineProcess> processes) {

    update(events, processes);

    return List.of(
        new RatioDTO(AVAILABILITY, activeSeconds, breakdownSeconds),
        new RatioDTO(EFFICIENCY, workingSeconds, wastedSeconds),
        new RatioDTO(EFFECTIVENESS, completedProcesses, abortedProcesses));
  }
}

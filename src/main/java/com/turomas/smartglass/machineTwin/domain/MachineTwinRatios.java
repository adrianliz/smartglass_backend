package com.turomas.smartglass.machineTwin.domain;

import com.turomas.smartglass.machineEvent.domain.EventType;
import com.turomas.smartglass.machineEvent.domain.MachineEvent;
import com.turomas.smartglass.machineEvent.repositories.MachineEventRepository;

import java.time.ZoneOffset;
import java.util.SortedSet;

public class MachineTwinRatios {
  private final String machineName;
  private final Period period;
  private long activeTime;
  private long stoppedTime;
  private MachineEvent lastEventEvaluated;

  public MachineTwinRatios(String machineTwinName, Period period) {
    this.machineName = machineTwinName;
    this.period = period;
    activeTime = 0;
    stoppedTime = 0;
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

  public RatioDTO calculateAvailability(MachineEventRepository machineEventRepository) {
    for (MachineEvent newEventProduced : getNewEventsProduced(machineEventRepository)) {
      EventType lastEventType = lastEventEvaluated.getEventType();
      long msNewEventProduced = newEventProduced.getTimestamp().toEpochSecond(ZoneOffset.UTC);
      long msLastEventProduced = lastEventEvaluated.getTimestamp().toEpochSecond(ZoneOffset.UTC);

      if ((lastEventType.equals(EventType.ERROR))
          || (lastEventType.equals(EventType.POWER_OFF))
          || (newEventProduced.getEventType().equals(EventType.POWER_ON))) {
        stoppedTime += msNewEventProduced - msLastEventProduced;
      } else {
        activeTime += msNewEventProduced - msLastEventProduced;
      }

      lastEventEvaluated = newEventProduced;
    }

    long totalTime = activeTime + stoppedTime;
    double availability = 1;
    if (totalTime > 0) {
      availability = (double) activeTime / totalTime;
    }

    return new RatioDTO(
        machineName, RatioType.AVAILABILITY, availability, period, lastEventEvaluated);
  }
}

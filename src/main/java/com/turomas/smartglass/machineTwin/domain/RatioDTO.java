package com.turomas.smartglass.machineTwin.domain;

import com.turomas.smartglass.machineEvent.domain.MachineEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RatioDTO {
  private final String machineName;
  private final RatioType ratioType;
  private final double ratioValue;
  private final Period period;
  private final MachineEvent lastEventEvaluated;
}

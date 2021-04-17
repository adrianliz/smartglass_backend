package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class MachineUsageDTO {
  private final long workingSeconds;
  private final long onSeconds;
}

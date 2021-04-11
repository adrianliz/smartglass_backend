package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.turomas.smartglass.twins.domain.dtos.statistics.ToHoursUtil.transformToHours;

@Getter
@EqualsAndHashCode
public class MachineUsageDTO {
  private final long workingHours;
  private final long onHours;

  public MachineUsageDTO(long workingSeconds, long onSeconds) {
    workingHours = transformToHours(workingSeconds);
    onHours = transformToHours(onSeconds);
  }
}

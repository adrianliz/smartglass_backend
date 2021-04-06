package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.turomas.smartglass.twins.domain.dtos.statistics.TimeDistributionDTO.SECONDS_TO_HOURS_FACTOR;

@Getter
public class MachineUsageDTO {
  private final long timestamp;
  private final long workingHours;
  private final long onHours;

  public MachineUsageDTO(LocalDateTime date, long workingSeconds, long machineOnSeconds) {
    this.timestamp = ZonedDateTime.of(date, ZoneId.of("UTC")).toInstant().toEpochMilli();
    workingHours = workingSeconds / SECONDS_TO_HOURS_FACTOR;
    onHours = machineOnSeconds / SECONDS_TO_HOURS_FACTOR;
  }
}

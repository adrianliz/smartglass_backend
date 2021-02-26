package com.turomas.smartglass.machineTwin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.turomas.smartglass.machineTwin.domain.exceptions.InvalidPeriod;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class Period {
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private final LocalDateTime startDate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private final LocalDateTime endDate;

  public Period(LocalDateTime startDate, LocalDateTime endDate) throws InvalidPeriod {
    if (startDate.compareTo(endDate) >= 0) {
      throw new InvalidPeriod(startDate, endDate);
    }

    this.startDate = startDate;
    this.endDate = endDate;
  }
}

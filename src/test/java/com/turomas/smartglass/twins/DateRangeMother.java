package com.turomas.smartglass.twins;

import com.turomas.smartglass.twins.domain.DateRange;

import java.time.LocalDateTime;

public class DateRangeMother {
  public static DateRange random() {
    return new DateRange(LocalDateTime.now(), LocalDateTime.now().plusSeconds(1 + Math.round(Math.random())));
  }
}

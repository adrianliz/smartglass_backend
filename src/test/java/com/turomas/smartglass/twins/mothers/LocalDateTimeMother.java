package com.turomas.smartglass.twins.mothers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimeMother {
  public static LocalDateTime todayAt14() {
    return LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0));
  }
}

package com.turomas.smartglass.twins.mothers;

import com.turomas.smartglass.twins.domain.DateRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateRangeMother {
  public static DateRange today() {
    return new DateRange(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
                         LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
  }

  public static DateRange yesterday() {
    return new DateRange(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT),
                         LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX));
  }

  public static DateRange tomorrow() {
    return new DateRange(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT),
                         LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX));
  }

  public static DateRange endTodayUntilEndTomorrow() {
    return new DateRange(LocalDateTime.of(LocalDate.now(), LocalTime.MAX),
                         LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX));
  }

  public static DateRange yesterdayMidnightUntilTodayMidnight() {
    return new DateRange(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT),
                         LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
  }

  public static DateRange yesterdayMidnightUtilEndToday() {
    return new DateRange(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT),
                         LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
  }

  public static DateRange yesterdayMinightUntilEndTomorrow() {
    return new DateRange(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT),
                         LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX));
  }
}

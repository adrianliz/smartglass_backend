package com.turomas.smartglass.twins.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public enum Period {
  TODAY {
    @Override
    public DateRange getDateRange() {
      return new DateRange(LocalDate.now().atTime(0, 0), LocalDate.now().atTime(23, 59));
    }
  },
  THIS_WEEK {
    @Override
    public DateRange getDateRange() {
      return new DateRange(
          LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atTime(0, 0),
          LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(23, 59));
    }
  },
  THIS_MONTH {
    @Override
    public DateRange getDateRange() {
      return new DateRange(
          LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atTime(0, 0),
          LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59));
    }
  },
  THIS_YEAR {
    @Override
    public DateRange getDateRange() {
      return new DateRange(
          LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atTime(0, 0),
          LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atTime(23, 59));
    }
  },
  ALL {
    @Override
    public DateRange getDateRange() {
      return new DateRange(LocalDate.EPOCH.atTime(0, 0), LocalDate.now().atTime(23, 59));
    }
  };

  public abstract DateRange getDateRange();
}

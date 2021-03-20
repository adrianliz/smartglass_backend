package com.turomas.smartglass.twins.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public enum Period {
  TODAY {
    @Override
    public DateRange getDateRange() {
      return new DateRange(
        LocalDate.now().atTime(START_DAY_HOUR, START_DAY_MINUTE),
        LocalDate.now().atTime(END_DAY_HOUR, END_DAY_MINUTE));
    }
  },
  THIS_WEEK {
    @Override
    public DateRange getDateRange() {
      return new DateRange(
        LocalDate.now()
          .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
          .atTime(START_DAY_HOUR, START_DAY_MINUTE),
        LocalDate.now()
          .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
          .atTime(END_DAY_HOUR, END_DAY_MINUTE));
    }
  },
  THIS_MONTH {
    @Override
    public DateRange getDateRange() {
      return new DateRange(
        LocalDate.now()
          .with(TemporalAdjusters.firstDayOfMonth())
          .atTime(START_DAY_HOUR, START_DAY_MINUTE),
        LocalDate.now()
          .with(TemporalAdjusters.lastDayOfMonth())
          .atTime(END_DAY_HOUR, END_DAY_MINUTE));
    }
  },
  THIS_YEAR {
    @Override
    public DateRange getDateRange() {
      return new DateRange(
        LocalDate.now()
          .with(TemporalAdjusters.firstDayOfYear())
          .atTime(START_DAY_HOUR, START_DAY_MINUTE),
        LocalDate.now()
          .with(TemporalAdjusters.lastDayOfYear())
          .atTime(END_DAY_HOUR, END_DAY_MINUTE));
    }
  },
  ALL {
    @Override
    public DateRange getDateRange() {
      return new DateRange(
        LocalDate.EPOCH.atTime(START_DAY_HOUR, START_DAY_MINUTE),
        LocalDate.now().atTime(END_DAY_HOUR, END_DAY_MINUTE));
    }
  };

  private static final int START_DAY_HOUR = 0;
  private static final int START_DAY_MINUTE = 0;
  private static final int END_DAY_HOUR = 23;
  private static final int END_DAY_MINUTE = 59;

  public abstract DateRange getDateRange();
}

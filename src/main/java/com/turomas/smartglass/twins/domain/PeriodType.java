package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.rest.exceptions.InvalidPeriodType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public enum PeriodType {
  TODAY("TODAY") {
    @Override
    public Period getPeriod() {
      return new Period(LocalDate.now().atTime(0, 0), LocalDate.now().atTime(23, 59));
    }
  },
  THIS_WEEK("THIS_WEEK") {
    @Override
    public Period getPeriod() {
      return new Period(
          LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atTime(0, 0),
          LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(23, 59));
    }
  },
  THIS_MONTH("THIS_MONTH") {
    @Override
    public Period getPeriod() {
      return new Period(
          LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atTime(0, 0),
          LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59));
    }
  },
  THIS_YEAR("THIS_YEAR") {
    @Override
    public Period getPeriod() {
      return new Period(
          LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atTime(0, 0),
          LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atTime(23, 59));
    }
  };

  private final String periodType;
  private Period period;

  PeriodType(String periodType) {
    this.periodType = periodType;
  }

  public abstract Period getPeriod();
}

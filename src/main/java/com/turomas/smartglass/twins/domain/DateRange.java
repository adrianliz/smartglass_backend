package com.turomas.smartglass.twins.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.exceptions.InvalidDateRange;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.services.StatesService;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@EqualsAndHashCode
public class DateRange {
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NonNull
  private final LocalDateTime startDate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NonNull
  private final LocalDateTime endDate;

  public DateRange(LocalDateTime startDate, LocalDateTime endDate) throws InvalidDateRange {
    if (startDate == null || endDate == null) throw new InvalidDateRange();

    if (startDate.compareTo(endDate) >= 0) {
      throw new InvalidDateRange(startDate, endDate);
    }

    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Collection<TwinState> getOverlapStates(String twinName, StatesService statesService) {
    if (statesService != null) {
      return statesService.getOverlapStates(twinName, startDate, endDate);
    }

    return Collections.emptyList();
  }


  public Collection<Event> getEventsInside(String twinName, EventsService eventsService) {
    if (eventsService != null) {
      return eventsService.getEventsBetween(twinName, startDate, endDate);
    }

    return Collections.emptyList();
  }

  private boolean overlaps(DateRange dateRange) {
    return ((dateRange != null) && startDate.isBefore(dateRange.endDate) &&
            dateRange.startDate.isBefore(endDate));
  }

  public long overlapSecondsWith(DateRange dateRange) {
    if (overlaps(dateRange)) {
      LocalDateTime overlapStart = dateRange.startDate.isBefore(startDate) ? startDate : dateRange.startDate;
      LocalDateTime overlapEnd = dateRange.endDate.isBefore(endDate) ? dateRange.endDate : endDate;

      return Duration.between(overlapStart, overlapEnd).getSeconds();
    }

    return 0;
  }
}

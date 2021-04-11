package com.turomas.smartglass.twins.domain.dtos.statistics;

public class ToHoursUtil {
  private static final int SECONDS_TO_HOURS_FACTOR = 3600;
  static long transformToHours(long seconds) {
    if (seconds > 0) {
      return seconds / SECONDS_TO_HOURS_FACTOR;
    }

    return 0;
  }
}

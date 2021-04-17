package com.turomas.smartglass.twins;

import com.turomas.smartglass.twins.domain.DateRange;
import com.turomas.smartglass.twins.mothers.DateRangeMother;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateRangeShouldTest {
  // testing https://user-images.githubusercontent.com/69039986/115106303-4bab5e00-9f64-11eb-97c1-ea3e8443754a.png
  private static Stream<Arguments> testOverlapSeconds() {
    return Stream.of(
      // should overlap 0 seconds (after)
      Arguments.of(0, DateRangeMother.today(), DateRangeMother.tomorrow()),

      // should overlap 0 seconds (start touching)
      Arguments.of(0, DateRangeMother.today(), DateRangeMother.yesterdayMidnightUntilTodayMidnight()),

      // should overlap 15 seconds (start inside)
      Arguments.of(15, DateRangeMother.today(),
                   new DateRange(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
                                 LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 15)))),

      // should overlap 86399 seconds (inside start touching)
      Arguments.of(86399, DateRangeMother.today(),
                   new DateRange(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
                                 LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT))),

      // should overlap 20 seconds (enclosing start touching)
      Arguments.of(20, DateRangeMother.today(),
                   new DateRange(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
                                 LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 20)))),

      // should overlap 10 seconds (enclosing)
      Arguments.of(10, DateRangeMother.today(),
                   new DateRange(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
                                 LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 10)))),

      // should overlap 30 seconds (enclosing end touching)
      Arguments.of(20, DateRangeMother.today(),
                   new DateRange(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 39)),
                                 LocalDateTime.of(LocalDate.now(), LocalTime.MAX))),

      // should overlap 86399 seconds (exact match)
      Arguments.of(86399, DateRangeMother.today(), DateRangeMother.today()),

      // should overlap 86399 seconds (inside)
      Arguments.of(86399, DateRangeMother.today(), DateRangeMother.yesterdayMinightUntilEndTomorrow()),

      // should overlap 86399 seconds (inside end touching)
      Arguments.of(86399, DateRangeMother.today(), DateRangeMother.yesterdayMidnightUtilEndToday()),

      // should overlap 40 seconds (end inside)
      Arguments.of(40, DateRangeMother.today(),
                   new DateRange(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 19)),
                                 LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX))),

      // should overlap 0 seconds (end touching)
      Arguments.of(0, DateRangeMother.today(), DateRangeMother.endTodayUntilEndTomorrow()),

      // should overlap 0 seconds (before)
      Arguments.of(0, DateRangeMother.today(), DateRangeMother.yesterday()));
  }

  @ParameterizedTest
  @MethodSource("testOverlapSeconds")
  void shouldOverlapSeconds(long expectedOverlapSeconds, DateRange testDateRange, DateRange withDateRange) {
    assertEquals(expectedOverlapSeconds, testDateRange.overlapSecondsWith(withDateRange));
  }
}

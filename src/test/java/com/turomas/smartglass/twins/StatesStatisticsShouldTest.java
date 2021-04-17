package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.DateRange;
import com.turomas.smartglass.twins.domain.dtos.statistics.MachineUsageDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.RatioDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.TimeDistributionDTO;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import com.turomas.smartglass.twins.mothers.DateRangeMother;
import com.turomas.smartglass.twins.mothers.LocalDateTimeMother;
import com.turomas.smartglass.twins.mothers.StatesMother;
import com.turomas.smartglass.twins.mothers.StatisticsMother;
import com.turomas.smartglass.twins.services.StatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class StatesStatisticsShouldTest {
  private static Stream<Arguments> testRatios() {
    return Stream.of(
      // Should have AVAILABILITY of 50%, EFFICIENCY of 50% and EFFECTIVENESS of 50%
      Arguments.of(
        List.of(
          new RatioDTO(RatioDTO.RatioId.AVAILABILITY, 20, 20),
          new RatioDTO(RatioDTO.RatioId.EFFICIENCY, 10, 10),
          new RatioDTO(RatioDTO.RatioId.EFFECTIVENESS, 1, 1)),
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, LocalDateTimeMother.todayAt14(), 10),
                StatesMother.of(TwinStateType.IN_STANDBY, LocalDateTimeMother.todayAt14(), 10),
                StatesMother.of(TwinStateType.IN_BREAKDOWN, LocalDateTimeMother.todayAt14(), 10),
                StatesMother.of(TwinStateType.OFF, LocalDateTimeMother.todayAt14(), 10),
                StatesMother.of(TwinStateType.DOING_PROCESS, EventType.END_PROCESS),
                StatesMother.of(TwinStateType.DOING_PROCESS, EventType.ERROR)),
        DateRangeMother.today()),

      // Should have AVAILABILITY of 0%, EFFICIENCY of 0% and EFFECTIVENESS of 0% when there arent transited states
      Arguments.of(
        List.of(
          new RatioDTO(RatioDTO.RatioId.AVAILABILITY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFICIENCY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFECTIVENESS, 0, 0)),
        Collections.emptyList(),
        DateRangeMother.today()),

      // Should have AVAILABILITY of 0%, EFFICIENCY of 0% and EFFECTIVENESS of 0% when transited states have a duration
      // less or equals to 0 seconds
      Arguments.of(
        List.of(
          new RatioDTO(RatioDTO.RatioId.AVAILABILITY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFICIENCY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFECTIVENESS, 0, 0)),
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, LocalDateTimeMother.todayAt14(), 0),
                StatesMother.of(TwinStateType.IN_STANDBY, LocalDateTimeMother.todayAt14(), - 2),
                StatesMother.of(TwinStateType.IN_BREAKDOWN, LocalDateTimeMother.todayAt14(), - 134),
                StatesMother.of(TwinStateType.OFF, LocalDateTimeMother.todayAt14(), 0)),
        DateRangeMother.today()),

      // Should have AVAILABILITY of 0%, EFFICIENCY of 0% and EFFECTIVENESS of 0% when transited states dont overlap
      // date range
      Arguments.of(
        List.of(
          new RatioDTO(RatioDTO.RatioId.AVAILABILITY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFICIENCY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFECTIVENESS, 0, 0)),
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, LocalDateTimeMother.todayAt14(), 23232),
                StatesMother.of(TwinStateType.IN_STANDBY, LocalDateTimeMother.todayAt14(), 21111),
                StatesMother.of(TwinStateType.IN_BREAKDOWN, LocalDateTimeMother.todayAt14(), 9090),
                StatesMother.of(TwinStateType.OFF, LocalDateTimeMother.todayAt14(), 333)),
        DateRangeMother.yesterday()));
  }

  private static Stream<Arguments> testMachineUsage() {
    return Stream.of(
      // Should have 50 s working and 50 s on
      Arguments.of(
        new MachineUsageDTO(50, 100),
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, LocalDateTimeMother.todayAt14(), 50),
                StatesMother.of(TwinStateType.IN_STANDBY, LocalDateTimeMother.todayAt14(), 50)),
        DateRangeMother.today()),

      // Should have 0s working and 0s on when there arent transited states
      Arguments.of(
        new MachineUsageDTO(0, 0),
        Collections.emptyList(),
        DateRangeMother.today()),

      // Should have 0s working and 0s on when transited states have a duration less or equals to 0 seconds
      Arguments.of(
        new MachineUsageDTO(0, 0),
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, LocalDateTimeMother.todayAt14(), 0),
                StatesMother.of(TwinStateType.IN_STANDBY, LocalDateTimeMother.todayAt14(), - 10)),
        DateRangeMother.today()),

      // Should have 0s working and 0s on when transited states dont overlap date range
      Arguments.of(
        new MachineUsageDTO(0, 0),
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, LocalDateTimeMother.todayAt14(), 213),
                StatesMother.of(TwinStateType.IN_STANDBY, LocalDateTimeMother.todayAt14(), 909)),
        DateRangeMother.yesterday()));
  }

  private static Stream<Arguments> testTimeDistribution() {
    return Stream.of(
      // Should have 50 s processing glass 50 s loading glass and 50 s in standby
      Arguments.of(
        new TimeDistributionDTO(50, 50, 50),
        List.of(StatesMother.doingProcess(ProcessName.CUT, LocalDateTime.now(), 50),
                StatesMother.doingProcess(ProcessName.LOAD_GLASS, LocalDateTimeMother.todayAt14(), 50),
                StatesMother.of(TwinStateType.IN_STANDBY, LocalDateTimeMother.todayAt14(), 50)),
        DateRangeMother.today()),

      // Should have 0 s processing glass 0 s loading glass and 0 s in standby when there arent transited states
      Arguments.of(
        new TimeDistributionDTO(0, 0, 0),
        Collections.emptyList(),
        DateRangeMother.today()),

      // Should have 0 s processing glass 0 s loading glass and 0 s in standby when transited states have a duration
      // less or equals to 0 seconds
      Arguments.of(
        new TimeDistributionDTO(0, 0, 0),
        List.of(StatesMother.doingProcess(ProcessName.CUT, LocalDateTimeMother.todayAt14(), 0),
                StatesMother.doingProcess(ProcessName.LOAD_GLASS, LocalDateTimeMother.todayAt14(), - 10),
                StatesMother.of(TwinStateType.IN_STANDBY, LocalDateTimeMother.todayAt14(), - 100)),
        DateRangeMother.today()),

      // Should have 0 s processing glass 0 s loading glass and 0 s in standby when transited states dont
      // overlap date range
      Arguments.of(
        new TimeDistributionDTO(0, 0, 0),
        List.of(StatesMother.doingProcess(ProcessName.CUT, LocalDateTimeMother.todayAt14(), 303),
                StatesMother.doingProcess(ProcessName.LOAD_GLASS, LocalDateTimeMother.todayAt14(), 101),
                StatesMother.of(TwinStateType.IN_STANDBY, LocalDateTimeMother.todayAt14(), 103)),
        DateRangeMother.yesterday()));
  }

  private StatesService statesService;

  @BeforeEach
  void setUp() {
    statesService = mock(StatesService.class);
  }

  @ParameterizedTest
  @MethodSource("testRatios")
  void haveRatios(Collection<RatioDTO> expectedRatios, Collection<TwinState> mockTransitedStates, DateRange dateRange) {
    Collection<RatioDTO> ratios =
      StatisticsMother.of(statesService, mockTransitedStates).calculateRatios(dateRange);

    assertTrue(ratios.containsAll(expectedRatios));
  }

  @ParameterizedTest
  @MethodSource("testMachineUsage")
  void haveMachineUsage(MachineUsageDTO expectedMachineUsage, Collection<TwinState> mockTransitedStates,
                        DateRange dateRange) {

    MachineUsageDTO machineUsage =
      StatisticsMother.of(statesService, mockTransitedStates).calculateMachineUsage(dateRange);

    assertEquals(machineUsage, expectedMachineUsage);
  }

  @ParameterizedTest
  @MethodSource("testTimeDistribution")
  void haveTimeDistribution(TimeDistributionDTO expectedTimeDistribution, Collection<TwinState> mockTransitedStates,
                            DateRange dateRange) {

    TimeDistributionDTO timeDistribution =
      StatisticsMother.of(statesService, mockTransitedStates).calculateTimeDistribution(dateRange);

    assertEquals(timeDistribution, expectedTimeDistribution);
  }
}

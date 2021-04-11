package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.dtos.statistics.MachineUsageDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.RatioDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.TimeDistributionDTO;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import com.turomas.smartglass.twins.services.StatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, 10),
                StatesMother.of(TwinStateType.IN_STANDBY, 10),
                StatesMother.of(TwinStateType.IN_BREAKDOWN, 10),
                StatesMother.of(TwinStateType.OFF, 10),
                StatesMother.of(TwinStateType.DOING_PROCESS, EventType.END_PROCESS),
                StatesMother.of(TwinStateType.DOING_PROCESS, EventType.ERROR))),

      // Should have AVAILABILITY of 0%, EFFICIENCY of 0% and EFFECTIVENESS of 0% when there arent transited states
      Arguments.of(
        List.of(
          new RatioDTO(RatioDTO.RatioId.AVAILABILITY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFICIENCY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFECTIVENESS, 0, 0)),
        Collections.emptyList()),

      // Should have AVAILABILITY of 0%, EFFICIENCY of 0% and EFFECTIVENESS of 0% when transited states have a duration
      // less or equals to 0 seconds
      Arguments.of(
        List.of(
          new RatioDTO(RatioDTO.RatioId.AVAILABILITY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFICIENCY, 0, 0),
          new RatioDTO(RatioDTO.RatioId.EFFECTIVENESS, 0, 0)),
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, 0),
                StatesMother.of(TwinStateType.IN_STANDBY, -2),
                StatesMother.of(TwinStateType.IN_BREAKDOWN, -134),
                StatesMother.of(TwinStateType.OFF, 0))));
  }

  private static Stream<Arguments> testMachineUsage() {
    return Stream.of(
      // Should have 50 s working and 50 s on
      Arguments.of(
        new MachineUsageDTO(50, 50),
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, 50),
                StatesMother.of(TwinStateType.IN_STANDBY, 50))),

      // Should have 0s working and 0s on when there arent transited states
      Arguments.of(
        new MachineUsageDTO(0, 0),
        Collections.emptyList()),

      // Should have 0s working and 0s on when transited states have a duration less or equals to 0 seconds
      Arguments.of(
        new MachineUsageDTO(0, 0),
        List.of(StatesMother.of(TwinStateType.DOING_PROCESS, 0),
                StatesMother.of(TwinStateType.IN_STANDBY, -10))));
  }

  private static Stream<Arguments> testTimeDistribution() {
    return Stream.of(
      // Should have 50 s processing glass 50 s loading glass and 50 s in standby
      Arguments.of(
        new TimeDistributionDTO(50, 50, 50),
        List.of(StatesMother.doingProcess(ProcessName.CUT, 50),
                StatesMother.doingProcess(ProcessName.LOAD_GLASS, 50),
                StatesMother.of(TwinStateType.IN_STANDBY, 50))),

      // Should have 0 s processing glass 0 s loading glass and 0 s in standby when there arent transited states
      Arguments.of(
        new TimeDistributionDTO(0, 0, 0),
        Collections.emptyList()),

      // Should have 0 s processing glass 0 s loading glass and 0 s in standby when transited states have a duration
      // less or equals to 0 seconds
      Arguments.of(
        new TimeDistributionDTO(0, 0, 0),
        List.of(StatesMother.doingProcess(ProcessName.CUT, 0),
                StatesMother.doingProcess(ProcessName.LOAD_GLASS, -10),
                StatesMother.of(TwinStateType.IN_STANDBY, -100))));
  }

  private StatesService statesService;

  @BeforeEach
  void setUp() {
    statesService = mock(StatesService.class);
  }

  @ParameterizedTest
  @MethodSource("testRatios")
  void haveRatios(Collection<RatioDTO> expectedRatios, Collection<TwinState> mockTransitedStates) {
    Collection<RatioDTO> ratios =
      StatesStatisticsMother.create(statesService, mockTransitedStates).calculateRatios(DateRangeMother.random());

    assertEquals(ratios, expectedRatios);
  }

  @ParameterizedTest
  @MethodSource("testMachineUsage")
  void haveMachineUsage(MachineUsageDTO expectedMachineUsage, Collection<TwinState> mockTransitedStates) {
    MachineUsageDTO machineUsage =
      StatesStatisticsMother.create(statesService, mockTransitedStates).calculateMachineUsage(DateRangeMother.random());

    assertEquals(machineUsage, expectedMachineUsage);
  }

  @ParameterizedTest
  @MethodSource("testTimeDistribution")
  void haveTimeDistribution(TimeDistributionDTO expectedTimeDistribution, Collection<TwinState> mockTransitedStates) {
    TimeDistributionDTO timeDistribution =
      StatesStatisticsMother.create(statesService, mockTransitedStates).calculateTimeDistribution(
        DateRangeMother.random());

    assertEquals(timeDistribution, expectedTimeDistribution);
  }
}

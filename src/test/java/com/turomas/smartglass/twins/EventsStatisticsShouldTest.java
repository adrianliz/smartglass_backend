package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.dtos.statistics.ErrorDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.MaterialDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.OptimizationDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.ToolsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class EventsStatisticsShouldTest {
  private static Stream<Arguments> testMaterialsUsed() {
    return Stream.of(
      // Should cut same number times of a material
      Arguments.of(
        List.of(new MaterialDTO("madera", 2)),
        List.of(EventsMother.endsCut("1", "madera"),
                EventsMother.endsCut("2", "madera"))),

      // Should have empty cut materials if not events occurred
      Arguments.of(Collections.emptyList(), Collections.emptyList()));
  }

  private static Stream<Arguments> testOptimizationsProcessed() {
    return Stream.of(
      // Should realize same number of optimizations
      Arguments.of(
        List.of(new OptimizationDTO("1", "madera", 1),
                new OptimizationDTO("2", "madera", 1)),
        List.of(EventsMother.endsCut("1", "madera"),
                EventsMother.endsCut("2", "madera"))),

      // Should have empty optimizations if not events occurred
      Arguments.of(Collections.emptyList(), Collections.emptyList()));
  }

  private static Stream<Arguments> testToolsInfo() {
    return Stream.of(
      // Should have same tools info
      Arguments.of(
        new ToolsDTO(5, 5, 2),
        List.of(EventsMother.endsCut(10, 10),
                EventsMother.endsLowe(10),
                EventsMother.endsLowe(2),
                EventsMother.endsCut(5, 5))),

      // Should have tools info with distance covered, tool angle and wheel diameter of 0
      Arguments.of(new ToolsDTO(), Collections.emptyList()));
  }

  private static Stream<Arguments> testErrorsProduced() {
    return Stream.of(
      // Should have same errors
      Arguments.of(
        List.of(new ErrorDTO("una", 1),
                new ErrorDTO("dos", 1)),
        List.of(EventsMother.error("una"),
                EventsMother.error("dos"))),

      // Should have empty errors if not events occurred
      Arguments.of(Collections.emptyList(), Collections.emptyList()));
  }

  private EventsService eventsService;

  @BeforeEach
  void setUp() {
    eventsService = mock(EventsService.class);
  }

  @ParameterizedTest
  @MethodSource("testMaterialsUsed")
  void shouldHaveMaterialsUsed(Collection<MaterialDTO> expectedMaterials, Collection<Event> mockEvents) {
    Collection<MaterialDTO> materialsUsed =
      StatisticsMother.of(eventsService, mockEvents).calculateMaterialsUsage(DateRangeMother.random());

    assertTrue(materialsUsed.containsAll(expectedMaterials));
  }

  @ParameterizedTest
  @MethodSource("testOptimizationsProcessed")
  void shouldHaveOptimizationsProcessed(Collection<OptimizationDTO> expectedOptimizations,
                                        Collection<Event> mockEvents) {
    Collection<OptimizationDTO> optimizations =
      StatisticsMother.of(eventsService, mockEvents).calculateOptimizationsProcessed(DateRangeMother.random());

    assertTrue(optimizations.containsAll(expectedOptimizations));
  }

  @ParameterizedTest
  @MethodSource("testToolsInfo")
  void shouldHaveToolsInfo(ToolsDTO expectedToolsInfo, Collection<Event> mockEvents) {
    ToolsDTO toolsInfo =
      StatisticsMother.of(eventsService, mockEvents).calculateToolsInfo(DateRangeMother.random());

    assertEquals(expectedToolsInfo, toolsInfo);
  }

  @ParameterizedTest
  @MethodSource("testErrorsProduced")
  void shouldHaveErrorsProduced(Collection<ErrorDTO> expectedErrors, Collection<Event> mockEvents) {
    Collection<ErrorDTO> errors =
      StatisticsMother.of(eventsService, mockEvents).calculateErrorsProduced(DateRangeMother.random());

    assertTrue(errors.containsAll(expectedErrors));
  }
}

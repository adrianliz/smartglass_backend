package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.exceptions.InvalidInitialState;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class StatesMachineShouldTest {
  private static Stream<Arguments> testLastStateType() {
    return Stream.of(
      // Should do transition from OFF to IN_STANDBY when POWER_ON
      Arguments.of(
        TwinStateType.IN_STANDBY,
        EventsMother.initial(EventType.POWER_ON),
        StatesMachinesMother.create(
          StatesMother.initial(TwinStateType.OFF),
          TransitionsMother.createOneTransition(Pair.of(TwinStateType.OFF, EventType.POWER_ON),
                                                TwinStateType.IN_STANDBY))),

      // Should do transition from DOING_PROCESS to IN_STANDBY when END_PROCESS with same params
      Arguments.of(
        TwinStateType.IN_STANDBY,
        EventsMother.of(EventType.END_PROCESS, ProcessName.CUT),
        StatesMachinesMother.create(
          StatesMother.doingProcess(ProcessName.CUT),
          TransitionsMother.createOneTransition(Pair.of(TwinStateType.DOING_PROCESS, EventType.END_PROCESS),
                                                TwinStateType.IN_STANDBY))),

      // Should cut transition from DOING_PROCESS to IN_STANDBY when END_PROCESS with different params
      Arguments.of(
        TwinStateType.DOING_PROCESS,
        EventsMother.of(EventType.END_PROCESS, ProcessName.LOAD_GLASS),
        StatesMachinesMother.create(StatesMother.doingProcess(ProcessName.CUT),
                                    TransitionsMother
                                      .createOneTransition(Pair.of(TwinStateType.DOING_PROCESS, EventType.END_PROCESS),
                                                           TwinStateType.IN_STANDBY))),

      // Shouldn't do transition when states machine don't have transitions defined
      Arguments.of(
        TwinStateType.OFF,
        EventsMother.initial(EventType.POWER_ON),
        StatesMachinesMother.create(StatesMother.initial(TwinStateType.OFF), null)));
  }

  private EventsService eventsService;

  @BeforeEach
  void setUp() {
    eventsService = mock(EventsService.class);
  }

  @ParameterizedTest
  @MethodSource("testLastStateType")
  void hasLastStateType(TwinStateType expectedStateType, Event mockEventOccurred, StatesMachine statesMachine) {
    Mockito.when(eventsService.getEvents(any(String.class))).thenReturn(List.of(mockEventOccurred));
    Mockito.when(eventsService.getSubsequentEvents(any(String.class), any(LocalDateTime.class))).thenReturn(
      List.of(mockEventOccurred));

    Optional<TwinState> lastStateTransited = statesMachine.processEvents(eventsService).stream().reduce(
      (first, last) -> last);

    assertTrue(lastStateTransited.isPresent() && lastStateTransited.get().typeIs(expectedStateType));
  }

  @Test
  void throwsInvalidInitialState() {
    assertThrows(InvalidInitialState.class, () -> new StatesMachine(null, null));
  }
}

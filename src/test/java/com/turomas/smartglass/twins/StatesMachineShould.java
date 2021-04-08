package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.exceptions.InvalidInitialState;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TransitionTrigger;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class StatesMachineShould {
  private static Stream<Arguments> testTransitions() {
    return Stream.of(
      // Should do transition from OFF to IN_STANDBY when POWER_ON
      Arguments.of(StateMachinesMother.create(StatesMother.create(TwinStateId.OFF),
                                              TransitionsMother.createOneTransition(
                                                new TransitionTrigger<>(TwinStateId.OFF,
                                                                        EventType.POWER_ON),
                                                TwinStateId.IN_STANDBY)),
                   EventsMother.create(EventType.POWER_ON), TwinStateId.IN_STANDBY),

      // Should do transition from DOING_PROCESS to IN_STANDBY when END_PROCESS with same params
      Arguments.of(StateMachinesMother.create(StatesMother.create(TwinStateId.DOING_PROCESS,
                                                                  EventsMother
                                                                    .create(EventType.START_PROCESS,
                                                                            ProcessName.CUT)),
                                              TransitionsMother.createOneTransition(
                                                new TransitionTrigger<>(TwinStateId.DOING_PROCESS,
                                                                        EventType.END_PROCESS),
                                                TwinStateId.IN_STANDBY)),
                   EventsMother.create(EventType.END_PROCESS, ProcessName.CUT), TwinStateId.IN_STANDBY),

      // Should cut transition from DOING_PROCESS to IN_STANDBY when END_PROCESS with different params
      Arguments.of(StateMachinesMother.create(StatesMother.create(TwinStateId.DOING_PROCESS,
                                                                  EventsMother
                                                                    .create(EventType.START_PROCESS,
                                                                            ProcessName.CUT)),
                                              TransitionsMother.createOneTransition(
                                                new TransitionTrigger<>(TwinStateId.DOING_PROCESS,
                                                                        EventType.END_PROCESS),
                                                TwinStateId.IN_STANDBY)),
                   EventsMother.create(EventType.END_PROCESS, ProcessName.LOAD_GLASS),
                   TwinStateId.DOING_PROCESS),

      // Shouldn't do transition when states machine don't have transitions defined
      Arguments.of(StateMachinesMother.create(StatesMother.create(TwinStateId.OFF), null),
                   EventsMother.create(EventType.POWER_ON), TwinStateId.OFF));
  }

  private EventsService eventsService;

  @BeforeEach
  void setUp() {
    eventsService = mock(EventsService.class);
  }

  @ParameterizedTest
  @MethodSource("testTransitions")
  void hasLastTransition(StatesMachine statesMachine, Event event, TwinStateId expectedState) {
    Mockito.when(eventsService.getEvents(any(String.class))).thenReturn(List.of(event));
    Mockito.when(eventsService.getSubsequentEvents(any(String.class), any(LocalDateTime.class))).thenReturn(
      List.of(event));

    Optional<TwinState> lastStateTransited = statesMachine.processEvents(eventsService).stream().reduce(
      (first, last) -> last);

    assertTrue(lastStateTransited.isPresent() && lastStateTransited.get().stateIdIs(expectedState));
  }

  @Test
  void throwsInvalidInitialState() {
    assertThrows(InvalidInitialState.class, () -> new StatesMachine(null, null));
  }
}

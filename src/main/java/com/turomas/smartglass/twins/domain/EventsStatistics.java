package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.dtos.statistics.ErrorDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.MaterialDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.OptimizationDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.ToolsDTO;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class EventsStatistics {
  private final String twinName;
  private final EventsService eventsService;

  public EventsStatistics(String twinName, EventsService eventsService) {
    this.twinName = twinName;
    this.eventsService = eventsService;
  }

  private Stream<Event> filterEvents(Collection<Event> events, Predicate<Event> condition) {
    return events.stream().filter(condition);
  }

  private <K> Map<K, Long> groupAndCount(Stream<Event> events, Function<Event, K> classifier) {
    return events.collect(groupingBy(classifier, Collectors.counting()));
  }

  private Optional<Event> getLastEvent(Stream<Event> events) {
    return events.reduce((first, last) -> last);
  }

  private boolean eventFinalizesProcess(Event event, ProcessName processName) {
    return event.finalizesProcess(processName);
  }

  private boolean materialWasCut(Event event) {
    return (eventFinalizesProcess(event, ProcessName.CUT) && (event.getParams().getMaterial() != null));
  }

  public Collection<MaterialDTO> calculateMaterialsUsage(DateRange dateRange) {
    Collection<Event> events =
      eventsService.getEventsBetween(twinName, dateRange.getStartDate(), dateRange.getEndDate());
    SortedSet<MaterialDTO> materialsUsed = new TreeSet<>(Collections.reverseOrder());

    Stream<Event> filteredEvents = filterEvents(events, this::materialWasCut);
    groupAndCount(filteredEvents, event -> event.getParams().getMaterial())
      .forEach((name, timesUsed) -> materialsUsed.add(new MaterialDTO(name, timesUsed)));

    return materialsUsed;
  }

  public Collection<OptimizationDTO> calculateOptimizationsProcessed(DateRange dateRange) {
    Collection<Event> events =
      eventsService.getEventsBetween(twinName, dateRange.getStartDate(), dateRange.getEndDate());
    SortedSet<OptimizationDTO> optimizationsProcessed = new TreeSet<>(Collections.reverseOrder());

    Stream<Event> filteredEvents = filterEvents(events, this::materialWasCut);
    groupAndCount(filteredEvents,
                  event -> Pair.of(event.getParams().getOptimizationName(), event.getParams().getMaterial()))
      .forEach((key, piecesProcessed) -> optimizationsProcessed
        .add(new OptimizationDTO(key.getFirst(), key.getSecond(), piecesProcessed)));

    return optimizationsProcessed;
  }

  public ToolsDTO calculateToolsInfo(DateRange dateRange) {
    Collection<Event> events =
      eventsService.getEventsBetween(twinName, dateRange.getStartDate(), dateRange.getEndDate());
    ToolsDTO toolsInfo = new ToolsDTO();

    Stream<Event> filteredEvents = filterEvents(events, event -> eventFinalizesProcess(event, ProcessName.CUT));
    getLastEvent(filteredEvents).ifPresent(
      event -> {
        toolsInfo.setToolDistanceCovered(event.getParams().getDistanceCovered());
        toolsInfo.setToolAngle(event.getParams().getToolAngle());
      });

    filteredEvents = filterEvents(events, event -> eventFinalizesProcess(event, ProcessName.LOWE));
    getLastEvent(filteredEvents).ifPresent(event -> toolsInfo.setWheelDiameter(event.getParams().getWheelDiameter()));

    return toolsInfo;
  }

  public Collection<ErrorDTO> calculateErrorsProduced(DateRange dateRange) {
    Collection<Event> events =
      eventsService.getEventsBetween(twinName, dateRange.getStartDate(), dateRange.getEndDate());
    SortedSet<ErrorDTO> errorsProduced = new TreeSet<>(Collections.reverseOrder());

    Stream<Event> filteredEvents = filterEvents(events, event -> event.typeIs(EventType.ERROR));
    groupAndCount(filteredEvents, Event::getErrorName)
      .forEach((cause, timesOccurred) -> errorsProduced.add(new ErrorDTO(cause, timesOccurred)));

    return errorsProduced;
  }
}

package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.CutResultDTO;
import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.dtos.statistics.ErrorDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.MaterialDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.OptimizationDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.ToolsDTO;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class EventsStatistics {
  @NonNull
  private final String twinName;
  @NonNull
  private final EventsService eventsService;

  public EventsStatistics(String twinName, EventsService eventsService) {
    this.twinName = twinName;
    this.eventsService = eventsService;
  }

  private Stream<CutResultDTO> getCutResults(Collection<Event> events) {
    return events.stream().map(Event::getCutResult).flatMap(Optional::stream);
  }

  public Collection<MaterialDTO> calculateMaterialsUsage(DateRange dateRange) {
    SortedSet<MaterialDTO> materialsUsed = new TreeSet<>(Collections.reverseOrder());

    if (dateRange != null) {
      Collection<Event> events = dateRange.getEventsInside(twinName, eventsService);

      getCutResults(events)
        .collect(groupingBy(CutResultDTO::getMaterial, Collectors.counting()))
        .forEach((name, timesUsed) -> materialsUsed.add(new MaterialDTO(name, timesUsed)));
    }

    return materialsUsed;
  }

  public Collection<OptimizationDTO> calculateOptimizationsProcessed(DateRange dateRange) {
    SortedSet<OptimizationDTO> optimizationsProcessed = new TreeSet<>(Collections.reverseOrder());

    if (dateRange != null) {
      Collection<Event> events = dateRange.getEventsInside(twinName, eventsService);

      getCutResults(events)
        .collect(groupingBy(cutResult -> cutResult, Collectors.counting()))
        .forEach((cutResult, piecesProcessed) ->
                   optimizationsProcessed.add(new OptimizationDTO(cutResult.getOptimization(), cutResult.getMaterial(),
                                                                  piecesProcessed)));
    }

    return optimizationsProcessed;
  }

  public ToolsDTO calculateToolsInfo(DateRange dateRange) {
    ToolsDTO toolsInfo = new ToolsDTO();

    if (dateRange != null) {
      Collection<Event> events = dateRange.getEventsInside(twinName, eventsService);

      events.stream().map(event -> event.getToolsInfoAfter(ProcessName.CUT))
            .flatMap(Optional::stream).reduce((first, last) -> last)
            .ifPresent(
              toolsDTO -> {
                toolsInfo.setToolDistanceCovered(toolsDTO.getToolDistanceCovered());
                toolsInfo.setToolAngle(toolsDTO.getToolAngle());
              });

      events.stream().map(event -> event.getToolsInfoAfter(ProcessName.LOWE))
            .flatMap(Optional::stream).reduce((first, last) -> last)
            .ifPresent(toolsDTO -> toolsInfo.setWheelDiameter(toolsDTO.getWheelDiameter()));
    }

    return toolsInfo;
  }

  public Collection<ErrorDTO> calculateErrorsProduced(DateRange dateRange) {
    SortedSet<ErrorDTO> errorsProduced = new TreeSet<>(Collections.reverseOrder());

    if (dateRange != null) {
      Collection<Event> events = dateRange.getEventsInside(twinName, eventsService);

      events.stream().filter(event -> (event.getErrorName() != null))
            .collect(groupingBy(Event::getErrorName, Collectors.counting()))
            .forEach((cause, timesOccurred) -> errorsProduced.add(new ErrorDTO(cause, timesOccurred)));
    }

    return errorsProduced;
  }
}

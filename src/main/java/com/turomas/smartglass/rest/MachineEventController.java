package com.turomas.smartglass.rest;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.services.MachineEventService;
import com.turomas.smartglass.events.services.exceptions.MachineEventNotFound;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.SortedSet;

@RestController
@RequestMapping("/events")
public class MachineEventController {
  private final MachineEventService machineEventService;

  public MachineEventController(MachineEventService machineEventService) {
    this.machineEventService = machineEventService;
  }

  @GetMapping("/{machineName}")
  public SortedSet<MachineEvent> getMachineEvents(String machineName) {
    return machineEventService.getMachineEvents(machineName);
  }

  @GetMapping("/search")
  public SortedSet<MachineEvent> getMachineEvents(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineEventService.searchBy(machineName, startDate, endDate);
  }
}

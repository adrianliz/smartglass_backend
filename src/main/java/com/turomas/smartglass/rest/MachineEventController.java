package com.turomas.smartglass.rest;

import com.turomas.smartglass.domain.MachineEvent;
import com.turomas.smartglass.services.MachineEventService;
import com.turomas.smartglass.services.exceptions.MachineEventNotFound;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class MachineEventController {
  private final MachineEventService machineEventService;

  public MachineEventController(MachineEventService machineEventService) {
    this.machineEventService = machineEventService;
  }

  @GetMapping("")
  public List<MachineEvent> getMachineEvents() {
    return machineEventService.getMachineEvents();
  }

  @GetMapping("/{eventId}")
  public MachineEvent getMachineEvent(@PathVariable String eventId) throws MachineEventNotFound {
    return machineEventService.getMachineEvent(eventId);
  }

  @GetMapping("/search")
  public List<MachineEvent> getMachineEvents(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineEventService.searchBy(machineName, startDate, endDate);
  }
}

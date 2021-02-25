package com.turomas.smartglass.rest;

import com.turomas.smartglass.machineTwin.domain.RatioDTO;
import com.turomas.smartglass.machineTwin.services.MachineTwinService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/machines")
public class MachineTwinController {
  private final MachineTwinService machineTwinService;

  public MachineTwinController(MachineTwinService machineTwinService) {
    this.machineTwinService = machineTwinService;
  }

  @GetMapping("/availability")
  public RatioDTO getAvailability(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineTwinService.getAvailability(machineName, startDate, endDate);
  }
}

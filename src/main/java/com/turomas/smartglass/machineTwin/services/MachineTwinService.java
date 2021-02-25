package com.turomas.smartglass.machineTwin.services;

import com.turomas.smartglass.machineTwin.domain.RatioDTO;

import java.time.LocalDateTime;

public interface MachineTwinService {
  RatioDTO getAvailability(
      String machineTwinName, LocalDateTime startDate, LocalDateTime endDate);
}

package com.turomas.smartglass.machineTwin.services;

import com.turomas.smartglass.machineTwin.domain.RatioDTO;
import com.turomas.smartglass.machineTwin.domain.RatioType;
import com.turomas.smartglass.machineTwin.domain.exceptions.InvalidRatio;
import com.turomas.smartglass.machineTwin.services.exceptions.MachineTwinNotFound;

import java.time.LocalDateTime;

public interface MachineTwinService {
  RatioDTO getRatio(
      String machineTwinName, RatioType ratio, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidRatio;
}

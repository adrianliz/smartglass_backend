package com.turomas.smartglass.machineTwin.services;

import com.turomas.smartglass.machineTwin.domain.MaterialDTO;
import com.turomas.smartglass.machineTwin.domain.OptimizationDTO;
import com.turomas.smartglass.machineTwin.domain.RatioDTO;
import com.turomas.smartglass.machineTwin.domain.exceptions.InvalidPeriod;
import com.turomas.smartglass.machineTwin.services.exceptions.MachineTwinNotFound;

import java.time.LocalDateTime;
import java.util.List;

public interface MachineTwinService {
  List<RatioDTO> getRatios(String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  List<MaterialDTO> getMostUsedMaterials(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  List<OptimizationDTO> getOptimizationsHistory(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;
}

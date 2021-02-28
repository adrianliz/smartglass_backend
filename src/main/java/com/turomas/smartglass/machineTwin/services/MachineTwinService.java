package com.turomas.smartglass.machineTwin.services;

import com.turomas.smartglass.machineEvent.domain.CuttingMaterial;
import com.turomas.smartglass.machineEvent.domain.Optimization;
import com.turomas.smartglass.machineTwin.domain.RatioDTO;
import com.turomas.smartglass.machineTwin.domain.exceptions.InvalidPeriod;
import com.turomas.smartglass.machineTwin.services.exceptions.MachineTwinNotFound;

import java.time.LocalDateTime;
import java.util.List;

public interface MachineTwinService {
  List<RatioDTO> getRatios(String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  List<CuttingMaterial> getMostUsedMaterials(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  List<Optimization> getOptimizationsHistory(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;
}

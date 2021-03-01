package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.domain.exceptions.InvalidPeriod;
import com.turomas.smartglass.twins.services.exceptions.MachineTwinNotFound;

import java.time.LocalDateTime;
import java.util.List;

public interface MachineTwinService {
  List<RatioDTO> getRatios(String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  List<MaterialDTO> getMostUsedMaterials(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  WorkingStatisticsDTO getWorkingStatistics(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  List<OptimizationDTO> getOptimizationsHistory(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  ToolInfoDTO getToolInfo(String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  WheelInfoDTO getWheelInfo(String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;

  List<BreakdownDTO> getBreakdownsOccurred(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod;
}

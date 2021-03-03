package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.services.exceptions.MachineTwinNotFound;

import java.util.List;

public interface MachineTwinService {
  List<RatioDTO> getRatios(String machineName, Period period) throws MachineTwinNotFound;

  List<MaterialDTO> getMostUsedMaterials(String machineName, Period period)
      throws MachineTwinNotFound;

  WorkingStatisticsDTO getWorkingStatistics(String machineName, Period period)
      throws MachineTwinNotFound;

  List<OptimizationDTO> getOptimizationsHistory(String machineName, Period period)
      throws MachineTwinNotFound;

  ToolInfoDTO getToolInfo(String machineName, Period period) throws MachineTwinNotFound;

  WheelInfoDTO getWheelInfo(String machineName, Period period) throws MachineTwinNotFound;

  List<BreakdownDTO> getBreakdownsOccurred(String machineName, Period period)
      throws MachineTwinNotFound;
}

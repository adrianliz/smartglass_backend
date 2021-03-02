package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.PeriodType;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.services.exceptions.MachineTwinNotFound;

import java.util.List;

public interface MachineTwinService {
  List<RatioDTO> getRatios(String machineName, PeriodType periodType) throws MachineTwinNotFound;

  List<MaterialDTO> getMostUsedMaterials(String machineName, PeriodType periodType)
      throws MachineTwinNotFound;

  WorkingStatisticsDTO getWorkingStatistics(String machineName, PeriodType periodType)
      throws MachineTwinNotFound;

  List<OptimizationDTO> getOptimizationsHistory(String machineName, PeriodType periodType)
      throws MachineTwinNotFound;

  ToolInfoDTO getToolInfo(String machineName, PeriodType periodType) throws MachineTwinNotFound;

  WheelInfoDTO getWheelInfo(String machineName, PeriodType periodType) throws MachineTwinNotFound;

  List<BreakdownDTO> getBreakdownsOccurred(String machineName, PeriodType periodType)
      throws MachineTwinNotFound;
}

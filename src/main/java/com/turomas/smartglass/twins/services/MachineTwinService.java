package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.StateType;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.services.exceptions.MachineTwinNotFound;

import java.util.Collection;

public interface MachineTwinService {
  StateType getState(String machineName) throws MachineTwinNotFound;

  Collection<RatioDTO> getRatios(String machineName, Period period) throws MachineTwinNotFound;

  Collection<MaterialDTO> getMostUsedMaterials(String machineName, Period period)
      throws MachineTwinNotFound;

  WorkingHoursDTO getWorkingHours(String machineName, Period period) throws MachineTwinNotFound;

  ToolsInfoDTO getToolsInfo(String machineName, Period period) throws MachineTwinNotFound;

  ProcessesInfoDTO getProcessesInfo(String machineName, Period period) throws MachineTwinNotFound;

  Collection<BreakdownDTO> getBreakdownsOccurred(String machineName, Period period)
      throws MachineTwinNotFound;
}

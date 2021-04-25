package com.turomas.smartglass.twins.domain.dtos.twins;

import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TwinInfoDTO {
  private final String twinName;
  private final String machineSeries;
  private final String machineModel;
  private final TwinStateType currentState;

  public TwinInfoDTO(String twinName, TwinStateType currentState) {
    this.twinName = twinName;
    this.currentState = currentState;
    machineSeries = "";
    machineModel = "";
  }
}

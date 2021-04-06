package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ToolsDTO {
  private long toolDistanceCovered; // 0.1mm
  private int toolAngle; // mm
  private int wheelDiameter; // 0.1mm
}

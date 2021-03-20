package com.turomas.smartglass.twins.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToolsInfoDTO {
  private final long toolDistanceCovered; // 0.1mm
  private final int toolAngle; // mm
  private final int wheelDiameter; // mm
}

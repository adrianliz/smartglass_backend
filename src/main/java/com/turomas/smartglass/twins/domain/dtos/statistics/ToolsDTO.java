package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ToolsDTO {
  private long toolDistanceCovered; // 0.1mm
  private int toolAngle; // mm
  private int wheelDiameter; // 0.1mm
}

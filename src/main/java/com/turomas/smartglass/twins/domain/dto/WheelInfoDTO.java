package com.turomas.smartglass.twins.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WheelInfoDTO {
  private final int diameter; // mm
  private final long distanceCovered; // 0.1mm
}

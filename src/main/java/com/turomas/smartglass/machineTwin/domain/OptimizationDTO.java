package com.turomas.smartglass.machineTwin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptimizationDTO {
  private final String name;
  private final String processName;
  private final int materialsProcessed;
}

package com.turomas.smartglass.machineEvent.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CuttingMaterial {
  private final String name;
  private final int usedTimes;
}

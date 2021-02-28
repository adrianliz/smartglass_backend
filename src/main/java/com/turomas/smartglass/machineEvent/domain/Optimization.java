package com.turomas.smartglass.machineEvent.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Optimization {
  private final String name;
  private final String processName;
  private final int materialsProcessed;
}

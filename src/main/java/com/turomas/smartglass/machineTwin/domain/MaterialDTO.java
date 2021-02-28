package com.turomas.smartglass.machineTwin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MaterialDTO {
  private final String name;
  private final int usedTimes;
}

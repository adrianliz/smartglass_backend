package com.turomas.smartglass.twins.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MaterialDTO implements Comparable<MaterialDTO> {
  private final String name;
  private final int usedTimes;

  @Override
  public int compareTo(MaterialDTO material) {
    if (usedTimes > material.usedTimes) {
      return 1;
    } else if (usedTimes < material.usedTimes) {
      return -1;
    }
    return 0;
  }
}

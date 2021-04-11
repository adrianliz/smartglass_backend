package com.turomas.smartglass.events.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CutResultDTO {
  @NonNull
  private final String optimization;
  @NonNull
  private final String material;
}

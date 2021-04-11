package com.turomas.smartglass.events.domain;

import com.mongodb.lang.NonNull;
import com.turomas.smartglass.twins.domain.dtos.statistics.ToolsDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventParams {
  @EqualsAndHashCode.Include
  @Field("process_name")
  @NonNull
  private ProcessName processName;

  @EqualsAndHashCode.Include
  @Field("optimization_name")
  @NonNull
  private String optimizationName;

  @EqualsAndHashCode.Include
  @Field("cut_plan_id")
  @NonNull
  private int cutPlanId;

  @Field("material")
  private String material;

  @Field("tool_total_distance_covered")
  private long distanceCovered; // 0.1mm

  @Field("tool_angle")
  private int toolAngle; // mm

  @Field("wheel_size")
  private int wheelDiameter; // mm

  public boolean processIs(ProcessName processName) {
    return this.processName.equals(processName);
  }

  public Optional<CutResultDTO> getCutResult() {
    if (processIs(ProcessName.CUT)) {
      if (material != null) {
        return Optional.of(new CutResultDTO(optimizationName, material));
      }
    }

    return Optional.empty();
  }

  public Optional<ToolsDTO> getToolsInfo(ProcessName processName) {
    if (processIs(processName)) {
      return Optional.of(new ToolsDTO(distanceCovered, toolAngle, wheelDiameter));
    }

    return Optional.empty();
  }
}

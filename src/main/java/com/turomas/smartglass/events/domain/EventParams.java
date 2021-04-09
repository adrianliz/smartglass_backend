package com.turomas.smartglass.events.domain;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Getter
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
}

package com.turomas.smartglass.events.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@AllArgsConstructor
@NoArgsConstructor // Necessary for Spring Data MongoDB Repository (TODO use Mongo Template instead of Repository)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventParams {
  @EqualsAndHashCode.Include
  @Field("process_name")
  private ProcessName processName;

  @EqualsAndHashCode.Include
  @Field("optimization_name")
  private String optimizationName;

  @EqualsAndHashCode.Include
  @Field("cut_plan_id")
  private int cutPlanId;

  @Field("material")
  private String material;

  @Field("tool_total_distance_covered")
  private long distanceCovered; // 0.1mm

  @Field("tool_angle")
  private int toolAngle; // mm

  @Field("wheel_size")
  private int wheelDiameter; // mm
}

package com.turomas.smartglass.machineEvent.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EventParams {
  @Field("optimization_name")
  private String optimizationName;

  @Field("process_name")
  private String processName;

  @Field("cut_plan_id")
  private int cutPlanId;
}

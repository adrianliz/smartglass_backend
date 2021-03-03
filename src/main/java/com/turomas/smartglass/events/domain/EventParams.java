package com.turomas.smartglass.events.domain;

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
  @Field("process_name")
  private String processName;

  @Field("optimization_name")
  private String optimizationName;

  @Field("cut_plan_id")
  private int cutPlanId;
}

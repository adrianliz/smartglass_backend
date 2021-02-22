package com.turomas.smartglass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
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

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
	private ProcessName processName;

	@Field("optimization_name")
	private String optimizationName;

	@Field("cut_plan_id")
	private int cutPlanId;

	@Field("material")
	@EqualsAndHashCode.Exclude
	private String material;

	@Field("tool_total_distance_covered")
	@EqualsAndHashCode.Exclude
	private long distanceCovered; // 0.1mm

	@Field("tool_angle")
	@EqualsAndHashCode.Exclude
	private int toolAngle; // mm

	@Field("wheel_size")
	@EqualsAndHashCode.Exclude
	private int wheelDiameter; // mm
}

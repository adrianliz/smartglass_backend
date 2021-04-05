package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.Getter;

import static com.turomas.smartglass.twins.domain.dtos.statistics.TimeDistributionDTO.SECONDS_TO_HOURS_FACTOR;

@Getter
public class MachineUsageDTO {
	private final long workingHours;
	private final long onHours;

	public MachineUsageDTO(long workingSeconds, long machineOnSeconds) {
		workingHours = workingSeconds / SECONDS_TO_HOURS_FACTOR;
		onHours = machineOnSeconds / SECONDS_TO_HOURS_FACTOR;
	}
}

package com.turomas.smartglass.twins.domain.dtos;

import lombok.Getter;

import static com.turomas.smartglass.twins.domain.dtos.TimeDistributionDTO.SECONDS_TO_HOURS_FACTOR;

@Getter
public class MachineUsageDTO {
	private final long workingHours;
	private final long machineOnHours;

	public MachineUsageDTO(long workingSeconds, long machineOnSeconds) {
		workingHours = workingSeconds / SECONDS_TO_HOURS_FACTOR;
		machineOnHours = machineOnSeconds / SECONDS_TO_HOURS_FACTOR;
	}
}

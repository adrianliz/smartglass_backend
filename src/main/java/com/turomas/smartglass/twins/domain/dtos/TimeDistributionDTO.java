package com.turomas.smartglass.twins.domain.dtos;

import lombok.Getter;

@Getter
public class TimeDistributionDTO {
	static final int SECONDS_TO_HOURS_FACTOR = 3600;

	private final long processingGlassHours;
	private final long loadingGlassHours;
	private final long dontWorkingHours;

	public TimeDistributionDTO(long processingGlassSeconds, long loadingGlassSeconds, long dontWorkingSeconds) {
		processingGlassHours = processingGlassSeconds / SECONDS_TO_HOURS_FACTOR;
		loadingGlassHours = loadingGlassSeconds / SECONDS_TO_HOURS_FACTOR;
		dontWorkingHours = dontWorkingSeconds / SECONDS_TO_HOURS_FACTOR;
	}
}

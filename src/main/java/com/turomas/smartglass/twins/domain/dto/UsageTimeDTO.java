package com.turomas.smartglass.twins.domain.dto;

import lombok.Getter;

@Getter
public class UsageTimeDTO {
	private static final int SECONDS_TO_HOURS_FACTOR = 3600;

	private final long processingGlassHours;
	private final long loadingGlassHours;
	private final long standbyHours;
	private final long breakdownHours;
	private final long offHours;

	public UsageTimeDTO(long processingGlassSeconds, long loadingGlassSeconds, long standbySeconds,
	                    long breakdownSeconds, long offSeconds) {

		processingGlassHours = processingGlassSeconds / SECONDS_TO_HOURS_FACTOR;
		loadingGlassHours = loadingGlassSeconds / SECONDS_TO_HOURS_FACTOR;
		standbyHours = standbySeconds / SECONDS_TO_HOURS_FACTOR;
		breakdownHours = breakdownSeconds / SECONDS_TO_HOURS_FACTOR;
		offHours = offSeconds / SECONDS_TO_HOURS_FACTOR;
	}
}

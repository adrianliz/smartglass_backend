package com.turomas.smartglass.twins.domain.exceptions;

import java.time.LocalDateTime;

public class InvalidPeriod extends RuntimeException {
	public InvalidPeriod(LocalDateTime startDate, LocalDateTime endDate) {
		super("End date '" + endDate + "' must be after start date '" + startDate + "'");
	}
}

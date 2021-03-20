package com.turomas.smartglass.twins.domain.exceptions;

import java.time.LocalDateTime;

public class InvalidPeriod extends RuntimeException {
	public InvalidPeriod(LocalDateTime startDate, LocalDateTime endDate) {
		super("End date '" + startDate + "' must be after start date '" + endDate + "'");
	}
}

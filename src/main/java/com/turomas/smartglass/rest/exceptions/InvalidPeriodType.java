package com.turomas.smartglass.rest.exceptions;

import com.turomas.smartglass.twins.domain.Period;

import java.util.Arrays;

public class InvalidPeriodType extends RuntimeException {
	public InvalidPeriodType() {
		super("Period type must be: " + Arrays.toString(Period.values()));
	}
}

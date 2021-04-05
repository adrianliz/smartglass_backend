package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ErrorDTO implements Comparable<ErrorDTO> {
	@EqualsAndHashCode.Include
	private final String cause;
	private final long timesOccurred;

	@Override
	public int compareTo(ErrorDTO breakdown) {
		if (this.equals(breakdown)) return 0;
		if (timesOccurred > breakdown.timesOccurred) {
			return 1;
		}
		return - 1;
	}
}

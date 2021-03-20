package com.turomas.smartglass.twins.domain.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class OptimizationDTO implements Comparable<OptimizationDTO> {
	private final String name;
	private final String material;
	@EqualsAndHashCode.Exclude
	private final long piecesProcessed;

	@Override
	public int compareTo(OptimizationDTO optimization) {
		if (this.equals(optimization)) return 0;
		if (piecesProcessed > optimization.piecesProcessed) {
			return 1;
		}
		return - 1;

	}
}

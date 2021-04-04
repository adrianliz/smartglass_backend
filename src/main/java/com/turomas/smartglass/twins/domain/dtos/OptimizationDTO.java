package com.turomas.smartglass.twins.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OptimizationDTO implements Comparable<OptimizationDTO> {
	@EqualsAndHashCode.Include
	private final String name;
	@EqualsAndHashCode.Include
	private final String material;
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

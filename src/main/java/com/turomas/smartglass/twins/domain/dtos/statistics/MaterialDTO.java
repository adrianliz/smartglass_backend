package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MaterialDTO implements Comparable<MaterialDTO> {
	@EqualsAndHashCode.Include
	private final String name;
	private final long timesUsed;

	@Override
	public int compareTo(MaterialDTO material) {
		if (this.equals(material)) return 0;
		if (timesUsed > material.timesUsed) {
			return 1;
		}
		return - 1;
	}
}

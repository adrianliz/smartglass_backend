package com.turomas.smartglass.twins.domain.dtos.twins;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TwinModelDTO {
	private final String twinName;
	private final String machineSeries;
	private final String machineModel;
}

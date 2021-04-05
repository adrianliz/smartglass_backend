package com.turomas.smartglass.twins.domain.dtos.twins;

import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TwinStateDTO {
	private final TwinStateId twinState;
}

package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TwinOntology {
	private final String twinName;
	private final String machineSeries;
	private final String machineModel;
	private TwinStateId currentState;

	public void updateState(TwinStateId currentState) {
		this.currentState = currentState;
	}
}

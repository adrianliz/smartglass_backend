package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.statesmachine.TwinState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatesService {
	TwinState getLastState(String twinName);

	List<TwinState> getStatesBetween(String twinName, LocalDateTime startDate, LocalDateTime endDate);

	void saveStates(Collection<TwinState> states);
}

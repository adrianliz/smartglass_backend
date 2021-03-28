package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.repositories.StatesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class StatesProxy implements StatesService {
	private final StatesRepository statesRepository;

	public StatesProxy(StatesRepository statesRepository) {
		this.statesRepository = statesRepository;
	}

	@Override
	public TwinState getLastState(String twinName) {
		PageRequest request =
			PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "enterEvent.timestamp"));

		List<TwinState> states = statesRepository.getStates(twinName, request).getContent();
		if (! states.isEmpty()) {
			return states.get(0);
		}

		return null;
	}

	@Override
	public List<TwinState> getStatesBetween(String twinName, LocalDateTime startDate, LocalDateTime endDate) {
		return statesRepository.getStatesBetween(twinName, startDate, endDate);
	}

	@Override
	public void saveStates(Collection<TwinState> states) {
		statesRepository.saveAll(states);
	}
}

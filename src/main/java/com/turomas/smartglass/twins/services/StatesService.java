package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.statesmachine.TwinState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface StatesService {
  Optional<TwinState> getLastState(String twinName);

  Collection<TwinState> getOverlapStates(String twinName, LocalDateTime startDate, LocalDateTime endDate);

  void saveStates(Collection<TwinState> states);
}

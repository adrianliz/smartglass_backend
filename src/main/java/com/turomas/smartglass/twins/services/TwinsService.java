package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.dtos.twins.TwinInfoDTO;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;

import java.util.Collection;

public interface TwinsService {
  Collection<Twin> getTwins();

  Twin getTwin(String twinName) throws TwinNotFound;

  Collection<TwinInfoDTO> getTwinsInfo();

  TwinInfoDTO getTwinInfo(String twinName) throws TwinNotFound;

  void updateLastState(String twinName, StatesService statesService) throws TwinNotFound;

  void updateRatios(String twinName) throws TwinNotFound;
}

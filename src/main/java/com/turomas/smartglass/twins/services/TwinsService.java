package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.dtos.twins.TwinModelDTO;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;

import java.util.Collection;

public interface TwinsService {
  Collection<Twin> getTwins();

  Twin getTwin(String twinName) throws TwinNotFound;

  Collection<TwinModelDTO> getTwinModels();

  TwinModelDTO getTwinModel(String twinName) throws TwinNotFound;

  void updateState(TwinState state) throws TwinNotFound;
}

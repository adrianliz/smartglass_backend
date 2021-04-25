package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.dtos.twins.TwinInfoDTO;
import com.turomas.smartglass.twins.repositories.TwinsRepository;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TwinsProxy implements TwinsService {
  @NonNull
  private final TwinsRepository twinsRepository;

  public TwinsProxy(TwinsRepository twinsRepository) {
    this.twinsRepository = twinsRepository;
  }

  @Override
  public Collection<Twin> getTwins() {
    return twinsRepository.getTwins();
  }

  @Override
  public Twin getTwin(String twinName) throws TwinNotFound {
    return twinsRepository.getTwin(twinName);
  }

  @Override
  public Collection<TwinInfoDTO> getTwinsInfo() {
    return twinsRepository.getTwinsInfo();
  }

  @Override
  public TwinInfoDTO getTwinInfo(String twinName) throws TwinNotFound {
    return twinsRepository.getTwinInfo(twinName);
  }

  @Override
  public void updateLastState(String twinName, StatesService statesService) throws TwinNotFound {
    twinsRepository.updateLastState(twinName, statesService);
  }

  @Override
  public void updateRatios(String twinName) throws TwinNotFound {
    twinsRepository.updateRatios(twinName);
  }
}

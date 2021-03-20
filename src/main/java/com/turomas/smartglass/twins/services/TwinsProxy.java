package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.TwinOntology;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.repositories.TwinsRepository;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class TwinsProxy implements TwinsService {
  private final TwinsRepository twinsRepository;

  public TwinsProxy(TwinsRepository twinsRepository) {
    this.twinsRepository = twinsRepository;
  }

  private Twin findTwin(String twinName) throws TwinNotFound {
    Twin twin = twinsRepository.getTwin(twinName);

    if (twin != null) return twin;
    throw new TwinNotFound(twinName);
  }

  @Override
  public Collection<TwinOntology> getTwins() {
    List<TwinOntology> twins = new ArrayList<>();

    for (Twin twin : twinsRepository.getTwins()) {
      twins.add(twin.getRepresentation());
    }

    return twins;
  }

  public TwinOntology getTwin(String twinName) throws TwinNotFound {
    Twin twin = findTwin(twinName);
    return twin.getRepresentation();
  }

  @Override
  public Collection<RatioDTO> getRatios(String twinName, Period period) throws TwinNotFound {
    Twin twin = findTwin(twinName);
    return twin.getRatios(period.getDateRange());
  }

  @Override
  public Collection<MaterialDTO> getMostUsedMaterials(String twinName, Period period) throws TwinNotFound {
    Twin twin = findTwin(twinName);
    return twin.getMostUsedMaterials(period.getDateRange());
  }

  @Override
  public Collection<OptimizationDTO> getOptimizationsProcessed(String twinName, Period period) throws TwinNotFound {
    Twin twin = findTwin(twinName);
    return twin.getOptimizationsProcessed(period.getDateRange());
  }

  @Override
  public ToolsInfoDTO getToolsInfo(String twinName, Period period) throws TwinNotFound {
    Twin twin = findTwin(twinName);
    return twin.getToolsInfo(period.getDateRange());
  }

  @Override
  public UsageTimeDTO getUsageTime(String twinName, Period period) throws TwinNotFound {
    Twin twin = findTwin(twinName);
    return twin.getUsageTime(period.getDateRange());
  }

  @Override
  public Collection<BreakdownDTO> getBreakdownsOccurred(String twinName, Period period) throws TwinNotFound {
    Twin twin = findTwin(twinName);
    return twin.getBreakdownsOccurred(period.getDateRange());
  }
}

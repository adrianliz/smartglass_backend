package com.turomas.smartglass.twins.repositories;

import com.turomas.smartglass.events.repositories.MachineEventRepository;
import com.turomas.smartglass.twins.domain.MachineTwin;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO replace with ontology model
@Repository
public class OWLMachineTwinRepository implements MachineTwinRepository {
  private final MachineTwin machineTwin;

  public OWLMachineTwinRepository(MachineEventRepository machineEventRepository) {
    machineTwin = new MachineTwin("Turomas1", machineEventRepository);
  }

  @Override
  public List<MachineTwin> getMachineTwins() {
    return null;
  }

  @Override
  public MachineTwin getMachineTwin(String name) {
    return machineTwin;
  }
}

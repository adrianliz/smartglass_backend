package com.turomas.smartglass.twins.repositories;

import com.turomas.smartglass.events.repositories.MachineEventRepository;
import com.turomas.smartglass.twins.domain.MachineTwin;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO replace with ontology model
@Repository
public class OWLMachineTwinRepository implements MachineTwinRepository {
  private MachineTwin machineTwin = null;
  private MachineEventRepository machineEventRepository;

  public OWLMachineTwinRepository(MachineEventRepository machineEventRepository) {
    this.machineEventRepository = machineEventRepository;
  }

  @Override
  public List<MachineTwin> getMachineTwins() {
    return null;
  }

  @Override
  public MachineTwin getMachineTwin(String name) {
    if (machineTwin == null) machineTwin = new MachineTwin("Turomas1", machineEventRepository);

    return machineTwin;
  }
}

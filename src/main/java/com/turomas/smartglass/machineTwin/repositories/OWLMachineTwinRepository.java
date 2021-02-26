package com.turomas.smartglass.machineTwin.repositories;

import com.turomas.smartglass.machineTwin.domain.MachineTwin;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO replace with ontology model
@Repository
public class OWLMachineTwinRepository implements MachineTwinRepository {
  MachineTwin machineTwin = null;

  @Override
  public List<MachineTwin> getMachineTwins() {
    return null;
  }

  @Override
  public MachineTwin getMachineTwin(String name) {
    if (machineTwin == null) machineTwin = new MachineTwin("Turomas1");

    return machineTwin;
  }
}

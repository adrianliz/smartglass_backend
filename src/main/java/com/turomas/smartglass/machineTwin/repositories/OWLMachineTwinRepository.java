package com.turomas.smartglass.machineTwin.repositories;

import com.turomas.smartglass.machineTwin.domain.MachineTwin;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO replace with Ontology model
@Repository
public class OWLMachineTwinRepository implements MachineTwinRepository {
  @Override
  public List<MachineTwin> getMachineTwins() {
    return null;
  }

  @Override
  public MachineTwin getMachineTwin(String name) {
    return new MachineTwin("Turomas1");
  }
}

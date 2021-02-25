package com.turomas.smartglass.machineTwin.repositories;

import com.turomas.smartglass.machineTwin.domain.MachineTwin;

import java.util.List;

public interface MachineTwinRepository {
  List<MachineTwin> getMachineTwins();

  MachineTwin getMachineTwin(String name);
}

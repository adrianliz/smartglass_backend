package com.turomas.smartglass.twins.repositories;

import com.turomas.smartglass.twins.domain.MachineTwin;

import java.util.List;

public interface MachineTwinRepository {
  List<MachineTwin> getMachineTwins();

  MachineTwin getMachineTwin(String name);
}

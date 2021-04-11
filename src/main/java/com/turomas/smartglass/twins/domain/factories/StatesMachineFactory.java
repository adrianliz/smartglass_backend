package com.turomas.smartglass.twins.domain.factories;

import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;

public interface StatesMachineFactory {
  StatesMachine createFor(String twinName);
}

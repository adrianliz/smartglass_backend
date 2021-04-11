package com.turomas.smartglass.twins.domain.statesmachine.guards;

import com.turomas.smartglass.twins.domain.statesmachine.TwinState;

public interface GuardStrategy {
  boolean cutTransition(TwinState currentState);
}

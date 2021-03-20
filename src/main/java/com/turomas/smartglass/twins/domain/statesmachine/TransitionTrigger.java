package com.turomas.smartglass.twins.domain.statesmachine;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class TransitionTrigger<A, B> {
	public final A state;
	public final B event;
}
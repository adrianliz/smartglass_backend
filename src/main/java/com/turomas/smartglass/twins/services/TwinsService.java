package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;

import java.util.Collection;

public interface TwinsService {
	Twin getTwin(String twinName) throws TwinNotFound;

	Collection<Twin> getTwins();
}

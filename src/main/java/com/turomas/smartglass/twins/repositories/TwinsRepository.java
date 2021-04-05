package com.turomas.smartglass.twins.repositories;

import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.domain.dtos.twins.TwinModelDTO;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;

import java.util.List;

public interface TwinsRepository {
	List<Twin> getTwins();

	Twin getTwin(String name) throws TwinNotFound;

	TwinModelDTO getTwinModel(String twinName) throws TwinNotFound;
}

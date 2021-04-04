package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Twin;
import com.turomas.smartglass.twins.repositories.TwinsRepository;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TwinsProxy implements TwinsService {
	private final TwinsRepository twinsRepository;

	public TwinsProxy(TwinsRepository twinsRepository) {
		this.twinsRepository = twinsRepository;
	}

	@Override
	public Twin getTwin(String twinName) throws TwinNotFound {
		return twinsRepository.getTwin(twinName);
	}

	@Override
	public Collection<Twin> getTwins() {
		return twinsRepository.getTwins();
	}
}

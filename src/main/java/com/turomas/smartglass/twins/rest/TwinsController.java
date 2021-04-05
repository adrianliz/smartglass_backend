package com.turomas.smartglass.twins.rest;

import com.turomas.smartglass.twins.domain.dtos.twins.TwinModelDTO;
import com.turomas.smartglass.twins.domain.dtos.twins.TwinStateDTO;
import com.turomas.smartglass.twins.services.TwinsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("{twinName}")
public class TwinsController {
	private final TwinsService twinsService;

	public TwinsController(TwinsService twinsService) {
		this.twinsService = twinsService;
	}

	@GetMapping("/model")
	public TwinModelDTO getTwinModel(@PathVariable String twinName) {
		return twinsService.getTwinModel(twinName);
	}

	@GetMapping("/state")
	public TwinStateDTO getTwinState(@PathVariable String twinName) {
		return twinsService.getTwinState(twinName);
	}
}

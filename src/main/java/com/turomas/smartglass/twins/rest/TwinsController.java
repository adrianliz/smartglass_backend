package com.turomas.smartglass.twins.rest;

import com.turomas.smartglass.twins.domain.dtos.twins.TwinInfoDTO;
import com.turomas.smartglass.twins.services.TwinsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("twins")
public class TwinsController {
  private final TwinsService twinsService;

  public TwinsController(TwinsService twinsService) {
    this.twinsService = twinsService;
  }

  @GetMapping("info")
  public Collection<TwinInfoDTO> getTwinsInfo() {
    return twinsService.getTwinsInfo();
  }

  @GetMapping("{twinName}/info")
  public TwinInfoDTO getTwinInfo(@PathVariable String twinName) {
    return twinsService.getTwinInfo(twinName);
  }
}

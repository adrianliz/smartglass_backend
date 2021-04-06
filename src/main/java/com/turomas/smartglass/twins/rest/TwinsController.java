package com.turomas.smartglass.twins.rest;

import com.turomas.smartglass.twins.domain.dtos.twins.TwinModelDTO;
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

  @GetMapping("models")
  public Collection<TwinModelDTO> getTwinModels() {
    return twinsService.getTwinModels();
  }

  @GetMapping("{twinName}/model")
  public TwinModelDTO getTwinModel(@PathVariable String twinName) {
    return twinsService.getTwinModel(twinName);
  }
}

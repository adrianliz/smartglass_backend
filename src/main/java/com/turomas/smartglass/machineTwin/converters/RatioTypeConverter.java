package com.turomas.smartglass.machineTwin.converters;

import com.turomas.smartglass.machineTwin.domain.RatioType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RatioTypeConverter implements Converter<String, RatioType> {
  @Override
  public RatioType convert(String source) {
    for (RatioType eventType : RatioType.values()) {
      if (eventType.name().equals(source.toUpperCase())) {
        return eventType;
      }
    }

    return RatioType.UNDEFINED;
  }
}

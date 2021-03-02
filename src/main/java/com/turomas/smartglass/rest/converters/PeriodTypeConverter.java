package com.turomas.smartglass.rest.converters;

import com.turomas.smartglass.rest.exceptions.InvalidPeriodType;
import com.turomas.smartglass.twins.domain.PeriodType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PeriodTypeConverter implements Converter<String, PeriodType> {
  @Override
  public PeriodType convert(String source) throws InvalidPeriodType {
    for (PeriodType periodType : PeriodType.values()) {
      if (periodType.name().equals(source)) {
        return periodType;
      }
    }

    throw new InvalidPeriodType();
  }
}

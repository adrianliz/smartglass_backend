package com.turomas.smartglass.twins.rest.converters;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.rest.exceptions.InvalidPeriod;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PeriodTypeConverter implements Converter<String, Period> {
  @Override
  public Period convert(String source) throws InvalidPeriod {
    for (Period period : Period.values()) {
      if (period.name().equals(source.toUpperCase())) {
        return period;
      }
    }

    throw new InvalidPeriod();
  }
}

package com.turomas.smartglass.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateConverter implements Converter<LocalDateTime, Date> {
  @Override
  public Date convert(LocalDateTime source) {
    return Date.from(source.toInstant(ZoneOffset.UTC));
  }
}

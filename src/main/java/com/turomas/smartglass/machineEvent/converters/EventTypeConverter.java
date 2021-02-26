package com.turomas.smartglass.machineEvent.converters;

import com.turomas.smartglass.machineEvent.domain.EventType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class EventTypeConverter implements Converter<String, EventType> {
  @Override
  public EventType convert(String source) {
    for (EventType eventType : EventType.values()) {
      if (eventType.name().equals(source.toUpperCase())) {
        return eventType;
      }
    }

    return EventType.UNDEFINED;
  }
}

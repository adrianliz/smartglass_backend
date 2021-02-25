package com.turomas.smartglass.config;

import com.turomas.smartglass.machineEvent.converters.DateConverter;
import com.turomas.smartglass.machineEvent.converters.EventTypeConverter;
import com.turomas.smartglass.machineEvent.converters.LocalDateTimeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoDBConfig {
  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(
        List.of(new EventTypeConverter(), new LocalDateTimeConverter(), new DateConverter()));
  }
}

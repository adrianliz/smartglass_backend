package com.turomas.smartglass.config;

import com.turomas.smartglass.events.converters.DateConverter;
import com.turomas.smartglass.events.converters.EventTypeConverter;
import com.turomas.smartglass.events.converters.LocalDateTimeConverter;
import com.turomas.smartglass.events.converters.ProcessTypeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoDBConfig {
  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(
      List.of(new EventTypeConverter(), new ProcessTypeConverter(), new LocalDateTimeConverter(), new DateConverter()));
  }
}

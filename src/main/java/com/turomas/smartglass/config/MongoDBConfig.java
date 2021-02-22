package com.turomas.smartglass.config;

import com.turomas.smartglass.converters.EventTypeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Collections;

@Configuration
public class MongoDBConfig {
  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(Collections.singletonList(new EventTypeConverter()));
  }
}

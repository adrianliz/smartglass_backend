package com.turomas.smartglass.twins.rest.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class APIException {
  @NonNull
  private final HttpStatus status;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private final LocalDateTime date = LocalDateTime.now();

  @NonNull
  private final String message;
}

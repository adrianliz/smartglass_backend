package com.turomas.smartglass.rest.exceptions;

import com.turomas.smartglass.events.services.exceptions.MachineEventNotFound;
import com.turomas.smartglass.twins.domain.exceptions.InvalidPeriod;
import com.turomas.smartglass.twins.services.exceptions.MachineTwinNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class APIExceptionHandler {
  @ExceptionHandler(MachineEventNotFound.class)
  public ResponseEntity<APIException> handleMachineEventNotFound(MachineEventNotFound ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new APIException(HttpStatus.NOT_FOUND, ex.getMessage()));
  }

  @ExceptionHandler(MachineTwinNotFound.class)
  public ResponseEntity<APIException> handleMachineTwinNotFound(MachineTwinNotFound ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new APIException(HttpStatus.NOT_FOUND, ex.getMessage()));
  }

  @ExceptionHandler(InvalidPeriod.class)
  public ResponseEntity<APIException> handleInvalidPeriod(InvalidPeriod ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new APIException(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }
}

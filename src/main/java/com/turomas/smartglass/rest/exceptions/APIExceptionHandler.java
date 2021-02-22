package com.turomas.smartglass.rest.exceptions;

import com.turomas.smartglass.services.exceptions.MachineEventNotFound;
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
}

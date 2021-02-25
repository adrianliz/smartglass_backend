package com.turomas.smartglass.rest.exceptions;

import com.turomas.smartglass.machineEvent.services.exceptions.MachineEventNotFound;
import com.turomas.smartglass.machineTwin.domain.exceptions.PeriodNotValidException;
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

  @ExceptionHandler(PeriodNotValidException.class)
  public ResponseEntity<APIException> handleMachineEventNotFound(PeriodNotValidException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(new APIException(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }
}

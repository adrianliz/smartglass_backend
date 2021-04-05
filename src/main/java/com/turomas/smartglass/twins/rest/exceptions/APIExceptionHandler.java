package com.turomas.smartglass.twins.rest.exceptions;

import com.turomas.smartglass.twins.domain.exceptions.InvalidDateRange;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class APIExceptionHandler {
	@ExceptionHandler(TwinNotFound.class)
	public ResponseEntity<APIException> handleMachineTwinNotFound(TwinNotFound ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                     .body(new APIException(HttpStatus.NOT_FOUND, ex.getMessage()));
	}

	@ExceptionHandler(InvalidPeriod.class)
	public ResponseEntity<APIException> handleInvalidPeriodType(InvalidPeriod ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                     .body(new APIException(HttpStatus.BAD_REQUEST, ex.getMessage()));
	}

	@ExceptionHandler(InvalidDateRange.class)
	public ResponseEntity<APIException> handleInvalidPeriodType(InvalidDateRange ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                     .body(new APIException(HttpStatus.BAD_REQUEST, ex.getMessage()));
	}
}

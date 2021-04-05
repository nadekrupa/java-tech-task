package com.rezdy.lunch.exception.handler;

import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rezdy.lunch.exception.InvalidInputException;
import com.rezdy.lunch.exception.RecipeNotFoundException;
import com.rezdy.lunch.response.ErrorResponse;

@RestControllerAdvice
public class LunchExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(DateTimeParseException exception) {
        return new ResponseEntity<>(new ErrorResponse().setErrorMessage(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException exception) {
        return new ResponseEntity<>(new ErrorResponse().setErrorMessage(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ErrorResponse> RecipeNotFoundException(RecipeNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse().setErrorMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}

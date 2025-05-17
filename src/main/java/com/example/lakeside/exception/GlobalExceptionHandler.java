package com.example.lakeside.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(UserNotFoundException.class)
 public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex){
	  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
 
  
  @ExceptionHandler(Exception.class)
 public  ResponseEntity<String> handleAllOtherExceptions(Exception ex) {
	  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error ocurred"+ex.getMessage());
	  
	  
  }
  
  
  
	

}

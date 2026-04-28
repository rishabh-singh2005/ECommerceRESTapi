package com.example.ecom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.ecom.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleProductNotFound(ProductNotFoundException ex){
		
		ApiResponse<String> response = new ApiResponse<>(ex.getMessage(),null);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleOrderNotFound(OrderNotFoundException ex){
		
		ApiResponse<String> response = new ApiResponse<>(ex.getMessage(),null);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

}

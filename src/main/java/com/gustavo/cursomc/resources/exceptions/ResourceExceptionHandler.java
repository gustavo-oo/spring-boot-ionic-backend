package com.gustavo.cursomc.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gustavo.cursomc.services.exceptions.DataIntegrityException;
import com.gustavo.cursomc.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound( //Recebe como parâmetro um objectNotFound
											ObjectNotFoundException exception, 
											HttpServletRequest request){
		
		StandardError error = new StandardError(	
									HttpStatus.NOT_FOUND.value(), 
									exception.getMessage(), 
									System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity( //Recebe como parâmetro um objectNotFound
											DataIntegrityException exception, 
											HttpServletRequest request){
		
		StandardError error = new StandardError(
									HttpStatus.BAD_REQUEST.value(), 
									exception.getMessage(), 
									System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}

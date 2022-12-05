package com.example.productapi.controller.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.productapi.service.exceptions.AuthenticationException;
import com.example.productapi.service.exceptions.DatabaseException;
import com.example.productapi.service.exceptions.ResourceNotFoundException;
import com.example.productapi.service.exceptions.ValidationException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> dataBaseException(DatabaseException e, HttpServletRequest request) {
		String error = "Data base error";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<StandardError> validationException(DatabaseException e, HttpServletRequest request) {
		String error = "Não encontrado";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<StandardError> authenticationException(AuthenticationException e, HttpServletRequest request) {
		String error = "Acesso não autorizado";
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	/*@ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardError> handleAuthenticationException(AuthenticationException authenticationException) {
        var details = new StandardError();
        details.setStatus(HttpStatus.UNAUTHORIZED.value());
        details.setMessage(authenticationException.getMessage());
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }*/
}

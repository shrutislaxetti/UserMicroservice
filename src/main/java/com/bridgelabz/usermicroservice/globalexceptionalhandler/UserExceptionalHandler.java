package com.bridgelabz.usermicroservice.globalexceptionalhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.usermicroservice.exceptions.LoginException;
import com.bridgelabz.usermicroservice.exceptions.RegistrationException;
import com.bridgelabz.usermicroservice.exceptions.UserNotActivatedException;
import com.bridgelabz.usermicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.usermicroservice.model.Response;


@ControllerAdvice
public class UserExceptionalHandler {
	
	private final Logger logger = LoggerFactory.getLogger(UserExceptionalHandler.class);

	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<Response> handleRegistrationException(RegistrationException exception) {
		logger.info("Error occured while registration " + exception.getMessage(), exception);

		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LoginException.class)
	public ResponseEntity<Response> handleLoginException(LoginException exception) {
		logger.info("Error occured while login " + exception.getMessage(), exception);

		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(2);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Response> handleUserNotFoundException(UserNotFoundException exception) {
		logger.info("Error occured while login " + exception.getMessage(), exception);

		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(3);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserNotActivatedException.class)
	public ResponseEntity<Response> handleUserNotActivatedException(UserNotActivatedException exception) {
		logger.info("Error occured while login " + exception.getMessage(), exception);

		Response response = new Response();
		response.setMessage(exception.getMessage());
		response.setStatus(4);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleException(Exception exception) {
		logger.error("Error occured for " + exception.getMessage(), exception);

		Response response = new Response();
		response.setMessage("Something went wrong");
		response.setStatus(0);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
}
}
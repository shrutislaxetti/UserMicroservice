package com.bridgelabz.usermicroservice.exceptions;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = -4379797511709305675L;

	public UserNotFoundException(String message) {
		super(message);

	}

}
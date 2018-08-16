package com.bridgelabz.usermicroservice.controller;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.usermicroservice.exceptions.LoginException;
import com.bridgelabz.usermicroservice.exceptions.RegistrationException;
import com.bridgelabz.usermicroservice.exceptions.UserNotActivatedException;
import com.bridgelabz.usermicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.usermicroservice.model.LoginDTO;
import com.bridgelabz.usermicroservice.model.RegistrationDTO;
import com.bridgelabz.usermicroservice.model.ResetPassword;
import com.bridgelabz.usermicroservice.model.Response;
import com.bridgelabz.usermicroservice.service.UserService;


@RestController
@RequestMapping(value="/user")
public class UserController {

	@Autowired
	private UserService userService;
	

	/**
	 * TO register a user
	 * @param registration
	 * @return ResponseDTO
	 * @throws RegistrationException
	 * @throws MessagingException
	 */
	@PostMapping(value = "/register")
	public ResponseEntity<Response> register(@RequestBody RegistrationDTO registration)
			throws RegistrationException, MessagingException {

		userService.register(registration);
		Response responseDTO = new Response();
		responseDTO.setMessage("Registration Successfull!!");
		responseDTO.setStatus(201);
		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

	}
	
	/**
	 * For User login
	 * @param login
	 * @param resp
	 * @return ResponseDTO 
	 * @throws LoginException
	 * @throws UserNotFoundException
	 * @throws UserNotActivatedException
	 * @throws javax.security.auth.login.LoginException 
	 */
	@PostMapping(value = "/login")
	public ResponseEntity<Response> login(@RequestBody LoginDTO login, HttpServletResponse resp)
			throws LoginException{

		String token = userService.login(login);
		resp.setHeader("token", token);
		
		Response response = new Response();
		response.setMessage("Login Successful");
		response.setStatus(20);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * To activate account after registration
	 * @param token
	 * @return ResponseDTO
	 * @throws LoginException
	 * @throws UserNotFoundException 
	 * @throws MessagingException 
	 */
	@GetMapping(value = "/activateaccount")
	public ResponseEntity<Response> activateaccount(@RequestHeader("token") String token) throws LoginException, MessagingException, UserNotFoundException {

		userService.activateAccount(token);

		Response response = new Response();
		response.setMessage("Account activated successfully");
		response.setStatus(12);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * To send reset password link 
	 * @param email
	 * @return ResponseDTO
	 * @throws MessagingException
	 * @throws UserNotFoundException
	 */
	@GetMapping(value = "/resetPasswordLink")
	public ResponseEntity<Response> resetPasswordLink(@RequestParam("email") String email) throws MessagingException, UserNotFoundException {

		userService.activateAccount(email);

		Response response = new Response();
		response.setMessage("Successfully sent mail");
		response.setStatus(31);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * To reset password for the user
	 * @param token
	 * @param resetPassword
	 * @return ResponseDTO
	 * @throws LoginException
	 * @throws UserNotFoundException
	 * @throws UserNotActivatedException 
	 * @throws javax.security.auth.login.LoginException 
	 * @throws javax.security.auth.login.LoginException 
	 */
	@PutMapping(value = "/resetPassword")
	public ResponseEntity<Response> resetPassword(@RequestHeader("token") String token,
			@RequestBody ResetPassword resetPassword) throws  UserNotFoundException, UserNotActivatedException, LoginException, javax.security.auth.login.LoginException {
		
		userService.setNewPssword(resetPassword, token);

		Response response = new Response();
		response.setMessage("Password reset successful");
		response.setStatus(32);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}
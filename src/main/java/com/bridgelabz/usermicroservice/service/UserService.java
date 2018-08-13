package com.bridgelabz.usermicroservice.service;

import javax.mail.MessagingException;

import com.bridgelabz.usermicroservice.exceptions.LoginException;
import com.bridgelabz.usermicroservice.exceptions.RegistrationException;
import com.bridgelabz.usermicroservice.exceptions.UserNotActivatedException;
import com.bridgelabz.usermicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.usermicroservice.model.LoginDTO;
import com.bridgelabz.usermicroservice.model.RegistrationDTO;
import com.bridgelabz.usermicroservice.model.ResetPassword;

public interface UserService {

	public String login(LoginDTO loginDTO) throws LoginException, com.bridgelabz.usermicroservice.exceptions.LoginException;

	public void register(RegistrationDTO registrationdto) throws RegistrationException, MessagingException;

	public void activateAccount(String token) throws MessagingException, UserNotFoundException;

	public void forgotPassword(String email) throws MessagingException, UserNotActivatedException;

	public void setNewPssword(ResetPassword resetPassword, String token)
			throws UserNotActivatedException, UserNotFoundException, LoginException;

}
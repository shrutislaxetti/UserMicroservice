package com.bridgelabz.usermicroservice.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import com.bridgelabz.usermicroservice.exceptions.LoginException;
import com.bridgelabz.usermicroservice.exceptions.RegistrationException;
import com.bridgelabz.usermicroservice.model.LoginDTO;
import com.bridgelabz.usermicroservice.model.RegistrationDTO;
import com.bridgelabz.usermicroservice.model.ResetPassword;
import com.bridgelabz.usermicroservice.model.User;

public class Utility {
	
	@Value("${key}")
	private String key;
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");

	private static final Pattern PASSWORD_PATTERN = Pattern
			.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})");

	private static final Pattern CONTACT_PATTERN = Pattern.compile("^[0-9]{10}$");

	private Utility() {

	}

	public static boolean validateEmail(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static boolean validatePassword(String password) {
		Matcher matcher = PASSWORD_PATTERN.matcher(password);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static boolean validatePhoneNumber(String phoneNumber) {
		Matcher matcher = CONTACT_PATTERN.matcher(phoneNumber);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static String getRequestUrl(HttpServletRequest request) throws MalformedURLException {
		URL url = new URL(request.getRequestURL().toString());
		return url.getProtocol() + "://" + url.getPort() + request.getContextPath();
	}

	public static void validateUserForRegistration(RegistrationDTO registrationDto) throws RegistrationException {
		if (registrationDto.getUserName() == null || registrationDto.getUserName().length() < 3) {
			throw new RegistrationException("Name should have atleast 3 characters");
		}

		if (registrationDto.getPhoneNumber() == null || (!validatePhoneNumber(registrationDto.getPhoneNumber()))) {
			throw new RegistrationException("Contact number should be exactly 10 digit numbers");
		}

		if (registrationDto.getPassword() == null || (!validatePassword(registrationDto.getPassword()))) {
			throw new RegistrationException("Password should have atleast one uppercase character, "
					+ "atlest one lowercase character, " + "one special character, " + "and atleast one number");
		}

		if (registrationDto.getEmailId() == null || registrationDto.getEmailId().length() == 0) {
			throw new RegistrationException("Incorrect email format");
		}

		if (registrationDto.getConfirmPassword() == null || registrationDto.getConfirmPassword().length() == 0
				|| (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword()))) {
			throw new RegistrationException("password should match with confirm password");
		}
	}
	
	public static User getUser(RegistrationDTO registrationDto) {
		User user = new User();
		user.setEmail(registrationDto.getEmailId());
		user.setName(registrationDto.getUserName());
		user.setPhoneNumber(registrationDto.getPhoneNumber());

		user.setPassword(registrationDto.getPassword());
		return user;
	}

	public static void validateUserForLogin(LoginDTO loginDto) throws LoginException {
		if (loginDto.getEmail() == null || loginDto.getEmail().length() == 0) {
			throw new LoginException("Incorrect email format");
		}

		if (loginDto.getPassword() == null || (!validatePassword(loginDto.getPassword()))) {
			throw new LoginException("Password should have atleast one uppercase character, "
					+ "atlest one lowercase character, " + "one special character, " + "and atleast one number");
		}
	}

	public static void validateUserForResetPassword(ResetPassword resetPassword) throws LoginException {
		if (resetPassword.getPassword() == null || (!validatePassword(resetPassword.getPassword()))) {
			throw new LoginException("Password should have atleast one uppercase character, "
					+ "atlest one lowercase character, " + "one special character, " + "and atleast one number");
		}

		if (resetPassword.getConfirmPassword() == null
				|| (!resetPassword.getPassword().equals(resetPassword.getConfirmPassword()))) {
			throw new LoginException("password should match with confirm password");
		}
	}
	
	public static String generateUUID() {
		UUID randomString = UUID.randomUUID();
		return randomString.toString();
	}

	

}
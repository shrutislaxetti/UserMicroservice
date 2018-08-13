package com.bridgelabz.usermicroservice.service;

import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.usermicroservice.exceptions.LoginException;
import com.bridgelabz.usermicroservice.exceptions.RegistrationException;
import com.bridgelabz.usermicroservice.exceptions.UserNotActivatedException;
import com.bridgelabz.usermicroservice.exceptions.UserNotFoundException;
import com.bridgelabz.usermicroservice.model.LoginDTO;
import com.bridgelabz.usermicroservice.model.Mail;
import com.bridgelabz.usermicroservice.model.RegistrationDTO;
import com.bridgelabz.usermicroservice.model.ResetPassword;
import com.bridgelabz.usermicroservice.model.User;
import com.bridgelabz.usermicroservice.repository.UserElasticRepo;
import com.bridgelabz.usermicroservice.repository.UserRepository;
import com.bridgelabz.usermicroservice.util.JWTokenProvider;
import com.bridgelabz.usermicroservice.util.Utility;


@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserElasticRepo userElasticRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailService emailservive;

	@Autowired
	private JWTokenProvider tokenProvider;
	
	@Value("${activationLink}")
	private String activationLink;

	@Value("${resetPasswordLink}")
	private String resetPasswordLink;

	@Override
	public String login(LoginDTO loginDTO) throws LoginException {
		
		Utility.validateUserForLogin(loginDTO);
		
		Optional<User> optionaluser = repository.findByEmail(loginDTO.getEmail());

		if (!optionaluser.isPresent()) {

			throw new LoginException("Login Unsuccessfull...User Not Present");

		}
		if (!optionaluser.get().isActive()) {
			throw new LoginException("Please Activate user");
		}
		if (!(passwordEncoder.matches(loginDTO.getPassword(), optionaluser.get().getPassword()))) {

			throw new LoginException("Incorrect Password Exception");

		}
		
		return tokenProvider.tokenGenerator(optionaluser.get().getUserId());
	}

	@Override
	public void activateAccount(String token) throws MessagingException, UserNotFoundException {

		String id = tokenProvider.parseJWT(token);

		Optional<User> user = repository.findById(id);

		if (!user.isPresent()) {
			throw new UserNotFoundException("Failed to Activate the User");
		}

		User userobj = user.get();
		userobj.setActive(true);
		repository.save(userobj);

	}

	@Override
	public void forgotPassword(String email) throws MessagingException {

		Mail mail = new Mail();
		String token = tokenProvider.tokenGenerator(email);
		mail.setBody(resetPasswordLink + token);
		mail.setSubject("Account activated");
		mail.setTo(email);
		emailservive.sendMail(mail);
	}

	@Override
	public void register(RegistrationDTO registrationdto) throws RegistrationException, MessagingException {
		
		Utility.validateUserForRegistration(registrationdto);
		Optional<User> optinaluser = repository.findByEmail(registrationdto.getEmailId());

		if (optinaluser.isPresent()) {
			throw new RegistrationException("User with this Email already present!!! Try to Register again..  ");
		}

		User user = new User();
		user.setName(registrationdto.getUserName());
		user.setEmail(registrationdto.getEmailId());
		user.setPhoneNumber(registrationdto.getPhoneNumber());

		String hashedPassword = passwordEncoder.encode(registrationdto.getPassword());

		user.setPassword(hashedPassword);
		repository.save(user);
		userElasticRepo.save(user);

		Mail mail = new Mail();
		String token = tokenProvider.tokenGenerator(user.getUserId());
		mail.setBody(activationLink + token);
		mail.setSubject("Activate Account");
		mail.setTo(user.getEmail());
		emailservive.sendMail(mail);

	}

	@Override
	public void setNewPssword(ResetPassword resetPassword, String token)
			throws UserNotActivatedException, LoginException, UserNotFoundException{
		String email = tokenProvider.parseJWT(token);

		Optional<User> optinaluser = repository.findById(email);

		if (!optinaluser.isPresent()) {
			throw new UserNotFoundException("Failed to reset password");
		}

		User user = new User();
		Utility.validateUserForResetPassword(resetPassword);
		String hashedPassword = passwordEncoder.encode(resetPassword.getPassword());
		user.setPassword(hashedPassword);
		user.setActive(true);
		repository.save(user);

	}

}
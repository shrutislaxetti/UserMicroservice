package com.bridgelabz.usermicroservice.service;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import com.bridgelabz.usermicroservice.model.FacebookUserDetails;
import com.bridgelabz.usermicroservice.model.User;
import com.bridgelabz.usermicroservice.repository.UserRepository;
import com.bridgelabz.usermicroservice.util.JWTokenProvider;

@Service
public class FacebookService {

	@Value("${spring.social.facebook.appId}")
	private String facebookAppId;
	@Value("${spring.social.facebook.appSecret}")
	private String facebookSecret;

	@Autowired
	private JWTokenProvider tokenProvider;

	@Autowired
	private UserRepository userRepository;
	
	private String accessToken;

	public String createFacebookAuthorizationURL() {
		FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
		OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
		OAuth2Parameters params = new OAuth2Parameters();
		params.setRedirectUri(
				"http://localhost:8080/swagger-ui.html#!/user-controller/createFacebookAccessTokenUsingGET");
		params.setScope("public_profile,email,user_birthday");
		return oauthOperations.buildAuthorizeUrl(params);
	}

	public String createFacebookAccessToken(String code) {
		FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
		AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code,
				"http://localhost:8080/swagger-ui.html#!/user-controller/createFacebookAccessTokenUsingGET", null);
		return accessToken = accessGrant.getAccessToken();
	}

	public String getFacebookDetails() {
		Facebook facebook = new FacebookTemplate(accessToken);
		FacebookUserDetails array =facebook.fetchObject("me", FacebookUserDetails.class,"name","email");
		
		Optional<User> optional =userRepository.findByEmail(array.getEmail());
		if(!optional.isPresent()) {
		
			User user =new User();
			user.setActive(true);
			user.setName(optional.get().getName());
			user.setEmail(optional.get().getEmail());
			userRepository.save(user);
			 return tokenProvider.tokenGenerator(user.getUserId());
		}
		return  tokenProvider.tokenGenerator(array.getId());
	}
	
}

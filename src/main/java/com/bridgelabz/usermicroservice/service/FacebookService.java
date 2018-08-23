package com.bridgelabz.usermicroservice.service;

public interface FacebookService {
	
	public String createFacebookAuthorizationURL();

	public String createFacebookAccessToken(String code);

	public String getFacebookDetails();
}

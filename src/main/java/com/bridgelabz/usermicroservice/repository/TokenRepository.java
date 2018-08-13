package com.bridgelabz.usermicroservice.repository;

public interface TokenRepository {
	
	public String find(String token);

	public void save(String token, String userId);
}
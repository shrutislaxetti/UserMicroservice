package com.bridgelabz.usermicroservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.usermicroservice.model.User;


public interface UserRepository extends MongoRepository<User, String> {

	public Optional<User> findByEmail(String email);

}
package com.bridgelabz.usermicroservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bridgelabz.usermicroservice.model.User;

public interface UserElasticRepo extends ElasticsearchRepository<User, String> {

}
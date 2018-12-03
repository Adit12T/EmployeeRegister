package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.AddEmployee;

@Repository
public interface AddEmpRepo extends MongoRepository<AddEmployee, String>{

}

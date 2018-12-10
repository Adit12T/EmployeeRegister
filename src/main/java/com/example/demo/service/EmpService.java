package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmpRepo;

@Service
public class EmpService {

	@Autowired
	private EmpRepo empRepo;
	

	public Employee create(String firstName, String lastName, String phoneNumber, String email, String password, String confirmPassword) {
		return empRepo.save(new Employee (firstName, lastName, phoneNumber, email, password, confirmPassword));
	}
	
	public Employee getByFirstName(String firstName) {
		
		return empRepo.findByFirstName(firstName);
		}
	
	public List<Employee> getAll(){
		return empRepo.findAll();
	}
}

package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.AddEmployee;
import com.example.demo.repository.AddEmpRepo;

@Service
public class AddEmpService {

	@Autowired
	private AddEmpRepo addEmpRepo;
	
	public AddEmployee create(String firstName, String lastName, String phoneNumber, String primaryEmail, String secondaryEmail, String address, String jobCode, String jobTitle, String endClient, String startDate, String vendor, String projectId, String projectName, String managerName, String approverName) {
		return addEmpRepo.save(new AddEmployee (firstName, lastName, phoneNumber, primaryEmail, secondaryEmail, address, jobCode, jobTitle, endClient, startDate, vendor, projectId, projectName, managerName, approverName));
	}
	
	public List<AddEmployee> getByFirstName(String firstName) {
		
		return addEmpRepo.findByFirstName(firstName);
		}
	
	public List<AddEmployee> getAll(){
		return addEmpRepo.findAll();
	}
}

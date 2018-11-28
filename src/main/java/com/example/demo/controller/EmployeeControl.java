package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmpRepo;
import com.example.demo.service.EmpService;

@RestController
public class EmployeeControl {

	@Autowired
	private EmpService eService;
	@Autowired
	private EmpRepo empRepo;
	@PostMapping(
            path= {"/create"},
            consumes= {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces= {
                    MediaType.APPLICATION_JSON_VALUE
                    
            })
    public ResponseEntity<Employee> create(@RequestBody Employee emp ) {
				
	
		Employee empInfo = new Employee();
		empInfo.setFirstName(emp.getFirstName());
        empInfo.setLastName(emp.getLastName());
        empInfo.setphoneNumber(emp.getphoneNumber());
        empInfo.setEmail(emp.getEmail());
        empInfo.setPassword(emp.getPassword());
        empInfo.setConfirmPassword(emp.getConfirmPassword());
        
        System.out.println(empInfo);
        empRepo.save(empInfo);
        
        return new ResponseEntity<Employee>(empInfo,HttpStatus.OK);
}
	
	@GetMapping(path = "/getAll")
	public List<Employee> getAll(){
		return eService.getAll();
	}
}

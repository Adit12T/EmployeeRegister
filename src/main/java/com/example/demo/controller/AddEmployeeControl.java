package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AddEmployee;
import com.example.demo.repository.AddEmpRepo;
import com.example.demo.service.AddEmpService;

@RestController
public class AddEmployeeControl {

	@Autowired
	private AddEmpService eService;
	@Autowired
	private AddEmpRepo empRepo;
	@PostMapping(
            path= {"/employee/create"},
            consumes= {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces= {
                    MediaType.APPLICATION_JSON_VALUE
                    
            })
    public ResponseEntity<AddEmployee> create(@RequestBody AddEmployee emp ) {
        
        empRepo.save(emp);
        return new ResponseEntity<AddEmployee>(emp,HttpStatus.OK);
}
	
	@GetMapping(path = "employee/search/{firstName}")
	public List<AddEmployee> getList(@PathVariable String firstName) {
		return eService.getByFirstName(firstName);
	}     
	
	@GetMapping(path = "employee/listEmployees")
	public List<AddEmployee> getAll(){
		return eService.getAll();
	}
}

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
            path= {"/employee"},
            consumes= {
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces= {
                    MediaType.APPLICATION_JSON_VALUE
                    
            })
    public ResponseEntity<AddEmployee> create(@RequestBody AddEmployee emp ) {
				
	
		AddEmployee empInfo = new AddEmployee();
		empInfo.setFirstName(emp.getFirstName());
        empInfo.setLastName(emp.getLastName());
        empInfo.setPhoneNumber(emp.getPhoneNumber());
        empInfo.setPrimaryEmail(emp.getPrimaryEmail());
        empInfo.setSecondaryEmail(emp.getSecondaryEmail());
        empInfo.setAddress(emp.getAddress());
        empInfo.setJobCode(emp.getJobCode());
        empInfo.setJobTitle(emp.getJobTitle());
        empInfo.setEndClient(emp.getEndClient());
        empInfo.setStartDate(emp.getStartDate());
        empInfo.setVendor(emp.getVendor());
        empInfo.setProjectId(emp.getProjectId());
        empInfo.setProjectName(emp.getProjectName());
        empInfo.setManagerName(emp.getManagerName());
        empInfo.setApproverName(emp.getApproverName());
        
        empRepo.save(empInfo);
        
        return new ResponseEntity<AddEmployee>(empInfo,HttpStatus.OK);
}
	
	@GetMapping(path = "/listEmployees")
	public List<AddEmployee> getAll(){
		return eService.getAll();
	}
}

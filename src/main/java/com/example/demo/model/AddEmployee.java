package com.example.demo.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AddEmployee {

	@Id
	UUID id = UUID.randomUUID();
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String primaryEmail; 
	private String secondaryEmail;
	private String address; 
	private String jobCode;
	private String jobTitle;
	private String endClient;
	private String startDate;
	private String vendor;
	private String projectId;
	private String projectName;
	private String managerName;
	private String approverName;
	
	public AddEmployee() {
		
	}

	public AddEmployee(String firstName, String lastName, String phoneNumber, String primaryEmail,
			String secondaryEmail, String address, String jobCode, String jobTitle, String endClient, String startDate,
			String vendor, String projectId, String projectName, String managerName, String approverName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.primaryEmail = primaryEmail;
		this.secondaryEmail = secondaryEmail;
		this.address = address;
		this.jobCode = jobCode;
		this.jobTitle = jobTitle;
		this.endClient = endClient;
		this.startDate = startDate;
		this.vendor = vendor;
		this.projectId = projectId;
		this.projectName = projectName;
		this.managerName = managerName;
		this.approverName = approverName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getSecondaryEmail() {
		return secondaryEmail;
	}

	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getEndClient() {
		return endClient;
	}

	public void setEndClient(String endClient) {
		this.endClient = endClient;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	@Override
	public String toString() {
		return String.format(
				"AddEmployee [firstName=%s, lastName=%s, phoneNumber=%s, primaryEmail=%s, secondaryEmail=%s, address=%s, jobCode=%s, jobTitle=%s, endClient=%s, startDate=%s, vendor=%s, projectId=%s, projectName=%s, managerName=%s, approverName=%s]",
				firstName, lastName, phoneNumber, primaryEmail, secondaryEmail, address, jobCode, jobTitle, endClient,
				startDate, vendor, projectId, projectName, managerName, approverName);
	}
	
}
	
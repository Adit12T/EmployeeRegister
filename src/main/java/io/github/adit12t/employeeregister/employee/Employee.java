package io.github.adit12t.employeeregister.employee;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

/** An onboarded employee: personal details, the assignment, and who manages and approves it. */
@Document("employees")
public class Employee {

    @Id
    private String id;
    @Version
    private Long version;
    @Indexed
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Indexed(unique = true)
    private String primaryEmail;
    private String secondaryEmail;
    private String address;
    private String jobCode;
    private String jobTitle;
    private String endClient;
    private LocalDate startDate;
    private String vendor;
    private String projectId;
    private String projectName;
    private String managerName;
    private String approverName;
    private EmploymentStatus status = EmploymentStatus.ONBOARDING;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
    @LastModifiedBy
    private String updatedBy;

    public void apply(EmployeeDtos.EmployeeRequest request) {
        this.firstName = request.firstName().trim();
        this.lastName = request.lastName().trim();
        this.phoneNumber = request.phoneNumber();
        this.primaryEmail = request.primaryEmail().trim().toLowerCase(java.util.Locale.ROOT);
        this.secondaryEmail = request.secondaryEmail();
        this.address = request.address();
        this.jobCode = request.jobCode();
        this.jobTitle = request.jobTitle();
        this.endClient = request.endClient();
        this.startDate = request.startDate();
        this.vendor = request.vendor();
        this.projectId = request.projectId();
        this.projectName = request.projectName();
        this.managerName = request.managerName();
        this.approverName = request.approverName();
        if (request.status() != null) {
            this.status = request.status();
        }
    }

    public String getId() { return id; }
    public Long getVersion() { return version; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPrimaryEmail() { return primaryEmail; }
    public String getSecondaryEmail() { return secondaryEmail; }
    public String getAddress() { return address; }
    public String getJobCode() { return jobCode; }
    public String getJobTitle() { return jobTitle; }
    public String getEndClient() { return endClient; }
    public LocalDate getStartDate() { return startDate; }
    public String getVendor() { return vendor; }
    public String getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public String getManagerName() { return managerName; }
    public String getApproverName() { return approverName; }
    public EmploymentStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getUpdatedBy() { return updatedBy; }
}

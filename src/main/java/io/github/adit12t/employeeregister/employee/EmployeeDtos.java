package io.github.adit12t.employeeregister.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;

public final class EmployeeDtos {

    private EmployeeDtos() {
    }

    public record EmployeeRequest(
            @NotBlank @Size(max = 50) String firstName,
            @NotBlank @Size(max = 50) String lastName,
            @Pattern(regexp = "^\\+?[0-9 ()-]{7,20}$", message = "must be a valid phone number") String phoneNumber,
            @NotBlank @Email @Size(max = 254) String primaryEmail,
            @Email @Size(max = 254) String secondaryEmail,
            @Size(max = 300) String address,
            @Size(max = 30) String jobCode,
            @NotBlank @Size(max = 100) String jobTitle,
            @Size(max = 100) String endClient,
            @NotNull LocalDate startDate,
            @Size(max = 100) String vendor,
            @Size(max = 30) String projectId,
            @Size(max = 100) String projectName,
            @Size(max = 100) String managerName,
            @Size(max = 100) String approverName,
            EmploymentStatus status) {
    }

    public record EmployeeResponse(String id, long version, String firstName, String lastName, String phoneNumber,
                                   String primaryEmail, String secondaryEmail, String address, String jobCode,
                                   String jobTitle, String endClient, LocalDate startDate, String vendor,
                                   String projectId, String projectName, String managerName, String approverName,
                                   EmploymentStatus status, Instant createdAt, Instant updatedAt, String updatedBy) {

        static EmployeeResponse from(Employee e) {
            return new EmployeeResponse(e.getId(), e.getVersion() == null ? 0 : e.getVersion(), e.getFirstName(),
                    e.getLastName(), e.getPhoneNumber(), e.getPrimaryEmail(), e.getSecondaryEmail(), e.getAddress(),
                    e.getJobCode(), e.getJobTitle(), e.getEndClient(), e.getStartDate(), e.getVendor(),
                    e.getProjectId(), e.getProjectName(), e.getManagerName(), e.getApproverName(), e.getStatus(),
                    e.getCreatedAt(), e.getUpdatedAt(), e.getUpdatedBy());
        }
    }
}

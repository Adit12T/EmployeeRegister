package io.github.adit12t.employeeregister.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Page<Employee> findByFirstNameIgnoreCase(String firstName, Pageable pageable);

    Page<Employee> findByStatus(EmploymentStatus status, Pageable pageable);

    Page<Employee> findByFirstNameIgnoreCaseAndStatus(String firstName, EmploymentStatus status, Pageable pageable);

    boolean existsByPrimaryEmailIgnoreCase(String primaryEmail);
}

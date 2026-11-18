package io.github.adit12t.employeeregister.employee;

import io.github.adit12t.employeeregister.common.ConflictException;
import io.github.adit12t.employeeregister.common.NotFoundException;
import io.github.adit12t.employeeregister.employee.EmployeeDtos.EmployeeRequest;
import io.github.adit12t.employeeregister.employee.EmployeeDtos.EmployeeResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employees;

    public EmployeeService(EmployeeRepository employees) {
        this.employees = employees;
    }

    public EmployeeResponse create(EmployeeRequest request) {
        if (employees.existsByPrimaryEmailIgnoreCase(request.primaryEmail().trim())) {
            throw new ConflictException("An employee with this primary email already exists.");
        }
        Employee employee = new Employee();
        employee.apply(request);
        return save(employee);
    }

    public EmployeeResponse get(String id) {
        return EmployeeResponse.from(find(id));
    }

    public Page<EmployeeResponse> search(String firstName, EmploymentStatus status, Pageable pageable) {
        boolean byName = firstName != null && !firstName.isBlank();
        Page<Employee> page;
        if (byName && status != null) {
            page = employees.findByFirstNameIgnoreCaseAndStatus(firstName.trim(), status, pageable);
        } else if (byName) {
            page = employees.findByFirstNameIgnoreCase(firstName.trim(), pageable);
        } else if (status != null) {
            page = employees.findByStatus(status, pageable);
        } else {
            page = employees.findAll(pageable);
        }
        return page.map(EmployeeResponse::from);
    }

    /** Updates only if the caller has seen the latest version, so concurrent edits are not silently lost. */
    public EmployeeResponse update(String id, long expectedVersion, EmployeeRequest request) {
        Employee employee = find(id);
        if (employee.getVersion() != null && employee.getVersion() != expectedVersion) {
            throw new ConflictException("The employee was changed by someone else. Reload it and try again.");
        }
        employee.apply(request);
        return save(employee);
    }

    public void delete(String id) {
        employees.delete(find(id));
    }

    private Employee find(String id) {
        return employees.findById(id).orElseThrow(() -> new NotFoundException("No employee with id " + id));
    }

    private EmployeeResponse save(Employee employee) {
        try {
            return EmployeeResponse.from(employees.save(employee));
        } catch (DuplicateKeyException e) {
            throw new ConflictException("An employee with this primary email already exists.");
        } catch (OptimisticLockingFailureException e) {
            throw new ConflictException("The employee was changed by someone else. Reload it and try again.");
        }
    }
}

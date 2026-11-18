package io.github.adit12t.employeeregister.employee;

import io.github.adit12t.employeeregister.common.PageResponse;
import io.github.adit12t.employeeregister.employee.EmployeeDtos.EmployeeRequest;
import io.github.adit12t.employeeregister.employee.EmployeeDtos.EmployeeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employees", description = "Onboarding records")
@SecurityRequirement(name = "basicAuth")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Onboard an employee")
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse created = service.create(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}").buildAndExpand(created.id()).toUri())
                .eTag(etag(created.version()))
                .body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one employee", description = "The ETag header carries the version to send back on update.")
    public ResponseEntity<EmployeeResponse> get(@PathVariable String id) {
        EmployeeResponse employee = service.get(id);
        return ResponseEntity.ok().eTag(etag(employee.version())).body(employee);
    }

    @GetMapping
    @Operation(summary = "Search employees by first name and/or status")
    public PageResponse<EmployeeResponse> search(@RequestParam(required = false) String firstName,
                                                 @RequestParam(required = false) EmploymentStatus status,
                                                 @PageableDefault(size = 20, sort = "lastName", direction = Sort.Direction.ASC)
                                                 Pageable pageable) {
        return PageResponse.of(service.search(firstName, status, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee", description = "Send the ETag from GET in If-Match to avoid overwriting someone else's change.")
    public ResponseEntity<EmployeeResponse> update(@PathVariable String id,
                                                   @Parameter(description = "ETag returned by GET, e.g. \"0\"")
                                                   @RequestHeader("If-Match") String ifMatch,
                                                   @Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse updated = service.update(id, parseVersion(ifMatch), request);
        return ResponseEntity.ok().eTag(etag(updated.version())).body(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static String etag(long version) {
        return "\"" + version + "\"";
    }

    private static long parseVersion(String ifMatch) {
        try {
            return Long.parseLong(ifMatch.replace("W/", "").replace("\"", "").trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("If-Match must be the ETag returned by GET, for example \"3\".");
        }
    }
}

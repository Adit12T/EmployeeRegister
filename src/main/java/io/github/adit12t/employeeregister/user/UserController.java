package io.github.adit12t.employeeregister.user;

import io.github.adit12t.employeeregister.common.PageResponse;
import io.github.adit12t.employeeregister.user.UserDtos.RegisterUserRequest;
import io.github.adit12t.employeeregister.user.UserDtos.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Registration and user lookup")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Register a user", description = "Open to anyone. The password is stored as a BCrypt hash.")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse created = userService.register(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .queryParam("email", created.email()).build().toUri()).body(created);
    }

    @GetMapping
    @Operation(summary = "List users, optionally filtered by first name")
    public PageResponse<UserResponse> list(@RequestParam(required = false) String firstName,
                                           @PageableDefault(size = 20, sort = "lastName", direction = Sort.Direction.ASC)
                                           Pageable pageable) {
        return PageResponse.of(userService.list(firstName, pageable));
    }
}

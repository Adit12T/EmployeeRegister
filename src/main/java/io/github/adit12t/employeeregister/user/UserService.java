package io.github.adit12t.employeeregister.user;

import io.github.adit12t.employeeregister.common.ConflictException;
import io.github.adit12t.employeeregister.user.UserDtos.RegisterUserRequest;
import io.github.adit12t.employeeregister.user.UserDtos.UserResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterUserRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("A user with this email is already registered.");
        }
        UserAccount account = new UserAccount(request.firstName().trim(), request.lastName().trim(),
                request.phoneNumber(), email, passwordEncoder.encode(request.password()));
        try {
            return UserResponse.from(users.save(account));
        } catch (DuplicateKeyException e) {
            // Two registrations raced past the check above; the unique index decides.
            throw new ConflictException("A user with this email is already registered.");
        }
    }

    public Page<UserResponse> list(String firstName, Pageable pageable) {
        Page<UserAccount> page = firstName == null || firstName.isBlank()
                ? users.findAll(pageable)
                : users.findByFirstNameIgnoreCase(firstName.trim(), pageable);
        return page.map(UserResponse::from);
    }
}

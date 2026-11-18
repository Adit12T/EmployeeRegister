package io.github.adit12t.employeeregister.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public final class UserDtos {

    private UserDtos() {
    }

    public record RegisterUserRequest(
            @NotBlank @Size(max = 50) String firstName,
            @NotBlank @Size(max = 50) String lastName,
            @Pattern(regexp = "^\\+?[0-9 ()-]{7,20}$", message = "must be a valid phone number") String phoneNumber,
            @NotBlank @Email @Size(max = 254) String email,
            @NotBlank @Size(min = 12, max = 128, message = "must be between 12 and 128 characters") String password,
            @NotBlank String confirmPassword) {

        @AssertTrue(message = "password and confirmPassword must match")
        public boolean isPasswordConfirmed() {
            return password == null || password.equals(confirmPassword);
        }

        @Override
        public String toString() {
            return "RegisterUserRequest[email=" + email + "]";
        }
    }

    /** What the API returns about a user: never the password or its hash. */
    public record UserResponse(String id, String firstName, String lastName, String phoneNumber, String email,
                               Instant createdAt) {

        static UserResponse from(UserAccount user) {
            return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(),
                    user.getEmail(), user.getCreatedAt());
        }
    }
}

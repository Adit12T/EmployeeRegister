package io.github.adit12t.employeeregister.user;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/** A registered user. Only a BCrypt hash of the password is stored. */
@Document("users")
public class UserAccount {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Indexed(unique = true)
    private String email;
    private String passwordHash;
    @CreatedDate
    private Instant createdAt;

    protected UserAccount() {
    }

    public UserAccount(String firstName, String lastName, String phoneNumber, String email, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Instant getCreatedAt() { return createdAt; }
}

package io.github.adit12t.employeeregister;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Runs the whole application against a real MongoDB in a container. */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class EmployeeRegisterIntegrationTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    private static final String EMAIL = "jane.doe@example.com";
    private static final String PASSWORD = "correct-horse-battery";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void cleanDatabase() {
        // Clear documents but keep the unique indexes created at startup.
        mongoTemplate.getCollection("users").deleteMany(new Document());
        mongoTemplate.getCollection("employees").deleteMany(new Document());
    }

    private void register() throws Exception {
        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content("""
                        {"firstName":"Jane","lastName":"Doe","phoneNumber":"+1 555 010 0100",
                         "email":"%s","password":"%s","confirmPassword":"%s"}
                        """.formatted(EMAIL, PASSWORD, PASSWORD)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$", not(hasKey("password"))))
                .andExpect(jsonPath("$", not(hasKey("passwordHash"))));
    }

    private static String employeeJson(String jobTitle) {
        return """
                {"firstName":"Sam","lastName":"Lee","primaryEmail":"sam.lee@example.com","jobTitle":"%s",
                 "startDate":"2026-01-05","projectName":"Claims Portal","managerName":"Jane Doe"}
                """.formatted(jobTitle);
    }

    @Test
    void registeredPasswordIsStoredOnlyAsAHash() throws Exception {
        register();

        String stored = mongoTemplate.getCollection("users").find().first().getString("passwordHash");
        assertThat(stored).startsWith("$2a$12$").doesNotContain(PASSWORD);
    }

    @Test
    void rejectsDuplicateEmailMismatchedPasswordsAndWeakInput() throws Exception {
        register();
        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content("""
                        {"firstName":"Jane","lastName":"Doe","email":"JANE.DOE@example.com",
                         "password":"%s","confirmPassword":"%s"}""".formatted(PASSWORD, PASSWORD)))
                .andExpect(status().isConflict());
        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content("""
                        {"firstName":"","lastName":"Doe","email":"not-an-email",
                         "password":"short","confirmPassword":"different"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    void employeeEndpointsNeedARegisteredUser() throws Exception {
        mvc.perform(get("/api/v1/employees")).andExpect(status().isUnauthorized());
        register();
        mvc.perform(get("/api/v1/employees").with(httpBasic(EMAIL, "wrong-password")))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/v1/employees").with(httpBasic(EMAIL, PASSWORD)))
                .andExpect(status().isOk());
    }

    @Test
    void fullEmployeeLifecycleWithOptimisticLocking() throws Exception {
        register();

        MvcResult created = mvc.perform(post("/api/v1/employees").with(httpBasic(EMAIL, PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON).content(employeeJson("Software Engineer")))
                .andExpect(status().isCreated())
                .andExpect(header().string("ETag", "\"0\""))
                .andExpect(jsonPath("$.status").value("ONBOARDING"))
                .andExpect(jsonPath("$.updatedBy").value(EMAIL))
                .andReturn();
        String id = com.jayway.jsonpath.JsonPath.read(created.getResponse().getContentAsString(), "$.id");

        mvc.perform(get("/api/v1/employees").param("firstName", "sam").with(httpBasic(EMAIL, PASSWORD)))
                .andExpect(jsonPath("$.totalElements").value(1));

        mvc.perform(put("/api/v1/employees/{id}", id).with(httpBasic(EMAIL, PASSWORD)).header("If-Match", "\"0\"")
                        .contentType(MediaType.APPLICATION_JSON).content(employeeJson("Senior Software Engineer")))
                .andExpect(status().isOk())
                .andExpect(header().string("ETag", "\"1\""));

        // A second writer still holding version 0 must not overwrite the change.
        mvc.perform(put("/api/v1/employees/{id}", id).with(httpBasic(EMAIL, PASSWORD)).header("If-Match", "\"0\"")
                        .contentType(MediaType.APPLICATION_JSON).content(employeeJson("Tech Lead")))
                .andExpect(status().isConflict());

        mvc.perform(post("/api/v1/employees").with(httpBasic(EMAIL, PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON).content(employeeJson("Duplicate")))
                .andExpect(status().isConflict());

        mvc.perform(delete("/api/v1/employees/{id}", id).with(httpBasic(EMAIL, PASSWORD)))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/v1/employees/{id}", id).with(httpBasic(EMAIL, PASSWORD)))
                .andExpect(status().isNotFound());
    }

    @Test
    void crossOriginRequestsFromUnknownOriginsAreRefused() throws Exception {
        mvc.perform(post("/api/v1/users").header("Origin", "https://evil.example")
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }
}

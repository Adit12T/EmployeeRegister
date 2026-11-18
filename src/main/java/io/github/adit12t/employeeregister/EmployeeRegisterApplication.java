package io.github.adit12t.employeeregister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class EmployeeRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeRegisterApplication.class, args);
    }
}

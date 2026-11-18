package io.github.adit12t.employeeregister.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/** Browser origins allowed to call the API. Empty means no cross-origin access. */
@ConfigurationProperties("app.cors")
public record CorsProperties(List<String> allowedOrigins) {

    public CorsProperties {
        allowedOrigins = allowedOrigins == null ? List.of() : List.copyOf(allowedOrigins);
    }
}

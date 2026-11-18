package io.github.adit12t.employeeregister.common;

import org.springframework.data.domain.Page;

import java.util.List;

/** A stable JSON shape for paged results, independent of Spring's internal Page serialisation. */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getTotalPages());
    }
}

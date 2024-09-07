package org.hygorp.listmanager.records;

import java.time.LocalDate;

public record NewTaskDTO(String title, String description, LocalDate expiresAt) {
}

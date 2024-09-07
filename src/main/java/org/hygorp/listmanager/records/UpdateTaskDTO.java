package org.hygorp.listmanager.records;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTaskDTO(UUID id, String title, String description, LocalDate expiresAt) {
}

package org.hygorp.listmanager.records;

import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;

public record NewItemDTO(String title, String description, ItemPriorityEnum priority, ItemStateEnum state) {
}

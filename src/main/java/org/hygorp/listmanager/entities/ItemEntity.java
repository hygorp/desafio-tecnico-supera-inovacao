package org.hygorp.listmanager.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity(name = "tb_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class ItemEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemPriorityEnum priority;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemStateEnum state;

    public ItemEntity(String title, String description, ItemPriorityEnum priority, ItemStateEnum state) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.state = state;
    }
}

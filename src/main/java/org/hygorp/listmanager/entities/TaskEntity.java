package org.hygorp.listmanager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tb_task")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class TaskEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "tb_task_item",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<ItemEntity> items = new HashSet<>();

    public TaskEntity(String title, String description, LocalDateTime expiresAt) {
        this.title = title;
        this.description = description;
        this.expiresAt = expiresAt;
    }

    public void addItem(ItemEntity item) {
        this.items.add(item);
        setUpdatedAt(LocalDateTime.now());
    }

    public void updateItem(ItemEntity item) {
        for (ItemEntity savedItem : items) {
            if (savedItem.getId().equals(item.getId())) {
                if (!Objects.equals(savedItem.getTitle(), item.getTitle()) || item.getTitle() == null)
                    savedItem.setTitle(item.getTitle());

                if (!Objects.equals(savedItem.getDescription(), item.getDescription()) || item.getDescription() == null)
                    savedItem.setDescription(item.getDescription());

                if (!Objects.equals(savedItem.getPriority(), item.getPriority()) || item.getPriority() == null)
                    savedItem.setPriority(item.getPriority());

                if (!Objects.equals(savedItem.getState(), item.getState()) || item.getState() == null)
                    savedItem.setState(item.getState());

                setUpdatedAt(LocalDateTime.now());

                return;
            }
        }
    }

    public void removeItem(ItemEntity item) {
        this.items.remove(item);
        setUpdatedAt(LocalDateTime.now());
    }

    public void clearItems() {
        this.items.clear();
        setUpdatedAt(LocalDateTime.now());
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

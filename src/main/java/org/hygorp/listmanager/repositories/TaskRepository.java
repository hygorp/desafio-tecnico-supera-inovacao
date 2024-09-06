package org.hygorp.listmanager.repositories;

import org.hygorp.listmanager.entities.TaskEntity;
import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    @Query("SELECT t FROM tb_task t JOIN t.items i WHERE (:priority IS NULL OR i.priority = :priority)")
    List<TaskEntity> findTaskByItemsPriority(@Param("priority")ItemPriorityEnum priority);

    @Query("SELECT t FROM tb_task t JOIN t.items i WHERE (:state IS NULL OR i.state = :state)")
    List<TaskEntity> findTaskByItemsState(@Param("state")ItemStateEnum state);

    List<TaskEntity> findAllByCreatedAtAfter(LocalDate createdAt);

    List<TaskEntity> findAllByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
}

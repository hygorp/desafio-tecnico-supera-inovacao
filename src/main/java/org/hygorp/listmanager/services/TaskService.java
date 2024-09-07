package org.hygorp.listmanager.services;

import org.hygorp.listmanager.entities.ItemEntity;
import org.hygorp.listmanager.entities.TaskEntity;
import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;
import org.hygorp.listmanager.repositories.TaskRepository;
import org.hygorp.listmanager.services.exceptions.TaskServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public TaskEntity save(TaskEntity taskEntity) {
        try {
            if(taskEntity.getExpiresAt().isBefore(LocalDate.now()))
                throw new TaskServiceException("Task expired");

            return taskRepository.save(taskEntity);
        } catch (Exception e) {
            throw new TaskServiceException("Error while saving task");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public TaskEntity update(UUID id, TaskEntity taskEntity) {
        try {
            if (taskEntity.getExpiresAt().isBefore(LocalDate.now()))
                throw new TaskServiceException("Invalid date");

            TaskEntity savedTask = taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));

            if (!Objects.equals(savedTask.getTitle(), taskEntity.getTitle()) || taskEntity.getTitle() == null) {
                savedTask.setTitle(taskEntity.getTitle());
            }

            if (!Objects.equals(savedTask.getDescription(), taskEntity.getDescription()) || taskEntity.getDescription() == null) {
                savedTask.setDescription(taskEntity.getDescription());
            }

            if (!Objects.equals(savedTask.getExpiresAt(), taskEntity.getExpiresAt()) || taskEntity.getExpiresAt() == null) {
                savedTask.setExpiresAt(taskEntity.getExpiresAt());
            }

            return taskRepository.save(savedTask);
        } catch (NoSuchElementException e) {
            throw new TaskServiceException("Task not found with provided id: " + id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void delete(UUID id) {
        try {
            taskRepository.deleteById(id);
        } catch (Exception exception) {
            throw new TaskServiceException("Error while deleting task");
        }

    }

    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }

    public List<TaskEntity> findByTitle(String title) {
        return taskRepository.findAllByTitleContainingIgnoreCase(title);
    }

    public List<TaskEntity> findAllByItemsPriority(ItemPriorityEnum priority) {
        return taskRepository.findTaskByItemsPriority(priority);
    }

    public List<TaskEntity> findAllByItemsState(ItemStateEnum state) {
        return taskRepository.findTaskByItemsState(state);
    }

    public List<TaskEntity> findAllByCreatedAtAfter(String dateString) {
        return taskRepository.findAllByCreatedAtAfter(stringToLocalDateTime(dateString));
    }

    public List<TaskEntity> findAllByCreatedAtBetween(String startDate, String endDate) {
        return taskRepository.findAllByCreatedAtBetween(
                stringToLocalDateTime(startDate), stringToLocalDateTime(endDate)
        );
    }

    public TaskEntity findById(UUID id) {
        try {
            return taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));
        } catch (NoSuchElementException exception) {
            throw new TaskServiceException("Task not found with provided id: " + id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public TaskEntity addItem(UUID taskId, ItemEntity itemEntity) {
        try {
            TaskEntity savedTask = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found"));

            savedTask.addItem(itemEntity);

            return taskRepository.save(savedTask);
        } catch (NoSuchElementException exception) {
            throw new TaskServiceException("Task not found with provided id: " + taskId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public TaskEntity updateItem(UUID taskId, ItemEntity item) {
        try {
            TaskEntity savedTask = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found"));

            savedTask.updateItem(item);

            return taskRepository.save(savedTask);
        } catch (NoSuchElementException exception) {
            throw new TaskServiceException("Task not found with provided id: " + taskId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public TaskEntity removeItem(UUID taskId, ItemEntity itemEntity) {
        try {
            TaskEntity savedTask = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found"));

            savedTask.removeItem(itemEntity);

            return taskRepository.save(savedTask);
        } catch (NoSuchElementException exception) {
            throw new TaskServiceException("Task not found with provided id: " + taskId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public TaskEntity clearItems(UUID taskId) {
        try {
            TaskEntity savedTask = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found"));

            savedTask.clearItems();

            return taskRepository.save(savedTask);
        } catch (NoSuchElementException exception) {
            throw new TaskServiceException("Task not found with provided id: " + taskId);
        }
    }

    private LocalDate stringToLocalDateTime(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateString, inputFormatter);
    }
}

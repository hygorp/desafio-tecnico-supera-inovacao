package org.hygorp.listmanager.resources;

import org.hygorp.listmanager.entities.ItemEntity;
import org.hygorp.listmanager.entities.TaskEntity;
import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;
import org.hygorp.listmanager.services.TaskService;
import org.hygorp.listmanager.services.exceptions.TaskServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskResource {
    private final TaskService taskService;

    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/save")
    public ResponseEntity<TaskEntity> save(@RequestBody TaskEntity task) {
        try {
            TaskEntity savedTask = taskService.save(task);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskEntity> update(@PathVariable UUID id, @RequestBody TaskEntity task) {
        try {
            TaskEntity updatedTask = taskService.update(id, task);

            return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
        } catch (TaskServiceException exception) {
            if (exception.getMessage().equals("Invalid date"))
                return ResponseEntity.status(HttpStatus.CONFLICT).build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            taskService.delete(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<TaskEntity>> findAll() {
            List<TaskEntity> tasks = taskService.findAll();

            return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/find-all-by-items-priority")
    public ResponseEntity<List<TaskEntity>> findAllByItemsPriority(@RequestParam ItemPriorityEnum priority) {
        List<TaskEntity> tasks = taskService.findAllByItemsPriority(priority);

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/find-all-by-items-state")
    public ResponseEntity<List<TaskEntity>> findAllByItemsState(@RequestParam ItemStateEnum state) {
        List<TaskEntity> tasks = taskService.findAllByItemsState(state);

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/find-all-by-created-after")
    public ResponseEntity<List<TaskEntity>> findAllByCreatedAfter(@RequestParam String date) {
        List<TaskEntity> tasks = taskService.findAllByCreatedAtAfter(date);

        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/find-all-by-created-between")
    public ResponseEntity<List<TaskEntity>> findAllByCreatedAtBetween(@RequestParam String startDate, @RequestParam String endDate) {
        List<TaskEntity> tasks = taskService.findAllByCreatedAtBetween(startDate, endDate);

        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<TaskEntity> findById(@PathVariable UUID id) {
        try {
            TaskEntity task = taskService.findById(id);

            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/task/{id}/add-item")
    public ResponseEntity<TaskEntity> addItem(@PathVariable UUID id, @RequestBody ItemEntity item) {
        try {
            TaskEntity task = taskService.addItem(id, item);
            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/task/{id}/update-item")
    public ResponseEntity<TaskEntity> updateItem(@PathVariable UUID id, @RequestBody ItemEntity item) {
        try {
            TaskEntity task = taskService.updateItem(id, item);
            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/task/{id}/delete-item")
    public ResponseEntity<TaskEntity> deleteItem(@PathVariable UUID id, @RequestBody ItemEntity item) {
        try {
            TaskEntity task = taskService.removeItem(id, item);

            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/task/{id}/clear-items")
    public ResponseEntity<TaskEntity> clearItems(@PathVariable UUID id) {
        try {
            TaskEntity task = taskService.clearItems(id);

            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

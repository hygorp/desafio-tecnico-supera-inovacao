package org.hygorp.listmanager.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hygorp.listmanager.entities.ItemEntity;
import org.hygorp.listmanager.entities.TaskEntity;
import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;
import org.hygorp.listmanager.records.NewItemDTO;
import org.hygorp.listmanager.records.NewTaskDTO;
import org.hygorp.listmanager.records.UpdateTaskDTO;
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
    @Operation(summary = "Criar uma nova Tarefa")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = NewTaskDTO.class))}
            )
    })
    public ResponseEntity<TaskEntity> save(@RequestBody NewTaskDTO task) {
        try {
            TaskEntity savedTask = taskService.save(new TaskEntity(
                    task.title(),
                    task.description(),
                    task.expiresAt())
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Editar Tarefa")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateTaskDTO.class))}
            )
    })
    public ResponseEntity<TaskEntity> update(@PathVariable UUID id, @RequestBody UpdateTaskDTO task) {
        try {
            TaskEntity updatedTask = taskService.update(id, new TaskEntity(
                    task.id(),
                    task.title(),
                    task.description(),
                    task.expiresAt()
            ));

            return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
        } catch (TaskServiceException exception) {
            if (exception.getMessage().equals("Invalid date"))
                return ResponseEntity.status(HttpStatus.CONFLICT).build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletar Tarefa")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204"
            )
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            taskService.delete(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/find-all")
    @Operation(summary = "Buscar todas as Tarefas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TaskEntity.class))}
            )
    })
    public ResponseEntity<List<TaskEntity>> findAll() {
            List<TaskEntity> tasks = taskService.findAll();

            return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/find-by-title")
    @Operation(summary = "Buscar Tarefas por título")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TaskEntity.class))}
            )
    })
    public ResponseEntity<List<TaskEntity>> findByTitle(@RequestParam("title") String title) {
        List<TaskEntity> tasks = taskService.findByTitle(title);

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/find-all-by-items-priority")
    @Operation(summary = "Buscar Tarefas por prioridade dos Itens")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TaskEntity.class))}
            )
    })
    public ResponseEntity<List<TaskEntity>> findAllByItemsPriority(@RequestParam ItemPriorityEnum priority) {
        List<TaskEntity> tasks = taskService.findAllByItemsPriority(priority);

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/find-all-by-items-state")
    @Operation(summary = "Buscar Tarefas por status dos Itens")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TaskEntity.class))}
            )
    })
    public ResponseEntity<List<TaskEntity>> findAllByItemsState(@RequestParam ItemStateEnum state) {
        List<TaskEntity> tasks = taskService.findAllByItemsState(state);

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/find-all-by-created-after")
    @Operation(summary = "Buscar Tarefas a partir de data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TaskEntity.class))}
            )
    })
    public ResponseEntity<List<TaskEntity>> findAllByCreatedAfter(@RequestParam String date) {
        List<TaskEntity> tasks = taskService.findAllByCreatedAtAfter(date);

        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/find-all-by-created-between")
    @Operation(summary = "Buscar Tarefas por intervalo de data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TaskEntity.class))}
            )
    })
    public ResponseEntity<List<TaskEntity>> findAllByCreatedAtBetween(@RequestParam String startDate, @RequestParam String endDate) {
        List<TaskEntity> tasks = taskService.findAllByCreatedAtBetween(startDate, endDate);

        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/find-by-id/{id}")
    @Operation(summary = "Buscar Tarefas por id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TaskEntity.class))}
            )
    })
    public ResponseEntity<TaskEntity> findById(@PathVariable UUID id) {
        try {
            TaskEntity task = taskService.findById(id);

            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/task/{id}/add-item")
    @Operation(summary = "Adicionar Item a uma Tarefa")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = NewItemDTO.class))}
            )
    })
    public ResponseEntity<TaskEntity> addItem(@PathVariable UUID id, @RequestBody NewItemDTO item) {
        try {
            TaskEntity task = taskService.addItem(id, new ItemEntity(
                    item.title(),
                    item.description(),
                    item.priority(),
                    item.state()
            ));
            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/task/{id}/update-item")
    @Operation(summary = "Editar Item de uma Tarefa")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ItemEntity.class))}
            )
    })
    public ResponseEntity<TaskEntity> updateItem(@PathVariable UUID id, @RequestBody ItemEntity item) {
        try {
            TaskEntity task = taskService.updateItem(id, item);
            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/task/{id}/delete-item")
    @Operation(summary = "Deletar Item de uma Tarefa")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    public ResponseEntity<TaskEntity> deleteItem(@PathVariable UUID id, @RequestBody ItemEntity item) {
        try {
            TaskEntity task = taskService.removeItem(id, item);

            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/task/{id}/clear-items")
    @Operation(summary = "Deletar todos os Items de uma Tarefa")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ItemEntity.class))}
            )
    })
    public ResponseEntity<TaskEntity> clearItems(@PathVariable UUID id) {
        try {
            TaskEntity task = taskService.clearItems(id);

            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (TaskServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

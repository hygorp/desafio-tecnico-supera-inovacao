package org.hygorp.listmanager.repositories;

import org.hygorp.listmanager.entities.TaskEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Task Repository Test")
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void beforeEach() {
        taskRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("should save task")
    @Order(1)
    void shouldSaveTask() {
        TaskEntity task = Assertions.assertDoesNotThrow(() -> taskRepository.save(
                new TaskEntity(
                        "Dog's House",
                        "Build a new house for the dog before winter",
                        LocalDateTime.now().plusDays(15)
                )
        ));

        Assertions.assertNotNull(task);
        Assertions.assertEquals("Dog's House", task.getTitle());
        Assertions.assertNotNull(task.getCreatedAt());
        Assertions.assertNotNull(task.getCreatedAt());
        Assertions.assertNull(task.getUpdatedAt());
        Assertions.assertNotNull(task.getExpiresAt());
    }

    @Test
    @DisplayName("should update task")
    @Order(2)
    void shouldUpdateTask() {
        TaskEntity task = Assertions.assertDoesNotThrow(() -> taskRepository.save(
                new TaskEntity(
                        "Dog's House",
                        "Build a new house for the dog before winter",
                        LocalDateTime.now().plusDays(15)
                )
        ));

        task.setTitle("Dog's House Until Winter");

        TaskEntity updatedTask = taskRepository.save(task);

        Assertions.assertNotNull(updatedTask);
        Assertions.assertEquals("Dog's House Until Winter", updatedTask.getTitle());
        Assertions.assertNotNull(updatedTask.getCreatedAt());
        Assertions.assertNotNull(updatedTask.getUpdatedAt());
        Assertions.assertNotNull(updatedTask.getExpiresAt());
    }

    @Test
    @DisplayName("should delete task")
    @Order(3)
    void shouldDeleteTask() {
        TaskEntity task = Assertions.assertDoesNotThrow(() -> taskRepository.save(
                new TaskEntity(
                        "Dog's House",
                        "Build a new house for the dog before winter",
                        LocalDateTime.now().plusDays(15)
                )
        ));

        taskRepository.deleteById(task.getId());

        Assertions.assertThrows(NoSuchElementException.class, () -> taskRepository.findById(task.getId()).orElseThrow());
    }

    @Test
    @DisplayName("should find all tasks")
    @Order(4)
    void shouldFindAllTasks() {
        Assertions.assertDoesNotThrow(() -> taskRepository.saveAll(Arrays.asList(
                new TaskEntity(
                        "Dog's House",
                        "Build a new house for the dog before winter",
                        LocalDateTime.now().plusDays(15)
                ),
                new TaskEntity(
                        "School's Project",
                        "science fair presentation project, my group must make a replica of everest",
                        LocalDateTime.now().plusDays(20)
                )
        )));

        List<TaskEntity> tasks = taskRepository.findAll();

        Assertions.assertEquals(2, tasks.size());
    }

    @Test
    @DisplayName("should find task by id")
    @Order(5)
    void shouldFindTaskById() {
        TaskEntity task = Assertions.assertDoesNotThrow(() -> taskRepository.save(
                new TaskEntity(
                        "School's Project",
                        "science fair presentation project, my group must make a replica of everest",
                        LocalDateTime.now().plusDays(20)
                )
        ));

        TaskEntity taskFoundedById = taskRepository.findById(task.getId()).orElseThrow();

        Assertions.assertNotNull(taskFoundedById);
        Assertions.assertEquals(task, taskFoundedById);
        Assertions.assertEquals("School's Project", taskFoundedById.getTitle());
    }
}

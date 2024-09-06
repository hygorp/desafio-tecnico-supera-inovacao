package org.hygorp.listmanager.services;

import org.hygorp.listmanager.entities.ItemEntity;
import org.hygorp.listmanager.entities.TaskEntity;
import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;
import org.hygorp.listmanager.repositories.TaskRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Task Service Test")
public class TaskServiceTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;


    private UUID myTaskTestId01;
    private UUID myTaskTestId02;

    @BeforeEach
    void beforeEach() {
        taskRepository.deleteAll();

        myTaskTestId01 = taskRepository.save(new TaskEntity(
                "Complete the collection",
                "complete the leaf collection by the end of spring",
                LocalDate.now().plusDays(22)
        )).getId();

        myTaskTestId02 = taskRepository.save(new TaskEntity(
                "Website",
                "finalize grandpa’s workshop website",
                LocalDate.now().plusDays(7)
        )).getId();
    }

    @AfterEach
    void afterEach() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("should save task")
    @Order(1)
    void shouldSaveTask() {
        TaskEntity task = Assertions.assertDoesNotThrow(() -> taskService.save(
                new TaskEntity(
                        "Dog's House",
                        "Build a new house for the dog before winter",
                        LocalDate.now().plusDays(15)
                )
        ));

        Assertions.assertNotNull(task);
        Assertions.assertEquals("Dog's House", task.getTitle());
    }

    @Test
    @DisplayName("should update task")
    @Order(2)
    void shouldUpdateTask() {
        TaskEntity savedTask = Assertions.assertDoesNotThrow(() -> taskRepository.findById(myTaskTestId01)).orElseThrow();

        Assertions.assertNotNull(savedTask);

        savedTask.setTitle("Complete the leaf collection");

        TaskEntity updatedTask = Assertions.assertDoesNotThrow(() -> taskService.update(savedTask.getId(), savedTask));

        Assertions.assertEquals("Complete the leaf collection", updatedTask.getTitle());
        Assertions.assertEquals(savedTask.getDescription(), updatedTask.getDescription());
    }

    @Test
    @DisplayName("should add item to task")
    @Order(3)
    void shouldAddItemToTask() {
        TaskEntity savedTask = Assertions.assertDoesNotThrow(() -> taskRepository.findById(myTaskTestId01).orElseThrow());

        Assertions.assertNotNull(savedTask);

        ItemEntity item = new ItemEntity(
                "Buy magnifying glass",
                "go to the store on the weekend to buy",
                ItemPriorityEnum.Media,
                ItemStateEnum.Pendente
        );

        TaskEntity updatedTask = Assertions.assertDoesNotThrow(() -> taskService.addItem(savedTask.getId(), item));

        Assertions.assertNotNull(updatedTask);
        Assertions.assertEquals("Complete the collection", updatedTask.getTitle());
        Assertions.assertNotNull(updatedTask.getUpdatedAt());
        Assertions.assertEquals(1, updatedTask.getItems().size());
    }

    @Test
    @DisplayName("should update item from task")
    @Order(4)
    void shouldUpdateItemFromTask() {
        TaskEntity task = new TaskEntity(
                "Dog's House",
                "Build a new house for the dog before winter",
                LocalDate.now().plusDays(15)
        );

        task.addItem(
                new ItemEntity(
                        "Buy wood",
                        "go to the store on the weekend to buy",
                        ItemPriorityEnum.Media,
                        ItemStateEnum.Pendente
                )
        );

        TaskEntity savedTask = Assertions.assertDoesNotThrow(() -> taskRepository.save(task));
        Assertions.assertNotNull(savedTask);

        ItemEntity item = savedTask.getItems().iterator().next();

        item.setPriority(ItemPriorityEnum.Alta);
        item.setState(ItemStateEnum.Fazendo);

        TaskEntity updatedTask = Assertions.assertDoesNotThrow(() -> taskService.updateItem(savedTask.getId(), item));

        ItemEntity updatedItem = updatedTask.getItems().iterator().next();

        Assertions.assertEquals("Buy wood", updatedItem.getTitle());
        Assertions.assertEquals("Alta", updatedItem.getPriority().toString());
        Assertions.assertEquals("Fazendo", updatedItem.getState().toString());
    }

    @Test
    @DisplayName("should remove item to task")
    @Order(5)
    void shouldRemoveItemFromTask() {
        TaskEntity task = new TaskEntity(
                "Dog's House",
                "Build a new house for the dog before winter",
                LocalDate.now().plusDays(15)
        );

        task.addItem(new ItemEntity(
                "Buy Nails",
                "go to the store on the weekend to buy nails",
                ItemPriorityEnum.Media,
                ItemStateEnum.Pendente
        ));

        TaskEntity savedTask = Assertions.assertDoesNotThrow(() -> taskService.save(task));

        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(1, task.getItems().size());

        ItemEntity savedItem = savedTask.getItems().stream().findFirst().orElseThrow();

        TaskEntity updatedTask = Assertions.assertDoesNotThrow(() -> taskService.removeItem(savedTask.getId(), savedItem));

        Assertions.assertEquals(0, updatedTask.getItems().size());
    }

    @Test
    @DisplayName("should clear items from task")
    @Order(6)
    void shouldClearItemsFromTask() {
        TaskEntity task = new TaskEntity(
                "Dog's House",
                "Build a new house for the dog before winter",
                LocalDate.now().plusDays(15)
        );

        task.addItem(new ItemEntity(
                "Buy Woods",
                "Go to the lumber yard on the weekend to buy the necessary items",
                ItemPriorityEnum.Media,
                ItemStateEnum.Pendente
        ));

        task.addItem(new ItemEntity(
                "Buy Nails",
                "go to the store on the weekend to buy nails",
                ItemPriorityEnum.Media,
                ItemStateEnum.Pendente
        ));

        TaskEntity savedTask = Assertions.assertDoesNotThrow(() -> taskService.save(task));
        Assertions.assertEquals(2, task.getItems().size());

        Assertions.assertDoesNotThrow(() -> taskService.clearItems(savedTask.getId()));

        TaskEntity updatedTask = Assertions.assertDoesNotThrow(() -> taskService.clearItems(savedTask.getId()));

        Assertions.assertEquals(0, updatedTask.getItems().size());
    }

    @Test
    @DisplayName("should delete task and item in cascade")
    @Order(7)
    void shouldDeleteTaskAndItemInCascade() {
        TaskEntity task = Assertions.assertDoesNotThrow(() -> taskRepository.findById(myTaskTestId02).orElseThrow());

        task.addItem(new ItemEntity(
                "Buy Woods",
                "Go to the lumber yard on the weekend to buy the necessary items",
                ItemPriorityEnum.Media,
                ItemStateEnum.Pendente
        ));

        task.addItem(new ItemEntity(
                "Buy Nails",
                "go to the store on the weekend to buy nails",
                ItemPriorityEnum.Media,
                ItemStateEnum.Pendente
        ));

        TaskEntity savedTask = Assertions.assertDoesNotThrow(() -> taskService.save(task));

        Assertions.assertDoesNotThrow(() -> taskService.delete(savedTask.getId()));

        Assertions.assertThrows(NoSuchElementException.class, () -> taskRepository.findById(savedTask.getId()).orElseThrow());
    }

    @Test
    @DisplayName("should find all tasks")
    @Order(8)
    void shouldFindAllTasks() {
        Assertions.assertEquals(2, taskService.findAll().size());
    }

    @Test
    @DisplayName("should find task by id")
    @Order(9)
    void shouldFindTaskById() {
        TaskEntity taskFoundById = taskService.findById(myTaskTestId02);

        Assertions.assertNotNull(taskFoundById);
        Assertions.assertEquals("Website", taskFoundById.getTitle());
    }
}

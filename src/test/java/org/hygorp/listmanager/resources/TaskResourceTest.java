package org.hygorp.listmanager.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hygorp.listmanager.entities.ItemEntity;
import org.hygorp.listmanager.entities.TaskEntity;
import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;
import org.hygorp.listmanager.repositories.TaskRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Task Resource Test")
public class TaskResourceTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private TaskEntity myTaskTest01;
    private TaskEntity myTaskTest02;
    private TaskEntity myTaskTest03;

    @BeforeEach
    void beforeEach() {
        taskRepository.deleteAll();

        TaskEntity task01 = new TaskEntity(
                "Complete the collection",
                "complete the leaf collection by the end of spring",
                LocalDate.now().plusDays(22)
        );
        task01.addItem(new ItemEntity(
                "Buy magnifying glass",
                "go to the store on the weekend to buy",
                ItemPriorityEnum.Media,
                ItemStateEnum.Pendente
        ));
        myTaskTest01 = taskRepository.save(task01);

        TaskEntity task02 = new TaskEntity(
                "Dog's House",
                "Build a new house for the dog before winter",
                LocalDate.now().plusDays(15)
        );
        task02.addItem(new ItemEntity(
                "Buy Woods",
                "Go to the lumber yard on the weekend to buy the necessary items",
                ItemPriorityEnum.Media,
                ItemStateEnum.Pendente
        ));
        myTaskTest02 = taskRepository.save(task02);

        TaskEntity task03 = new TaskEntity(
                "Software Project",
                "Build a web application",
                LocalDate.now().plusDays(4)
        );
        task03.addItem(new ItemEntity(
                "Build Back-End",
                "Use Spring-Boot",
                ItemPriorityEnum.Alta,
                ItemStateEnum.Completo
        ));
        myTaskTest03 = taskRepository.save(task03);
    }

    @AfterEach
    void afterEach() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("should save task and return 201 status")
    @Order(1)
    void shouldSaveTaskAndReturn201Status() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/tasks/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskEntity(
                                "Website",
                                "finalize grandpa’s workshop website",
                                LocalDate.now().plusDays(7)
                        ))))
                .andExpect(status().isCreated()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals("Website", body.get("title").asText());
    }

    @Test
    @DisplayName("should update task and return 200 status")
    @Order(2)
    void shouldUpdateTaskAndReturn200Status() throws Exception {
        MvcResult result = mockMvc.perform(put("/api/v1/tasks/update/" + myTaskTest01.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskEntity(
                                "Complete the leaf collection",
                                "complete the leaf collection by the end of spring",
                                LocalDate.now().plusDays(22)
                        ))))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals("Complete the leaf collection", body.get("title").asText());
        Assertions.assertEquals("complete the leaf collection by the end of spring", body.get("description").asText());
    }

    @Test
    @DisplayName("should delete task and return 204 status")
    @Order(3)
    void shouldDeleteTaskAndReturn204Status() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/delete/" + myTaskTest02.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertThrows(NoSuchElementException.class, () -> taskRepository.findById(myTaskTest02.getId()).orElseThrow());
    }

    @Test
    @DisplayName("should find all tasks and return 200 status")
    @Order(4)
    void shouldFindAllTasksAndReturn200Status() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/tasks/find-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(3, body.size());
    }

    @Test
    @DisplayName("should find tasks by title and return 200 status")
    @Order(5)
    void shouldFindTasksByTitleAndReturn200Status() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/tasks/find-by-title")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", "Complete"))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(1, body.size());
    }

    @Test
    @DisplayName("should find all tasks by item priority and return 200 status")
    @Order(6)
    void shouldFindAllTasksByItemPriorityAndReturn200Status() throws Exception {
        MvcResult result = mockMvc.perform((get("/api/v1/tasks/find-all-by-items-priority"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("priority", "Alta"))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(1, body.size());
    }

    @Test
    @DisplayName("should find all tasks by item state and return 200 status")
    @Order(7)
    void shouldFindAllTasksByItemStateAndReturn200Status() throws Exception {
        MvcResult result = mockMvc.perform((get("/api/v1/tasks/find-all-by-items-state"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", "Completo"))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(1, body.size());
    }

    @Test
    @DisplayName("should find all tasks created at after date and return 200 status")
    @Order(8)
    void shouldFindAllTasksCreatedAtAfterDateAndReturn200Status() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/tasks/find-all-by-created-after")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("date", "01-09-2024"))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(3, body.size());
    }

    @Test
    @DisplayName("should find all tasks created at between dates and return 200 status")
    @Order(9)
    void shouldFindAllTasksCreatedAtBetweenDatesAndReturn200Status() throws Exception {
        myTaskTest03.setCreatedAt(LocalDate.of(2024, 1, 25));
        taskRepository.save(myTaskTest03);

        MvcResult result = mockMvc.perform(get("/api/v1/tasks/find-all-by-created-between")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("startDate", "01-01-2024")
                        .param("endDate", "01-02-2024"))
                        .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(1, body.size());
    }

    @Test
    @DisplayName("should find task by id and return 200 status")
    @Order(10)
    void shouldFindTaskByIdAndReturn200Status() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/tasks/find-by-id/" + myTaskTest01.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(myTaskTest01.getId().toString(), body.get("id").asText());
        Assertions.assertEquals("Complete the collection", body.get("title").asText());
    }

    @Test
    @DisplayName("should add item to task and return 200 status")
    @Order(11)
    void shouldAddItemToTaskAndReturn200Status() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/tasks/task/" + myTaskTest01.getId() + "/add-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ItemEntity(
                                "catalog",
                                "catalog harvested leaves",
                                ItemPriorityEnum.Alta,
                                ItemStateEnum.Fazendo
                        ))))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(2, body.get("items").size());
    }

    @Test
    @DisplayName("should update item from task and return 200 status")
    @Order(12)
    void shouldUpdateItemFromTaskAndReturn200Status() throws Exception {
        ItemEntity preUpdated = myTaskTest02.getItems().iterator().next();

        preUpdated.setTitle("Buy pine woods");
        preUpdated.setPriority(ItemPriorityEnum.Alta);
        preUpdated.setState(ItemStateEnum.Fazendo);

        MvcResult result = mockMvc.perform(put("/api/v1/tasks/task/" + myTaskTest02.getId() + "/update-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                preUpdated
                        )))
                .andExpect(status().isOk()).andReturn();

        JsonNode items = objectMapper.readTree(result.getResponse().getContentAsString()).get("items");

        ItemEntity updatedItem = objectMapper.treeToValue(items.get(0), ItemEntity.class);

        Assertions.assertEquals("Buy pine woods", updatedItem.getTitle());
        Assertions.assertEquals(ItemPriorityEnum.Alta, updatedItem.getPriority());
        Assertions.assertEquals(ItemStateEnum.Fazendo, updatedItem.getState());
    }

    @Test
    @DisplayName("should delete item from task and return 200 status")
    @Order(13)
    void shouldDeleteItemFromTaskAndReturn200Status() throws Exception {
        Assertions.assertEquals(1, myTaskTest02.getItems().size());

        ItemEntity item = myTaskTest02.getItems().iterator().next();

        MvcResult result = mockMvc.perform(delete("/api/v1/tasks/task/" + myTaskTest02.getId() + "/delete-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                item
                        )))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(0, body.get("items").size());
    }

    @Test
    @DisplayName("should clear items from task and return 200 status")
    @Order(14)
    void shouldClearItemsFromTaskAndReturn200Status() throws Exception {
        Assertions.assertEquals(1, myTaskTest02.getItems().size());

        MvcResult result = mockMvc.perform(delete("/api/v1/tasks/task/" + myTaskTest01.getId() + "/clear-items"))
                .andExpect(status().isOk()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(0, body.get("items").size());
    }
}

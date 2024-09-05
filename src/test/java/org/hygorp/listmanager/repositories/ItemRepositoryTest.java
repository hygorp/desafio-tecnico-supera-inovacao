package org.hygorp.listmanager.repositories;

import org.hygorp.listmanager.entities.ItemEntity;
import org.hygorp.listmanager.enums.ItemPriorityEnum;
import org.hygorp.listmanager.enums.ItemStateEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Item Repository Test")
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        itemRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("should save item")
    @Order(1)
    void shouldSaveItem() {
        ItemEntity item = Assertions.assertDoesNotThrow(() -> itemRepository.save(
                new ItemEntity(
                        "Buy Woods",
                        "Go to the lumber yard on the weekend to buy the necessary items",
                        ItemPriorityEnum.MEDIUM,
                        ItemStateEnum.TODO
                )
        ));

        Assertions.assertNotNull(item);
        Assertions.assertEquals("Buy Woods", item.getTitle());
        Assertions.assertEquals("MEDIUM", item.getPriority().toString());
        Assertions.assertEquals("TODO", item.getState().toString());

    }

    @Test
    @DisplayName("should update item")
    @Order(2)
    void shouldUpdateItem() {
        ItemEntity item = Assertions.assertDoesNotThrow(() -> itemRepository.save(
                new ItemEntity(
                        "Buy Woods",
                        "Go to the lumber yard on the weekend to buy the necessary items",
                        ItemPriorityEnum.MEDIUM,
                        ItemStateEnum.TODO
                )
        ));

        item.setPriority(ItemPriorityEnum.HIGH);

        ItemEntity updatedItem = itemRepository.save(item);

        Assertions.assertNotNull(updatedItem);
        Assertions.assertEquals("HIGH", updatedItem.getPriority().toString());
    }

    @Test
    @DisplayName("should delete item")
    @Order(3)
    void shouldDeleteItem() {
        ItemEntity item = Assertions.assertDoesNotThrow(() -> itemRepository.save(
                new ItemEntity(
                        "Buy Woods",
                        "Go to the lumber yard on the weekend to buy the necessary items",
                        ItemPriorityEnum.MEDIUM,
                        ItemStateEnum.TODO
                )
        ));

        itemRepository.deleteById(item.getId());

        Assertions.assertThrows(NoSuchElementException.class, () -> itemRepository.findById(item.getId()).orElseThrow());
    }

    @Test
    @DisplayName("should find all items")
    @Order(4)
    void shouldFindAllItems() {
        Assertions.assertDoesNotThrow(() -> itemRepository.saveAll(Arrays.asList(
                new ItemEntity(
                        "Buy Woods",
                        "Go to the lumber yard on the weekend to buy the necessary items",
                        ItemPriorityEnum.MEDIUM,
                        ItemStateEnum.TODO
                ),
                new ItemEntity(
                        "Buy Woods",
                        "Go to the lumber yard on the weekend to buy the necessary items",
                        ItemPriorityEnum.MEDIUM,
                        ItemStateEnum.TODO
                )
        )));

        List<ItemEntity> items = itemRepository.findAll();

        Assertions.assertEquals(2, items.size());
    }

    @Test
    @DisplayName("should find item by id")
    @Order(5)
    void shouldFindItemById() {
        ItemEntity item = Assertions.assertDoesNotThrow(() -> itemRepository.save(
                new ItemEntity(
                        "Buy Woods",
                        "Go to the lumber yard on the weekend to buy the necessary items",
                        ItemPriorityEnum.MEDIUM,
                        ItemStateEnum.TODO
                )
        ));

        ItemEntity itemFoundedById = itemRepository.findById(item.getId()).orElseThrow();

        Assertions.assertNotNull(itemFoundedById);
        Assertions.assertEquals(item, itemFoundedById);
        Assertions.assertEquals("Buy Woods", itemFoundedById.getTitle());
    }
}

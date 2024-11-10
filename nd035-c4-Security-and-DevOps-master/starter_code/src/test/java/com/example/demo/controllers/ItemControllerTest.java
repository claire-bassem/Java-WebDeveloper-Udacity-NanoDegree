package com.example.demo.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_all_items() {
        List<Item> items = new ArrayList<>();
        items.add(createTestItem(1L, "Item1"));
        items.add(createTestItem(2L, "Item2"));

        when(itemRepository.findAll()).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void get_item_by_id_found() {
        Item item = createTestItem(1L, "Item1");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(item.getId(), response.getBody().getId());
        assertEquals(item.getName(), response.getBody().getName());
    }

    @Test
    public void get_item_by_id_not_found() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name_found() {
        List<Item> items = new ArrayList<>();
        items.add(createTestItem(1L, "Item1"));
        items.add(createTestItem(2L, "Item1"));

        when(itemRepository.findByName("Item1")).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Item1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Item1", response.getBody().get(0).getName());
    }

    @Test
    public void get_items_by_name_not_found() {
        when(itemRepository.findByName("NonExistentItem")).thenReturn(new ArrayList<>());

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("NonExistentItem");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private Item createTestItem(Long id, String name) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(BigDecimal.valueOf(10.00));
        item.setDescription("Test description");
        return item;
    }
}

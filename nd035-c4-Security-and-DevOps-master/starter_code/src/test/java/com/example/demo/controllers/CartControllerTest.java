package com.example.demo.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart_user_and_item_exist() {
        User user = createTestUser();
        Item item = createTestItem();
        ModifyCartRequest request = createCartRequest(user.getUsername(), item.getId(), 2);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(2, cart.getItems().size());
        assertEquals(item.getPrice().multiply(BigDecimal.valueOf(2)), cart.getTotal());
    }

    @Test
    public void add_to_cart_user_not_found() {
        ModifyCartRequest request = createCartRequest("nonexistentUser", 1L, 2);

        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_item_not_found() {
        User user = createTestUser();
        ModifyCartRequest request = createCartRequest(user.getUsername(), 99L, 2);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_user_and_item_exist() {
        User user = createTestUser();
        Item item = createTestItem();
        user.getCart().addItem(item);
        user.getCart().addItem(item);
        ModifyCartRequest request = createCartRequest(user.getUsername(), item.getId(), 1);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals(item.getPrice(), cart.getTotal());
    }

    @Test
    public void remove_from_cart_user_not_found() {
        ModifyCartRequest request = createCartRequest("nonexistentUser", 1L, 2);

        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_item_not_found() {
        User user = createTestUser();
        ModifyCartRequest request = createCartRequest(user.getUsername(), 99L, 2);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setCart(new Cart());
        return user;
    }

    private Item createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(10.00));
        return item;
    }

    private ModifyCartRequest createCartRequest(String username, Long itemId, int quantity) {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(itemId);
        request.setQuantity(quantity);
        return request;
    }
}

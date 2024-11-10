package com.example.demo.controllers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    // @Test
    // public void submit_order_for_existing_user() {
    //     User user = createTestUser("testUser");
    //     Cart cart = createTestCart();
    //     user.setCart(cart);

    //     when(userRepository.findByUsername("testUser")).thenReturn(user);

    //     final ResponseEntity<UserOrder> response = orderController.submit("testUser");

    //     assertNotNull(response);
    //     assertEquals(200, response.getStatusCodeValue());
    //     UserOrder order = response.getBody();
    //     assertNotNull(order);
    //     assertEquals(user.getCart().getTotal(), order.getTotal());
    //     verify(orderRepository, times(1)).save(order);
    // }

    @Test
    public void submit_order_for_nonexistent_user() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        final ResponseEntity<UserOrder> response = orderController.submit("nonexistentUser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(orderRepository, times(0)).save(any());
    }

    // @Test
    // public void get_order_history_for_existing_user() {
    //     User user = createTestUser("testUser");
    //     List<UserOrder> orders = new ArrayList<>();
    //     orders.add(UserOrder.createFromCart(createTestCart()));

    //     when(userRepository.findByUsername("testUser")).thenReturn(user);
    //     when(orderRepository.findByUser(user)).thenReturn(orders);

    //     final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");

    //     assertNotNull(response);
    //     assertEquals(200, response.getStatusCodeValue());
    //     assertEquals(1, response.getBody().size());
    // }

    @Test
    public void get_order_history_for_nonexistent_user() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("nonexistentUser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(orderRepository, times(0)).findByUser(any());
    }

    private User createTestUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setId(1L);
        return user;
    }

    private Cart createTestCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(BigDecimal.valueOf(100.00));
        return cart;
    }
}

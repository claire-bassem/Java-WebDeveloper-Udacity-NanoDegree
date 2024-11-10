package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

    }

    @Test
    public void create_user_happy_path() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
public void create_user_password_too_short() {
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("test");
    request.setPassword("short");
    request.setConfirmPassword("short");

    final ResponseEntity<User> response = userController.createUser(request);
    assertNotNull(response);
    assertEquals(400, response.getStatusCodeValue());
}

@Test
public void create_user_passwords_do_not_match() {
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("test");
    request.setPassword("password");
    request.setConfirmPassword("differentPassword");

    final ResponseEntity<User> response = userController.createUser(request);
    assertNotNull(response);
    assertEquals(400, response.getStatusCodeValue());
}

@Test
public void find_user_by_username_happy_path() {
    User user = new User();
    user.setId(1L);
    user.setUsername("testUser");
    when(userRepo.findByUsername("testUser")).thenReturn(user);

    final ResponseEntity<User> response = userController.findByUserName("testUser");
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("testUser", response.getBody().getUsername());
}

@Test
public void find_user_by_username_not_found() {
    when(userRepo.findByUsername("nonexistentUser")).thenReturn(null);

    final ResponseEntity<User> response = userController.findByUserName("nonexistentUser");
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
}

// @Test
// public void find_user_by_id_happy_path() {
//     User user = new User();
//     user.setId(1L);
//     when(userRepo.findById(1L)).thenReturn(Optional.of(user));

//     final ResponseEntity<User> response = userController.findById(1L);
//     assertNotNull(response);
//     assertEquals(200, response.getStatusCodeValue());
//     assertEquals(1L, response.getBody().getId().longValue());
// }

// @Test
// public void find_user_by_id_not_found() {
//     when(userRepo.findById(2L)).thenReturn(Optional.empty());

//     final ResponseEntity<User> response = userController.findById(2L);
//     assertNotNull(response);
//     assertEquals(404, response.getStatusCodeValue());
// }

}
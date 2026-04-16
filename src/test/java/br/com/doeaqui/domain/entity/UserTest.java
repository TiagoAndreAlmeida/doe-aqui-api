package br.com.doeaqui.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("Should create user with default constructor and initial state")
    void shouldCreateUserWithDefaultConstructor() {
        User user = new User();
        
        assertNotNull(user);
        assertFalse(user.getInactive());
        assertNotNull(user.getDonatedProducts());
        assertTrue(user.getDonatedProducts().isEmpty());
        assertNotNull(user.getReceivedProducts());
        assertTrue(user.getReceivedProducts().isEmpty());
        assertEquals("", user.getPhone());
    }

    @Test
    @DisplayName("Should create user with all-args constructor")
    void shouldCreateUserWithAllArgsConstructor() {
        Long id = 1L;
        Boolean inactive = true;
        String email = "test@example.com";
        String name = "Test User";
        String password = "password123";
        String phone = "123456789";
        List<Product> donated = new ArrayList<>();
        List<Product> received = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(id, inactive, email, name, password, phone, donated, received, now, now);

        assertEquals(id, user.getId());
        assertEquals(inactive, user.getInactive());
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
        assertEquals(password, user.getPassword());
        assertEquals(phone, user.getPhone());
        assertEquals(donated, user.getDonatedProducts());
        assertEquals(received, user.getReceivedProducts());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test all getters and setters")
    void shouldTestGettersAndSetters() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = new ArrayList<>();

        user.setId(1L);
        user.setInactive(true);
        user.setEmail("test@example.com");
        user.setName("Name");
        user.setPassword("pass");
        user.setPhone("9999");
        user.setDonatedProducts(products);
        user.setReceivedProducts(products);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertTrue(user.getInactive());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Name", user.getName());
        assertEquals("pass", user.getPassword());
        assertEquals("9999", user.getPhone());
        assertEquals(products, user.getDonatedProducts());
        assertEquals(products, user.getReceivedProducts());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }
}

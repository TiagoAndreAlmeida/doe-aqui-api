package br.com.doeaqui.infrastructure.gateways.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;

class UserEntityMapperTest {

    private UserEntityMapper userEntityMapper;

    @BeforeEach
    void setUp() {
        userEntityMapper = new UserEntityMapper();
    }

    @Test
    @DisplayName("Should map User domain to UserEntity correctly")
    void shouldMapDomainToEntity() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("secret");
        user.setPhone("12345");
        user.setInactive(false);

        UserEntity entity = userEntityMapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getName(), entity.getName());
        assertEquals(user.getEmail(), entity.getEmail());
        assertEquals(user.getPassword(), entity.getPassword());
        assertEquals(user.getPhone(), entity.getPhone());
        assertEquals(user.getInactive(), entity.getInactive());
    }

    @Test
    @DisplayName("Should map UserEntity to User domain with empty product lists")
    void shouldMapEntityToDomain() {
        LocalDateTime now = LocalDateTime.now();
        UserEntity entity = new UserEntity(
            1L, "Test User", "test@example.com", "12345", "secret", false
        );
        // CreatedAt and UpdatedAt are normally set by JPA, but we can assume they are present
        // Or we might need to use reflection if they are private and only managed by Hibernate,
        // but for unit tests, let's assume we can get them.

        User user = userEntityMapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getName(), user.getName());
        assertEquals(entity.getEmail(), user.getEmail());
        assertEquals(entity.getInactive(), user.getInactive());
        assertEquals(entity.getPhone(), user.getPhone());
        
        // Key verification for our empty list strategy
        assertNotNull(user.getDonatedProducts());
        assertTrue(user.getDonatedProducts().isEmpty());
        assertNotNull(user.getReceivedProducts());
        assertTrue(user.getReceivedProducts().isEmpty());
    }
}

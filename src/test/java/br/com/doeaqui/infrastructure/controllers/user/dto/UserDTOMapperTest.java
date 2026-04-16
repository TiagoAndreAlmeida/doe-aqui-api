package br.com.doeaqui.infrastructure.controllers.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.doeaqui.domain.entity.User;

class UserDTOMapperTest {

    private UserDTOMapper userDTOMapper;

    @BeforeEach
    void setUp() {
        userDTOMapper = new UserDTOMapper();
    }

    @Test
    @DisplayName("Should map CreateUserRequest to User domain")
    void shouldMapRequestToDomain() {
        CreateUserRequest request = new CreateUserRequest("Test", "test@email.com", "123", "pass123");

        User result = userDTOMapper.toDomain(request);

        assertNotNull(result);
        assertEquals(request.name(), result.getName());
        assertEquals(request.email(), result.getEmail());
        assertEquals(request.phone(), result.getPhone());
        assertEquals(request.password(), result.getPassword());
    }

    @Test
    @DisplayName("Should map User domain to CreateUserResponse")
    void shouldMapDomainToResponse() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@email.com");
        user.setPhone("123");
        user.setInactive(false);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        CreateUserResponse response = userDTOMapper.toResponse(user);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getPhone(), response.phone());
        assertEquals(user.getInactive(), response.inactive());
        assertEquals(user.getCreatedAt(), response.createdAt());
        assertEquals(user.getUpdatedAt(), response.updatedAt());
    }
}

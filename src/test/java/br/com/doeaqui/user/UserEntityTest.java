package br.com.doeaqui.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserEntityTest {

    @Test
    @DisplayName("Deve instânciar um usuário corretamente via construtor")
    void shouldCreateUserWithConstructor() {
        UserEntity userEntity = new UserEntity(1L, "Tiago Teste", "tiago@teste.com", "");
        assertInstanceOf(UserEntity.class, userEntity);
        assertEquals(1L, userEntity.getId());
        assertEquals("Tiago Teste", userEntity.getName());
        assertEquals("tiago@teste.com", userEntity.getEmail());
        assertEquals("", userEntity.getPhone());
    }

    @Test
    @DisplayName("Deve alterar os dados do usuário corretamente via setters")
    void shouldUpdateUserDataViaSetters() {
        UserEntity userEntity = new UserEntity(null, "", "", "");

        userEntity.setId(2L);
        userEntity.setName("Novo Nome");
        userEntity.setEmail("novo@email.com");
        userEntity.setPhone("11988888888");

        assertEquals(2L, userEntity.getId());
        assertEquals("Novo Nome", userEntity.getName());
        assertEquals("novo@email.com", userEntity.getEmail());
        assertEquals("11988888888", userEntity.getPhone());
    }
}

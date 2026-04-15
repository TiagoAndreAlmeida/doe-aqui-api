package br.com.doeaqui.infrastructure.controllers.user.dto;

import br.com.doeaqui.domain.entity.User;

public class UserDTOMapper {
    public CreateUserResponse toResponse(User user) {
        return new CreateUserResponse(
            user.getId(), user.getName(), user.getEmail(),
            user.getPhone(), user.getInactive(), user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }

    public User toDomain(CreateUserRequest createUserRequest) {
        User userDomain = new User();
        userDomain.setName(createUserRequest.name());
        userDomain.setEmail(createUserRequest.email());
        userDomain.setPhone(createUserRequest.phone());
        userDomain.setPassword(createUserRequest.password());
        return userDomain;
    }
}

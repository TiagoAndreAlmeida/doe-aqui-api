package br.com.doeaqui.infrastructure.gateways.user;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;

@Component
public class UserEntityMapper {
    public UserEntity toEntity(User userDomain) {
        return new UserEntity(
            userDomain.getId(),
            userDomain.getName(), userDomain.getEmail(),
            userDomain.getPhone(), userDomain.getPassword(),
            userDomain.getInactive()
        );
    }

    public User toDomain(UserEntity userEntity) {
        return new User(
            userEntity.getId(), userEntity.getInactive(),
            userEntity.getEmail(), userEntity.getName(),
            userEntity.getPassword(), userEntity.getPhone(),
            new ArrayList<>(), new ArrayList<>(),
            userEntity.getCreatedAt(), userEntity.getUpdatedAt()
        );
    }
}

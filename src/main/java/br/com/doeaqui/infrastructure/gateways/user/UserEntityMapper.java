package br.com.doeaqui.infrastructure.gateways.user;

import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;

public class UserEntityMapper {
    UserEntity toEntity(User userDomain) {
        return new UserEntity(
            userDomain.getId(),
            userDomain.getName(), userDomain.getEmail(),
            userDomain.getPhone(), userDomain.getPassword(),
            userDomain.getInactive()
        );
    }

    User toDomain(UserEntity userEntity) {
        return new User(
            userEntity.getId(), userEntity.getInactive(),
            userEntity.getEmail(), userEntity.getName(),
            userEntity.getPassword(), userEntity.getPhone(),
            userEntity.getDonatedProducts(), userEntity.getReceivedProducts(),
            userEntity.getCreatedAt(), userEntity.getUpdatedAt()
        );
    }
}

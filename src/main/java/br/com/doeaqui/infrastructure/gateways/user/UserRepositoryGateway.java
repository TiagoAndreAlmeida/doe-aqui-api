package br.com.doeaqui.infrastructure.gateways.user;

import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;
import br.com.doeaqui.infrastructure.persistence.user.UserRepository;

public class UserRepositoryGateway implements UserGateway {
    private UserRepository userRepository;
    private UserEntityMapper userEntityMapper;

    public UserRepositoryGateway(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User createUser(User userDomain) {
        UserEntity userEntity = this.userEntityMapper.toEntity(userDomain);
        UserEntity saved = userRepository.save(userEntity);
        return this.userEntityMapper.toDomain(saved);
    }

}

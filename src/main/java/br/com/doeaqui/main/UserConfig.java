package br.com.doeaqui.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.application.usecases.user.CreateUserInteractor;
import br.com.doeaqui.infrastructure.controllers.user.dto.UserDTOMapper;
import br.com.doeaqui.infrastructure.gateways.user.UserEntityMapper;
import br.com.doeaqui.infrastructure.gateways.user.UserRepositoryGateway;
import br.com.doeaqui.infrastructure.persistence.user.UserRepository;

@Configuration
public class UserConfig {
    @Bean
    CreateUserInteractor createUserCase(UserGateway userGateway) {
        return new CreateUserInteractor(userGateway);
    }

    @Bean
    UserGateway userGateway(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        return new UserRepositoryGateway(userRepository, userEntityMapper);
    }

    @Bean
    UserEntityMapper userEntityMapper() {   
        return new UserEntityMapper();
    }

    @Bean
    UserDTOMapper userDTOMapper() {
        return new UserDTOMapper();
    }
}

package br.com.doeaqui.main;

import br.com.doeaqui.application.gateways.password.PasswordEncoderGateway;
import br.com.doeaqui.application.gateways.token.TokenGateway;
import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.application.usecases.auth.AuthenticateUserInteractor;
import br.com.doeaqui.application.usecases.user.CreateUserInteractor;
import br.com.doeaqui.application.usecases.user.GetUserByEmailInteractor;
import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.infrastructure.controllers.user.dto.UserDTOMapper;
import br.com.doeaqui.infrastructure.gateways.password.Argon2PasswordEncoderGateway;
import br.com.doeaqui.infrastructure.gateways.token.JwtTokenGateway;
import br.com.doeaqui.infrastructure.gateways.user.UserEntityMapper;
import br.com.doeaqui.infrastructure.gateways.user.UserRepositoryGateway;
import br.com.doeaqui.infrastructure.persistence.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {
    @Bean
    CreateUserInteractor createUserInteractor(UserGateway userGateway, PasswordEncoderGateway passwordEncoderGateway) {
        return new CreateUserInteractor(userGateway, passwordEncoderGateway);
    }

    @Bean
    AuthenticateUserInteractor authenticateUserInteractor(UserGateway userGateway, PasswordEncoderGateway passwordEncoderGateway, TokenGateway tokenGateway) {
        return new AuthenticateUserInteractor(userGateway, passwordEncoderGateway, tokenGateway);
    }

    @Bean
    GetUserByEmailInteractor getUserByEmailInteractor(UserGateway userGateway) {
        return new GetUserByEmailInteractor(userGateway);
    }

    @Bean
    UserGateway userGateway(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        return new UserRepositoryGateway(userRepository, userEntityMapper);
    }

    @Bean
    PasswordEncoderGateway passwordEncoderGateway(PasswordEncoder passwordEncoder) {
        return new Argon2PasswordEncoderGateway(passwordEncoder);
    }

    @Bean
    TokenGateway tokenGateway(JwtService jwtService) {
        return new JwtTokenGateway(jwtService);
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

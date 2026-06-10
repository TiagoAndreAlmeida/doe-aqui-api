package br.com.doeaqui.main;

import br.com.doeaqui.application.gateways.category.CategoryGateway;
import br.com.doeaqui.application.gateways.password.PasswordEncoderGateway;
import br.com.doeaqui.application.gateways.product.ProductGateway;
import br.com.doeaqui.application.gateways.token.TokenGateway;
import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.application.usecases.auth.AuthenticateUserInteractor;
import br.com.doeaqui.application.usecases.category.CreateCategoryInteractor;
import br.com.doeaqui.application.usecases.category.ListCategoryInteractor;
import br.com.doeaqui.application.usecases.product.CreateProductInteractor;
import br.com.doeaqui.application.usecases.user.CreateUserInteractor;
import br.com.doeaqui.application.usecases.user.GetUserByEmailInteractor;
import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.domain.service.SlugGenerator;
import br.com.doeaqui.infrastructure.controllers.user.dto.UserDTOMapper;
import br.com.doeaqui.infrastructure.gateways.category.CategoryEntityMapper;
import br.com.doeaqui.infrastructure.gateways.category.CategoryRepositoryGateway;
import br.com.doeaqui.infrastructure.gateways.password.Argon2PasswordEncoderGateway;
import br.com.doeaqui.infrastructure.gateways.product.ProductEntityMapper;
import br.com.doeaqui.infrastructure.gateways.product.ProductRepositoryGateway;
import br.com.doeaqui.infrastructure.gateways.token.JwtTokenGateway;
import br.com.doeaqui.infrastructure.gateways.user.UserEntityMapper;
import br.com.doeaqui.infrastructure.gateways.user.UserRepositoryGateway;
import br.com.doeaqui.infrastructure.persistence.category.CategoryRepository;
import br.com.doeaqui.infrastructure.persistence.product.ProductRepository;
import br.com.doeaqui.infrastructure.persistence.user.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {

    // --- User Beans ---
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
    UserDTOMapper userDTOMapper() {
        return new UserDTOMapper();
    }

    // --- Product Beans ---
    @Bean
    CreateProductInteractor createProductInteractor(ProductGateway productGateway, UserGateway userGateway) {
        return new CreateProductInteractor(productGateway, userGateway);
    }

    @Bean
    ProductGateway productGateway(ProductRepository productRepository, ProductEntityMapper productEntityMapper) {
        return new ProductRepositoryGateway(productRepository, productEntityMapper);
    }

    @Bean
    CreateCategoryInteractor createCategoryInteractor(SlugGenerator slugGenerator, CategoryGateway categoryGateway) {
        return new CreateCategoryInteractor(slugGenerator, categoryGateway);
    }

    @Bean
    ListCategoryInteractor listCategoryInteractor(CategoryGateway categoryGateway) {
        return new ListCategoryInteractor(categoryGateway);
    }

    @Bean
    CategoryGateway categoryGateway(CategoryEntityMapper categoryEntityMapper, CategoryRepository categoryRepository) {
        return new CategoryRepositoryGateway(categoryEntityMapper, categoryRepository);
    }
}

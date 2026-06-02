package br.com.doeaqui.user;

import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
import br.com.doeaqui.infrastructure.controllers.user.dto.CreateUserRequest;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;
import br.com.doeaqui.infrastructure.persistence.user.UserRepository;
import br.com.doeaqui.user.dto.request.LoginRequest;
import br.com.doeaqui.user.dto.response.LoginResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public LoginResponse authenticate(LoginRequest request) {
        UserEntity user;
        try {
            user = findByEmail(request.email());
        } catch (BusinessException e) {
            // Se o e-mail não existir, tratamos como erro de credenciais (401) por segurança
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }

        if (user.getInactive()) {
            throw new BusinessException("Conta inátiva, valide sua conta ou entre em contado com o suporte", ErrorCode.FORBIDDEN);
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }

        String token = "TODO";//jwtService.generateToken(user);
        return new LoginResponse(token);
    }

    @Transactional(readOnly = true)
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("Usuário não encontrado", ErrorCode.NOT_FOUND));
    }

    @Transactional
    public UserEntity create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado", ErrorCode.ALREADY_EXISTS);
        }

        String hashPassword = passwordEncoder.encode(request.password());

        UserEntity user = new UserEntity(
            null,
            request.name(),
            request.email(),
            request.phone() == null ? "" : request.phone(),
            hashPassword,
            true
        );

        return userRepository.save(user);
    }
}

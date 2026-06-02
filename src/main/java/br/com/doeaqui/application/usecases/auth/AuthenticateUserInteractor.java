package br.com.doeaqui.application.usecases.auth;

import br.com.doeaqui.application.gateways.password.PasswordEncoderGateway;
import br.com.doeaqui.application.gateways.token.TokenGateway;
import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;

public class AuthenticateUserInteractor {
    private final UserGateway userGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;
    private final TokenGateway tokenGateway;

    public AuthenticateUserInteractor(UserGateway userGateway, PasswordEncoderGateway passwordEncoderGateway, TokenGateway tokenGateway) {
        this.userGateway = userGateway;
        this.passwordEncoderGateway = passwordEncoderGateway;
        this.tokenGateway = tokenGateway;
    }

    public String authenticate(String email, String password) {
        User user;
        try {
            user = userGateway.findByEmail(email);
        } catch (Exception e) {
            throw new BusinessException("E-mail ou Senha Inválidos", ErrorCode.UNAUTHORIZED);
        }

        if (user.getInactive()) {
            throw new BusinessException("Usuário inativo", ErrorCode.FORBIDDEN);
        }

        if (!passwordEncoderGateway.matches(password, user.getPassword())) {
            throw new BusinessException("E-mail ou senha inválidos", ErrorCode.UNAUTHORIZED);
        }

        String token = tokenGateway.generateToken(user);
        return token;
    }

}

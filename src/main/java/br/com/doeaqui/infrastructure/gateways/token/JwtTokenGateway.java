package br.com.doeaqui.infrastructure.gateways.token;

import br.com.doeaqui.application.gateways.token.TokenGateway;
import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.domain.entity.User;

public class JwtTokenGateway implements TokenGateway {
    private final JwtService jwtService;

    public JwtTokenGateway(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String generateToken(User user) {
        return jwtService.generateToken(user.getId(), user.getEmail(), user.getName());
    }

}

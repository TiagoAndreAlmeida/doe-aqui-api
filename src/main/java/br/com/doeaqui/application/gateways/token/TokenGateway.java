package br.com.doeaqui.application.gateways.token;

import br.com.doeaqui.domain.entity.User;

public interface TokenGateway {
    String generateToken(User user);
}

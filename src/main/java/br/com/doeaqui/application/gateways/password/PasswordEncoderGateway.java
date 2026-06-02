package br.com.doeaqui.application.gateways.password;

public interface PasswordEncoderGateway {
    String encode(String password);
    boolean matches(String RawPassword, String hashPassword);
}

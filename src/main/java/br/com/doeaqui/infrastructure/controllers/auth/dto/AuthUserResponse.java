package br.com.doeaqui.infrastructure.controllers.auth.dto;

public record AuthUserResponse(
    String token,
    String type
) {}

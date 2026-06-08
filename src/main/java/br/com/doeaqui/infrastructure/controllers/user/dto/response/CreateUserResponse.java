package br.com.doeaqui.infrastructure.controllers.user.dto.response;

import java.time.LocalDateTime;

public record CreateUserResponse(
    Long id,
    String name,
    String email,
    String phone,
    Boolean inactive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

package br.com.doeaqui.user.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String name,
    String email,
    String phone,
    Boolean inactive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

package br.com.doeaqui.category.dto.response;

import java.time.LocalDateTime;

public record CategorySummaryResponse(
    Long id,
    String name,
    String slug,
    LocalDateTime createdAt
) {}

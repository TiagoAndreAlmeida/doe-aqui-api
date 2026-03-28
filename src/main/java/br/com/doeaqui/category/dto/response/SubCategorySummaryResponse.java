package br.com.doeaqui.category.dto.response;

import java.time.LocalDateTime;

public record SubCategorySummaryResponse(
    Long id,
    String name,
    String slug,
    LocalDateTime createdAt
) {}

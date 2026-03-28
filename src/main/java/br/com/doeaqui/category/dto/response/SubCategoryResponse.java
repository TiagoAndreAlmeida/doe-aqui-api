package br.com.doeaqui.category.dto.response;

import java.time.LocalDateTime;

public record SubCategoryResponse(
    Long id,
    String name,
    String slug,
    String categoryName,
    String categorySlug,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

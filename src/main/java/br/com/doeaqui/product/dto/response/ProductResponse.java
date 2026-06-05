package br.com.doeaqui.product.dto.response;

import java.time.LocalDateTime;

import br.com.doeaqui.domain.enums.ConditionStatus;
import br.com.doeaqui.domain.enums.DonationStatus;
import br.com.doeaqui.infrastructure.controllers.user.dto.UserSummaryResponse;

public record ProductResponse(
    Long id,
    String title,
    String description,
    ConditionStatus condition,
    DonationStatus status,
    UserSummaryResponse donor,
    LocalDateTime createdAt
) {}

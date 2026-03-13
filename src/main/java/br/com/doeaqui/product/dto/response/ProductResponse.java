package br.com.doeaqui.product.dto.response;

import java.time.LocalDateTime;

import br.com.doeaqui.product.enums.ConditionStatus;
import br.com.doeaqui.product.enums.DonationStatus;
import br.com.doeaqui.user.dto.response.UserSummaryResponse;

public record ProductResponse(
    Long id,
    String title,
    String description,
    ConditionStatus condition,
    DonationStatus status,
    UserSummaryResponse donor,
    LocalDateTime createdAt
) {}

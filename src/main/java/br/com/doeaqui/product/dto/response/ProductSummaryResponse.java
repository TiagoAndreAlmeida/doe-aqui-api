package br.com.doeaqui.product.dto.response;

import br.com.doeaqui.product.enums.ConditionStatus;
import br.com.doeaqui.product.enums.DonationStatus;

public record ProductSummaryResponse(
    Long id,
    String title,
    ConditionStatus condition,
    DonationStatus status
) {}

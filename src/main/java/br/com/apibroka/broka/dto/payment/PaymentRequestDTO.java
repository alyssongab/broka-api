package br.com.apibroka.broka.dto.payment;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequestDTO(
   UUID orderId, BigDecimal value
) {}

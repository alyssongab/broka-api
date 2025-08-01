package br.com.apibroka.broka.dto.payment;

import java.util.UUID;

public record PaymentResponseDTO(
    UUID orderId, String paymentLink
) {}

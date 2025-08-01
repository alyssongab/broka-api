package br.com.apibroka.broka.dto.payment;

import java.util.UUID;

public record PaymentWebhookDTO(
    UUID order, String status
) {}

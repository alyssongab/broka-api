package br.com.apibroka.broka.dto.order;

import java.util.UUID;

public record OrderItemRequestDTO(UUID productId, int quantity) {
}

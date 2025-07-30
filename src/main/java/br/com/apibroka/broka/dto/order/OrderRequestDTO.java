package br.com.apibroka.broka.dto.order;

import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(
   UUID restaurantId,
   List<OrderItemRequestDTO> items
) {}

package br.com.apibroka.broka.dto.restaurant;

import java.util.UUID;

public record RestaurantDTO(UUID id, String name, String address, String cuisineType) {}

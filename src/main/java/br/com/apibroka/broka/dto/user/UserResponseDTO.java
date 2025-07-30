package br.com.apibroka.broka.dto.user;

import br.com.apibroka.broka.enums.UserRole;

import java.util.UUID;

public record UserResponseDTO(
   UUID id,
   String name,
   String email,
   UserRole role
) {}

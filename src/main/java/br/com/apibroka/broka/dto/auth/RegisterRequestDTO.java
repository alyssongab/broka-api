package br.com.apibroka.broka.dto.auth;

import br.com.apibroka.broka.enums.UserRole;

public record RegisterRequestDTO(
    String name,
    String email,
    String password,
    UserRole role
) {
}

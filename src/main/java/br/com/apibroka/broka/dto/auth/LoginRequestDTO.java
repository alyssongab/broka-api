package br.com.apibroka.broka.dto.auth;

public record LoginRequestDTO(
    String email,
    String password
) {
}

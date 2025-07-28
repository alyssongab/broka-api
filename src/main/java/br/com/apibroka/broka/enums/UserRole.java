package br.com.apibroka.broka.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin"),
    CLIENTE("cliente"),
    DONO_RESTAURANTE("dono_restaurante");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}

package br.com.apibroka.broka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            // desabilita o csrf ja que nao usarei sessao / cookies
                .csrf(csrf-> csrf.disable())

            // regras de autorizacao
                .authorizeHttpRequests(auth -> auth
                // permite acesso a todos os endpoints que comecem com /api/test)
                .requestMatchers("/api/test").permitAll()
                // exige autenticacao para todos os outros endpoints
                .anyRequest().authenticated()
                );
        return http.build();
    }
}

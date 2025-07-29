package br.com.apibroka.broka.service;

import br.com.apibroka.broka.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.jwt.secret}")
    private String secret;

    public String gerarToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("api-broka")
                    .withSubject(user.getEmail())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);
        }
        catch(JWTCreationException e){
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String validarToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("api-broka")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch(JWTVerificationException e){
            return "";
        }
    }

    public Instant gerarDataExpiracao(){
        // expirar o token em 2h
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-04:00"));
    }
}

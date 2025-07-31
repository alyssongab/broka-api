package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.auth.LoginRequestDTO;
import br.com.apibroka.broka.dto.auth.LoginResponseDTO;
import br.com.apibroka.broka.dto.auth.RegisterRequestDTO;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.repository.UserRepository;
import br.com.apibroka.broka.service.AuthorizationService;
import br.com.apibroka.broka.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    @Operation(description = "Usuário faz login no sistema e recebe token de autenticação")
    public ResponseEntity<Record> login(@RequestBody LoginRequestDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var user = (User)auth.getPrincipal();
        var token = this.tokenService.gerarToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @Operation(description = "Cadastra um novo usuario no sistema seja cliente ou dono de restaurante")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO data){
        try{
            authorizationService.registrarUsuario(data);
            return ResponseEntity.ok("Usuario registrado com sucesso");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

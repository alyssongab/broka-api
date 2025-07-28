package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.auth.LoginRequestDTO;
import br.com.apibroka.broka.dto.auth.LoginResponseDTO;
import br.com.apibroka.broka.dto.auth.RegisterRequestDTO;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.repository.UserRepository;
import br.com.apibroka.broka.service.TokenService;
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

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Record> login(@RequestBody LoginRequestDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var user = (User)auth.getPrincipal();
        var token = this.tokenService.gerarToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO data){
        if(this.userRepository.findByEmail(data.email()).isPresent()){
            return ResponseEntity.badRequest().body("Email ja cadastrado");
        }

        String encryptedPass = this.passwordEncoder.encode(data.password());
        User newUser = new User(null, data.name(), data.email(), encryptedPass, data.role());
        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}

package br.com.apibroka.broka.service;

import br.com.apibroka.broka.dto.auth.RegisterRequestDTO;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    // register
    public void registrarUsuario(RegisterRequestDTO data){
        if(userRepository.findByEmail(data.email()).isPresent()){
            // futuramente sera substituido por uma excecao personalizada
            throw new RuntimeException("Email já cadastrado");
        }
        String encryptedPassword = passwordEncoder.encode(data.password());
        User user = new User(null, data.name(), data.email(), encryptedPassword, data.role());
        this.userRepository.save(user);
    }
}

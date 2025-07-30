package br.com.apibroka.broka.service;

import br.com.apibroka.broka.dto.user.UserResponseDTO;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponseDTO> findAllUsers(){
        return userRepository.findAll().stream()
                .map(u -> new UserResponseDTO(u.getId(), u.getName(), u.getEmail(), u.getRole()))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserInfo(User currentUser){
        return new UserResponseDTO(
                currentUser.getId(),
                currentUser.getName(),
                currentUser.getEmail(),
                currentUser.getRole()
        );
    }
}

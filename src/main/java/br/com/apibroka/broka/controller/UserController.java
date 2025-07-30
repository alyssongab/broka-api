package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.user.UserResponseDTO;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping("/me")
    public UserResponseDTO getMyInfo(@AuthenticationPrincipal User currentUser){
        return userService.getUserInfo(currentUser);
    }
}

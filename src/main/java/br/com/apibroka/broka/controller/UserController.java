package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.user.UserResponseDTO;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Usuarios")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Lista todos usuários do sistema")
    public List<UserResponseDTO> getAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping("/me")
    @Operation(description = "Lista os dados do usuário logado")
    public UserResponseDTO getMyInfo(@AuthenticationPrincipal User currentUser){
        return userService.getUserInfo(currentUser);
    }
}

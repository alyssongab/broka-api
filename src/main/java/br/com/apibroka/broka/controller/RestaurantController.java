package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.restaurant.RestaurantDTO;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@Tag(name = "Restaurantes")
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    @Operation(description = "Lista todos os restaurantes")
    public List<RestaurantDTO> getAllRestaurants(){
        return restaurantService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('DONO_RESTAURANTE')")
    @Operation(description = "Adiciona um novo restaurante")
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO, @AuthenticationPrincipal User owner){
        RestaurantDTO createdRestaurant = restaurantService.create(restaurantDTO, owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DONO_RESTAURANTE')")
    @Operation(description = "Atualiza dados de um restaurante")
    public ResponseEntity<RestaurantDTO> updateRestaurant(
            @PathVariable UUID id,
            @RequestBody RestaurantDTO restaurantDTO,
            @AuthenticationPrincipal User currentUser)
    {
        RestaurantDTO updatedRestaurant = restaurantService.update(id, restaurantDTO, currentUser);
        return ResponseEntity.ok(updatedRestaurant);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DONO_RESTAURANTE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Deleta um restaurante")
    public void deleteRestaurant(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ){
        restaurantService.delete(id, currentUser);
    }
}

package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.restaurant.RestaurantDTO;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    // listar todos os restaurantes
    @GetMapping
    public List<RestaurantDTO> getAllRestaurants(){
        return restaurantService.findAll();
    }

    // criar um restaurante (apenas dono_restaurante pode criar)
    @PostMapping
    @PreAuthorize("hasRole('DONO_RESTAURANTE')")
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO, @AuthenticationPrincipal User owner){
        RestaurantDTO createdRestaurant = restaurantService.create(restaurantDTO, owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    // atualizar um restaurante (apenas dono_restaurante pode atualizar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DONO_RESTAURANTE')")
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
    public void deleteRestaurant(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser
    ){
        restaurantService.delete(id, currentUser);
    }
}

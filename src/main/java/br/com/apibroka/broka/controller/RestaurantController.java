package br.com.apibroka.broka.controller;

import br.com.apibroka.broka.dto.restaurant.RestaurantDTO;
import br.com.apibroka.broka.model.Restaurant;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // listar todos os restaurantes
    @GetMapping
    public List<RestaurantDTO> getAllRestaurants(){
        return restaurantRepository.findAll().stream()
                .map(r -> new RestaurantDTO(r.getId(), r.getName(), r.getAddress(), r.getCuisineType()))
                .collect(Collectors.toList());
    }

    // criar um restaurante (apenas dono_restaurante pode criar)
    @PostMapping
    @PreAuthorize("hasRole('DONO_RESTAURANTE')")
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO, @AuthenticationPrincipal User owner){
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.name());
        restaurant.setAddress(restaurantDTO.address());
        restaurant.setCuisineType(restaurantDTO.cuisineType());
        restaurant.setOwner(owner);

        Restaurant saved = restaurantRepository.save(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantDTO(saved.getId(), saved.getName(), saved.getAddress(), saved.getCuisineType()));
    }

    // atualizar um restaurante (apenas dono_restaurante pode atualizar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DONO_RESTAURANTE')")
    public ResponseEntity<RestaurantDTO> updateRestaurant(
            @PathVariable UUID id,
            @RequestBody RestaurantDTO restaurantDTO,
            @AuthenticationPrincipal User owner)
    {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));

        // verificacao de role
        if(!restaurant.getOwner().getId().equals(owner.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar este restaurante.");
        }

        restaurant.setName(restaurantDTO.name());
        restaurant.setAddress(restaurantDTO.address());
        restaurant.setCuisineType(restaurantDTO.cuisineType());

        Restaurant updated = restaurantRepository.save(restaurant);

        return ResponseEntity.ok(new RestaurantDTO(updated.getId(), updated.getName(), updated.getAddress(), updated.getCuisineType()));
    }
}

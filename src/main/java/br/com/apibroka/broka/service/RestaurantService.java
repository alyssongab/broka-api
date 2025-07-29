package br.com.apibroka.broka.service;

import br.com.apibroka.broka.dto.restaurant.RestaurantDTO;
import br.com.apibroka.broka.model.Restaurant;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    public List<RestaurantDTO> findAll(){
        return repository.findAll().stream()
                .map(r -> new RestaurantDTO(r.getId(), r.getName(), r.getAddress(), r.getCuisineType()))
                .collect(Collectors.toList());
    }

    @Transactional
    public RestaurantDTO create(RestaurantDTO data, User owner){
        Restaurant restaurant = new Restaurant();
        restaurant.setName(data.name());
        restaurant.setAddress(data.address());
        restaurant.setCuisineType(data.cuisineType());
        restaurant.setOwner(owner);

        Restaurant saved = repository.save(restaurant);
        return new RestaurantDTO(saved.getId(), saved.getName(), saved.getAddress(), saved.getCuisineType());
    }

    @Transactional
    public RestaurantDTO update(UUID id, RestaurantDTO dto, User currentUser){
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado."));

        if(!restaurant.getOwner().getId().equals(currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para atualizar um restaurante.");
        }

        restaurant.setName(dto.name());
        restaurant.setAddress(dto.address());
        restaurant.setCuisineType(dto.cuisineType());

        Restaurant updated = repository.save(restaurant);
        return new RestaurantDTO(updated.getId(), updated.getName(), updated.getAddress(), updated.getCuisineType());
    }

    @Transactional
    public void delete(UUID id, User currentUser){
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado."));

        if(!restaurant.getOwner().getId().equals(currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para deletar um restaurante.");
        }

        repository.delete(restaurant);
    }
}

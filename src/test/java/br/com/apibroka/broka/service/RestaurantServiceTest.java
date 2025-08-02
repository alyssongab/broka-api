package br.com.apibroka.broka.service;

import br.com.apibroka.broka.dto.restaurant.RestaurantDTO;
import br.com.apibroka.broka.model.Restaurant;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    @DisplayName("Deve criar um novo restaurante com sucesso")
    void create_shouldCreateNewRestaurantSuccessfully(){
        // Arrange
        // dados de entrada do servico
        User owner = new User();
        owner.setId(UUID.randomUUID());

        RestaurantDTO inputDto = new RestaurantDTO(null, "Restaurante Teste", "Rua teste 123", "Brasileira");

        // objeto a ser salvo e retornado pelo repository
        Restaurant restaurantToSave = new Restaurant();
        restaurantToSave.setName(inputDto.name());
        restaurantToSave.setAddress(inputDto.address());
        restaurantToSave.setCuisineType(inputDto.cuisineType());
        restaurantToSave.setOwner(owner);

        Restaurant savedRestaurant = new Restaurant();
        savedRestaurant.setId(UUID.randomUUID());
        savedRestaurant.setName(restaurantToSave.getName());
        savedRestaurant.setAddress(restaurantToSave.getAddress());
        savedRestaurant.setCuisineType(restaurantToSave.getCuisineType());
        savedRestaurant.setOwner(restaurantToSave.getOwner());

        // programando o mock:
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        // Act
        // chama o metodo que esta sendo testado
        RestaurantDTO resultDto = restaurantService.create(inputDto, owner);

        // Assert

        // resultado nao pode ser nulo
        assertThat(resultDto).isNotNull();

        // id do resultado tem que ser o msm do objeto que o mock retornou
        assertThat(resultDto.id()).isEqualTo(savedRestaurant.getId());

        // nome do resultado tem que ser o msm q foi enviado
        assertThat(resultDto.name()).isEqualTo(inputDto.name());
    }

}

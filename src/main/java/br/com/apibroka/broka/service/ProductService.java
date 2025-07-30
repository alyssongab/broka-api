package br.com.apibroka.broka.service;

import br.com.apibroka.broka.dto.product.ProductDTO;
import br.com.apibroka.broka.model.Product;
import br.com.apibroka.broka.model.Restaurant;
import br.com.apibroka.broka.model.User;
import br.com.apibroka.broka.repository.ProductRepository;
import br.com.apibroka.broka.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<ProductDTO> findProductsByRestaurant(UUID restaurantId){
        return productRepository.findAllByRestaurantId(restaurantId).stream()
                .map(p -> new ProductDTO(p.getId(), p.getName(), p.getDescription(), p.getPrice()))
                .collect(Collectors.toList());
    }

    public ProductDTO addProduct(UUID restaurantId, ProductDTO dto, User owner){
        // busca restaurante ou dispara not found
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));

        if(!restaurant.getOwner().getId().equals(owner.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para adicionar um produto ao restaurante.");

        }

        Product newProduct = new Product();
        newProduct.setName(dto.name());
        newProduct.setDescription(dto.description());
        newProduct.setPrice(dto.price());
        newProduct.setRestaurant(restaurant);

        Product saved = productRepository.save(newProduct);
        return new ProductDTO(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice());
    }

    public ProductDTO updateProduct(UUID productId, ProductDTO dto, User owner){
        // busca produto ou 404.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        if(!product.getRestaurant().getOwner().getId().equals(owner.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Você não tem permissão para atualizar um produto.");
        }

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());

        Product updated = productRepository.save(product);
        return new ProductDTO(updated.getId(), updated.getName(), updated.getDescription(), updated.getPrice());
    }

    public void deleteProduct(UUID productId, User owner){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        if (!product.getRestaurant().getOwner().getId().equals(owner.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para deletar este produto.");
        }

        productRepository.delete(product);
    }
}
